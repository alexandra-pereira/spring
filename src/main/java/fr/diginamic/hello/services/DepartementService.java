package fr.diginamic.hello.services;

import fr.diginamic.hello.dao.DepartementDao;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    private final DepartementDao departementDao;

    public DepartementService(DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    public List<Departement> rechercherTous() {
        return departementDao.rechercherTous();
    }

    public Departement rechercherParId(int id) {
        return departementDao.rechercherParId(id);
    }

    public Departement creer(Departement dep) {
        return departementDao.inserer(dep);
    }

    public Departement modifier(Departement dep) {
        return departementDao.modifier(dep);
    }

    public void supprimer(int id) {
        departementDao.supprimer(id);
    }

    public List<Ville> rechercherVillesPlusPeuplees(int depId, int n) {
        return departementDao.rechercherVillesPlusPeuplees(depId, n);
    }

    public List<Ville> rechercherVillesParTranchePopulation(int depId, int min, int max) {
        return departementDao.rechercherVillesParTranchePopulation(depId, min, max);
    }
}
