package fr.diginamic.hello.dto;

public class DepartementDto {

    private int id;
    private String nom;
    private int nbTotalHabitants;

    public DepartementDto() {}

    public DepartementDto(int id, String nom, int nbTotalHabitants) {
        this.id = id;
        this.nom = nom;
        this.nbTotalHabitants = nbTotalHabitants;
    }

    // Getters / Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbTotalHabitants() {
        return nbTotalHabitants;
    }

    public void setNbTotalHabitants(int nbTotalHabitants) {
        this.nbTotalHabitants = nbTotalHabitants;
    }
}
