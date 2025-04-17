package fr.diginamic.hello.services;

import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilleService {

    private final VilleRepository repo;

    public VilleService(VilleRepository repo) {
        this.repo = repo;
    }

    // Pagination de toutes les villes
    public Page<Ville> rechercherToutesLesVilles(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Ville rechercherVilleParId(int id) {
        return repo.findById(id).orElse(null);
    }

    public Ville rechercherVilleParNom(String nom) {
        return repo.findByNom(nom);
    }

    public List<Ville> rechercherVillesCommencantPar(String prefix) {
        return repo.findByNomStartingWithIgnoreCase(prefix);
    }

    public List<Ville> rechercherVillesPlusPeuplees(int min) {
        return repo.findByNbHabitantsGreaterThanEqual(min);
    }

    public List<Ville> rechercherVillesEntreMinEtMax(int min, int max) {
        return repo.findByNbHabitantsBetween(min, max);
    }

    public List<Ville> rechercherVillesParDepartementEtMin(String code, int min) {
        return repo.findByDepartementCodeAndNbHabitantsGreaterThanEqual(code, min);
    }

    public List<Ville> rechercherVillesParDepartementEtPlage(String code, int min, int max) {
        return repo.findByDepartementCodeAndNbHabitantsBetween(code, min, max);
    }

    public List<Ville> rechercherTopNVillesParDepartement(String code, int n) {
        Pageable p = PageRequest.of(0, n);
        return repo.findByDepartementCodeOrderByNbHabitantsDesc(code, p);
    }

    public Ville insererVille(Ville ville) {
        return repo.save(ville);
    }

    public Ville modifierVille(int id, Ville ville) {
        ville.setId(id);
        return repo.save(ville);
    }

    public void supprimerVille(int id) {
        repo.deleteById(id);
    }
}
