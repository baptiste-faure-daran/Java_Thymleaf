package com.thymeleaf.beta.model;


public class Personnage {

    private int id;
    private String name;
    private String classe;

    public Personnage() {

    }

    public Personnage(String name, String classe) {
        this.name = name;
        this.classe = classe;
    }

    public Personnage(int id, String name, String classe) {
        this.id = id;
        this.name = name;
        this.classe = classe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + classe + '\'' +
                '}';
    }

}