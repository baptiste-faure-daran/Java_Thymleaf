package com.thymeleaf.beta.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.NoSuchElementException;


import com.thymeleaf.beta.form.PersonForm;
import com.thymeleaf.beta.model.Personnage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Controller
public class MainController {

    private static List<Personnage> personnages = new ArrayList<Personnage>();

    static {
        personnages.add(new Personnage(1, "Baptiste", "Guerrier"));
        personnages.add(new Personnage(2, "Julien", "Mage"));
        personnages.add(new Personnage(3, "Marvin", "Guerrier"));
    }

    // Injecte (inject) via application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", message);
        return "index";
    }


    @RequestMapping(value = { "/addPersonnage" }, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {
        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);
        return "addPerson";
    }

    @RequestMapping(value = {"/personnages"}, method = RequestMethod.GET)
    public String characterList(Model model) {
        ResponseEntity<Personnage[]> response =
                restTemplate.getForEntity(
                        "http://localhost:8081/personnages",
                        Personnage[].class);
        Personnage[] personnages = response.getBody();
        model.addAttribute("personnages", personnages);
        return "personList";
    }

    @RequestMapping(value = {"/personnages/{id}"}, method = RequestMethod.GET)
    public String character(Model model, @PathVariable int id) {

        ResponseEntity<Personnage> response =
                restTemplate.getForEntity(
                        "http://localhost:8081/personnages/" + id,
                        Personnage.class);
        Personnage personnage = response.getBody();
        model.addAttribute("personnages", personnage);
        return "person";

    }

    @PostMapping(value = { "/addPersonnage" })
    public String savePerson(Model model, //
                             @ModelAttribute("personForm") PersonForm personForm) {
        String name = personForm.getName();
        String classe = personForm.getClasse();

        if (name != null && name.length() > 0 && classe != null && classe.length() > 0) {

            Personnage maxId = personnages
                    .stream()
                    .max(Comparator.comparing(Personnage::getId))
                    .orElseThrow(NoSuchElementException::new);
            Personnage newPersonnage = new Personnage(maxId.getId() + 1, name, classe);
            ResponseEntity<Personnage> response = restTemplate.postForEntity("http://localhost:8081/personnages",
                    newPersonnage,
                    Personnage.class);

//            personnage.add(newPersonnage);

            return "redirect:/personnages";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addPerson";
    }

    @RequestMapping(value = {"/supprimer/{id}"}, method = RequestMethod.GET)
    public String delete(@PathVariable int id) {
        restTemplate.delete("http://localhost:8081/personnages/" + id + "/supprimer");
        return "redirect:/personnages";
    }

}