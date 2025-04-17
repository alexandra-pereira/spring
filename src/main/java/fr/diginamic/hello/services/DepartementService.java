package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.BusinessException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartementService {

    private final DepartementRepository repo;
    private final VilleRepository villeRepo;

    public DepartementService(DepartementRepository repo, VilleRepository villeRepo) {
        this.repo = repo;
        this.villeRepo = villeRepo;
    }

    // --- Vos CRUD existants ---

    public List<Departement> rechercherTous() {
        return repo.findAll();
    }

    public Departement rechercherParId(int id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public Departement creer(Departement dep) {
        // validations…
        return repo.save(dep);
    }

    @Transactional
    public Departement modifier(Departement dep) {
        // validations…
        return repo.save(dep);
    }

    @Transactional
    public void supprimer(int id) {
        // validations…
        repo.deleteById(id);
    }

    // --- Méthodes sur les villes d’un département ---

    /**
     * Retourne les N villes les plus peuplées d’un département donné (ID).
     */
    public List<Ville> rechercherVillesPlusPeuplees(int depId, int n) {
        Departement dep = rechercherParId(depId);
        if (dep == null) {
            throw new BusinessException("Département introuvable pour id=" + depId);
        }
        Pageable p = PageRequest.of(0, n);
        // On retourne directement la List<Ville> fournie par le repository
        return villeRepo.findByDepartementCodeOrderByNbHabitantsDesc(dep.getCode(), p);
    }

    /**
     * Retourne les villes d’un département dont la population est comprise entre min et max.
     */
    public List<Ville> rechercherVillesParTranchePopulation(int depId, int min, int max) {
        Departement dep = rechercherParId(depId);
        if (dep == null) {
            throw new BusinessException("Département introuvable pour id=" + depId);
        }
        return villeRepo.findByDepartementCodeAndNbHabitantsBetween(dep.getCode(), min, max);
    }

    /**
     * Retourne **toutes** les villes d’un département triées par population décroissante.
     */
    public List<Ville> listerVillesDuDepartement(String code) {
        // Pageable.unpaged() marche aussi, mais on peut directement passer PageRequest.of(0, Integer.MAX_VALUE)
        Pageable p = Pageable.unpaged();
        return villeRepo.findByDepartementCodeOrderByNbHabitantsDesc(code, p);
    }
}
