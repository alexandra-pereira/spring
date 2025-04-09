package fr.diginamic.hello.dao;

import fr.diginamic.hello.models.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class VilleDao {

    @PersistenceContext
    private EntityManager em;

    public List<Ville> rechercherToutesLesVilles() {
        return em.createQuery("SELECT v FROM Ville v", Ville.class).getResultList();
    }

    public Ville rechercherVilleParId(int id) {
        return em.find(Ville.class, id);
    }

    public Ville rechercherVilleParNom(String nom) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class);
        query.setParameter("nom", nom);
        return query.getResultStream().findFirst().orElse(null);
    }

    /**
     * Recherche les villes ayant au moins le nombre d'habitants spécifié
     */
    public List<Ville> rechercherVillesLesPlusPeuplees(int minHabitants) {
        return em.createQuery(
                        "SELECT v FROM Ville v WHERE v.nbHabitants >= :min", Ville.class)
                .setParameter("min", minHabitants)
                .getResultList();
    }

    @Transactional
    public Ville insererVille(Ville ville) {
        em.persist(ville);
        return ville;
    }

    @Transactional
    public Ville modifierVille(Ville ville) {
        return em.merge(ville);
    }

    @Transactional
    public void supprimerVille(int id) {
        Ville ville = em.find(Ville.class, id);
        if (ville != null) {
            em.remove(ville);
        } else {
            System.out.println("⚠️ Ville avec ID " + id + " non trouvée pour suppression.");
        }
    }
}
