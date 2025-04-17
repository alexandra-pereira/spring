package fr.diginamic.hello.services;

import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    private final DepartementRepository departementRepo;
    private final VilleRepository villeRepo;

    public DepartementService(DepartementRepository departementRepo,
                              VilleRepository villeRepo) {
        this.departementRepo = departementRepo;
        this.villeRepo = villeRepo;
    }

    public List<Departement> rechercherTous() {
        return departementRepo.findAll();
    }

    public Departement rechercherParId(int id) {
        return departementRepo.findById(id).orElse(null);
    }

    public Departement creer(Departement dep) {
        return departementRepo.save(dep);
    }

    public Departement modifier(Departement dep) {
        return departementRepo.save(dep);
    }

    public void supprimer(int id) {
        departementRepo.deleteById(id);
    }

    // —— Ajoutez ces deux méthodes pour lever l’erreur de compilation ——

    /**
     * Retourne les N villes les plus peuplées d’un département donné.
     */
    public List<Ville> rechercherVillesPlusPeuplees(int depId, int n) {
        Departement dep = rechercherParId(depId);
        if (dep == null) {
            throw new IllegalArgumentException("Département introuvable pour id=" + depId);
        }
        // on page sur la page 0 de taille n, tri descendant par population
        return villeRepo.findByDepartementCodeOrderByNbHabitantsDesc(
                dep.getCode(),
                PageRequest.of(0, n)
        );
    }

    /**
     * Retourne les villes d’un département dont la population est comprise entre min et max.
     */
    public List<Ville> rechercherVillesParTranchePopulation(int depId, int min, int max) {
        Departement dep = rechercherParId(depId);
        if (dep == null) {
            throw new IllegalArgumentException("Département introuvable pour id=" + depId);
        }
        return villeRepo.findByDepartementCodeAndNbHabitantsBetween(
                dep.getCode(),
                min,
                max
        );
    }
}
