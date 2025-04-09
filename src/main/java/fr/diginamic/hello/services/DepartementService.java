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

    public List<Departement> getAll() {
        return departementDao.findAll();
    }

    public Departement getById(int id) {
        return departementDao.findById(id);
    }

    public Departement create(Departement dep) {
        return departementDao.insert(dep);
    }

    public Departement update(Departement dep) {
        return departementDao.update(dep);
    }

    public void delete(int id) {
        departementDao.delete(id);
    }

    public List<Ville> getLargestVilles(int depId, int n) {
        return departementDao.findLargestVilles(depId, n);
    }

    public List<Ville> getVillesByPopulationRange(int depId, int min, int max) {
        return departementDao.findVillesByPopulationRange(depId, min, max);
    }
}
