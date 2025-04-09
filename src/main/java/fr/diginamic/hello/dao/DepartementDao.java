package fr.diginamic.hello.dao;

import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DepartementDao {

    @PersistenceContext
    private EntityManager em;

    public List<Departement> rechercherTous() {
        return em.createQuery("SELECT d FROM Departement d", Departement.class).getResultList();
    }

    public Departement rechercherParId(int id) {
        return em.find(Departement.class, id);
    }

    @Transactional
    public Departement inserer(Departement dep) {
        em.persist(dep);
        return dep;
    }

    @Transactional
    public Departement modifier(Departement dep) {
        return em.merge(dep);
    }

    @Transactional
    public void supprimer(int id) {
        Departement dep = em.find(Departement.class, id);
        if (dep != null) em.remove(dep);
    }

    // BONUS : villes les plus peuplées
    public List<Ville> rechercherVillesPlusPeuplees(int depId, int n) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.departement.id = :id ORDER BY v.nbHabitants DESC",
                Ville.class);
        query.setParameter("id", depId);
        query.setMaxResults(n);
        return query.getResultList();
    }

    // BONUS : villes entre min et max habitants
    public List<Ville> rechercherVillesParTranchePopulation(int depId, int min, int max) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.departement.id = :id AND v.nbHabitants BETWEEN :min AND :max",
                Ville.class);
        return query.setParameter("id", depId)
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }
}
