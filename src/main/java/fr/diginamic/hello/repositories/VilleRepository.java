package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.Ville;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VilleRepository extends JpaRepository<Ville, Integer> {

    Ville findByNom(String nom);

    List<Ville> findByNomStartingWithIgnoreCase(String prefix);

    List<Ville> findByNbHabitantsGreaterThanEqual(int min);

    List<Ville> findByNbHabitantsBetween(int min, int max);

    List<Ville> findByDepartementCodeAndNbHabitantsGreaterThanEqual(String code, int min);

    List<Ville> findByDepartementCodeAndNbHabitantsBetween(String code, int min, int max);

    List<Ville> findByDepartementCodeOrderByNbHabitantsDesc(String code, Pageable pageable);
}
