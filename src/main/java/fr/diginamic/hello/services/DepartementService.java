package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.BusinessException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.repositories.DepartementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {

    private final DepartementRepository repo;

    public DepartementService(DepartementRepository repo) {
        this.repo = repo;
    }

    /**
     * Récupère tous les départements (sans validation spécifique).
     */
    public List<Departement> rechercherTous() {
        return repo.findAll();
    }

    /**
     * Récupère un département par ID.
     */
    public Departement rechercherParId(int id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Création d’un département avec validations métier.
     */
    @Transactional
    public Departement creer(Departement dep) {
        // Nom obligatoire ≥ 3 lettres
        if (dep.getNom() == null || dep.getNom().trim().length() < 3) {
            throw new BusinessException("Le nom du département doit comporter au moins 3 lettres.");
        }
        // Code département entre 2 et 3 caractères
        String code = dep.getCode();
        if (code == null || code.length() < 2 || code.length() > 3) {
            throw new BusinessException("Le code du département doit faire entre 2 et 3 caractères.");
        }
        // Unicité du code
        Optional<Departement> exist = repo.findByCode(code);
        if (exist.isPresent()) {
            throw new BusinessException("Le code '" + code + "' est déjà utilisé.");
        }
        return repo.save(dep);
    }

    /**
     * Mise à jour d’un département : mêmes validations que pour la création.
     */
    @Transactional
    public Departement modifier(Departement dep) {
        if (dep.getId() == null || !repo.existsById(dep.getId())) {
            throw new BusinessException("Impossible de modifier : département introuvable (id=" + dep.getId() + ").");
        }
        // On réapplique les règles métier de création
        return creer(dep);
    }

    /**
     * Suppression d’un département.
     */
    @Transactional
    public void supprimer(int id) {
        if (!repo.existsById(id)) {
            throw new BusinessException("Impossible de supprimer : département introuvable (id=" + id + ").");
        }
        repo.deleteById(id);
    }
}
