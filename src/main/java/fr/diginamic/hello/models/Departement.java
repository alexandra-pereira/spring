package fr.diginamic.hello.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "departement")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    // Le code est la colonne sur laquelle on joint les villes
    @Column(nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    @JsonIgnore
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

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public List<Ville> getVilles() { return villes; }

    public void setVilles(List<Ville> villes) { this.villes = villes; }
}
