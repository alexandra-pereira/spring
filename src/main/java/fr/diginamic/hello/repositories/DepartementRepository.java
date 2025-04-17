package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Integer> {
    Optional<Departement> findByCode(String code);
}
