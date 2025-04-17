package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.BusinessException;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VilleService {

    private final VilleRepository repo;

    public VilleService(VilleRepository repo) {
        this.repo = repo;
    }

    // Lecture paginée
    public Page<Ville> rechercherToutesLesVilles(Pageable pageable) {
        return repo.findAll(pageable);
    }

    // Lecture par ID
    public Ville rechercherVilleParId(int id) {
        return repo.findById(id).orElse(null);
    }

    // Lecture par nom
    public Ville rechercherVilleParNom(String nom) {
        return repo.findByNom(nom);
    }

    // Recherches avec gestion d'exception si résultat vide
    public List<Ville> rechercherVillesCommencantPar(String prefix) {
        List<Ville> result = repo.findByNomStartingWithIgnoreCase(prefix);
        if (result.isEmpty()) {
            throw new BusinessException(
                    "Aucune ville dont le nom commence par '" + prefix + "' n’a été trouvée"
            );
        }
        return result;
    }

    public List<Ville> rechercherVillesPlusPeuplees(int min) {
        List<Ville> result = repo.findByNbHabitantsGreaterThanEqual(min);
        if (result.isEmpty()) {
            throw new BusinessException(
                    "Aucune ville n’a une population supérieure à " + min
            );
        }
        return result;
    }

    public List<Ville> rechercherVillesEntreMinEtMax(int min, int max) {
        List<Ville> result = repo.findByNbHabitantsBetween(min, max);
        if (result.isEmpty()) {
            throw new BusinessException(
                    "Aucune ville n’a une population comprise entre " + min + " et " + max
            );
        }
        return result;
    }

    public List<Ville> rechercherVillesParDepartementEtMin(String code, int min) {
        List<Ville> result = repo.findByDepartementCodeAndNbHabitantsGreaterThanEqual(code, min);
        if (result.isEmpty()) {
            throw new BusinessException(
                    "Aucune ville n’a une population supérieure à " + min +
                            " dans le département " + code
            );
        }
        return result;
    }

    public List<Ville> rechercherVillesParDepartementEtPlage(String code, int min, int max) {
        List<Ville> result = repo.findByDepartementCodeAndNbHabitantsBetween(code, min, max);
        if (result.isEmpty()) {
            throw new BusinessException(
                    "Aucune ville n’a une population comprise entre " + min +
                            " et " + max + " dans le département " + code
            );
        }
        return result;
    }

    public List<Ville> rechercherTopNVillesParDepartement(String code, int n) {
        Pageable page = PageRequest.of(0, n);
        List<Ville> result = repo.findByDepartementCodeOrderByNbHabitantsDesc(code, page);
        if (result.isEmpty()) {
            throw new BusinessException(
                    "Aucune ville n’a été trouvée dans le département " + code
            );
        }
        return result;
    }

    // Insertion avec validations métier
    @Transactional
    public Ville insererVille(Ville ville) {
        if (ville.getNbHabitants() < 10) {
            throw new BusinessException("La ville doit avoir au moins 10 habitants.");
        }
        if (ville.getNom() == null || ville.getNom().length() < 2) {
            throw new BusinessException("Le nom de la ville doit contenir au moins 2 lettres.");
        }
        String codeDept = ville.getDepartement().getCode();
        if (codeDept == null || codeDept.length() != 2) {
            throw new BusinessException("Le code du département doit faire exactement 2 caractères.");
        }
        if (repo.existsByNomAndDepartementCode(ville.getNom(), codeDept)) {
            throw new BusinessException(
                    "La ville '" + ville.getNom() + "' existe déjà dans le département " + codeDept + "."
            );
        }
        return repo.save(ville);
    }

    // Mise à jour : on réutilise les mêmes règles
    @Transactional
    public Ville modifierVille(int id, Ville ville) {
        ville.setId(id);
        return insererVille(ville);
    }

    // Suppression
    @Transactional
    public void supprimerVille(int id) {
        repo.deleteById(id);
    }
}
