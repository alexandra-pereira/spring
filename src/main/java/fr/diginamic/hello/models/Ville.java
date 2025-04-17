package fr.diginamic.hello.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ville")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Le nom ne peut pas être nul.")
    @Size(min = 2, message = "Le nom doit contenir au moins 2 caractères.")
    private String nom;

    // Dans le script recensement.sql, le champ population s’appelle "population"
    @Column(name = "population")
    @Min(value = 1, message = "Le nombre d'habitants doit être ≥ 1.")
    private int nbHabitants;

    @ManyToOne
    // Dans le script, la colonne étrangère vers departement est sans doute "code_departement"
    @JoinColumn(name = "code_departement", referencedColumnName = "code")
    @NotNull(message = "Le département ne peut pas être nul.")
    private Departement departement;


    public Ville() {}

    public Ville(int id, String nom, int nbHabitants) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }

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

    public int getNbHabitants() {
        return nbHabitants;
    }

    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}
