package fr.diginamic.hello.services;

import fr.diginamic.hello.dao.VilleDao;
import fr.diginamic.hello.models.Ville;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilleService {

    private final VilleDao villeDao;

    public VilleService(VilleDao villeDao) {
        this.villeDao = villeDao;
    }

    public List<Ville> rechercherToutesLesVilles() {
        return villeDao.rechercherToutesLesVilles();
    }

    public Ville rechercherVilleParId(int id) {
        return villeDao.rechercherVilleParId(id);
    }

    public Ville rechercherVilleParNom(String nom) {
        return villeDao.rechercherVilleParNom(nom);
    }

    public List<Ville> rechercherVillesLesPlusPeuplees(int minHabitants) {
        return villeDao.rechercherVillesLesPlusPeuplees(minHabitants);
    }

    public List<Ville> insererVille(Ville ville) {
        villeDao.insererVille(ville);
        return villeDao.rechercherToutesLesVilles();
    }

    public List<Ville> modifierVille(int id, Ville villeModifiee) {
        Ville villeExistante = villeDao.rechercherVilleParId(id);
        if (villeExistante != null) {
            villeExistante.setNom(villeModifiee.getNom());
            villeExistante.setNbHabitants(villeModifiee.getNbHabitants());
            villeExistante.setDepartement(villeModifiee.getDepartement());
            villeDao.modifierVille(villeExistante);
        }
        return villeDao.rechercherToutesLesVilles();
    }

    public List<Ville> supprimerVille(int id) {
        villeDao.supprimerVille(id);
        return villeDao.rechercherToutesLesVilles();
    }
}
