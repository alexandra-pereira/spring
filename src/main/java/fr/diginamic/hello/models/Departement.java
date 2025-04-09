package fr.diginamic.hello.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    @JsonIgnore // Pour éviter les boucles infinies dans le JSON (Jackson)
    private List<Ville> villes;

    public Departement() {}

    public Departement(String nom) {
        this.nom = nom;
    }

    // Getters and setters
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public List<Ville> getVilles() { return villes; }

    public void setVilles(List<Ville> villes) { this.villes = villes; }
}
