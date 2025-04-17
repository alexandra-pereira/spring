package fr.diginamic.hello.services;

import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.VilleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class VilleServiceTest {

    @Autowired
    private VilleService villeService;

    @Autowired
    private DepartementRepository departementRepo;

    @Autowired
    private VilleRepository villeRepo;

    private Departement dept34;

    @BeforeEach
    void setUp() {
        villeRepo.deleteAll();
        departementRepo.deleteAll();

        dept34 = new Departement();
        dept34.setCode("34");
        dept34.setNom("Hérault");
        departementRepo.save(dept34);

        // deux villes dans le département 34
        Ville v1 = new Ville();
        v1.setNom("Montpellier");
        v1.setNbHabitants(300000);
        v1.setDepartement(dept34);
        villeRepo.save(v1);

        Ville v2 = new Ville();
        v2.setNom("Béziers");
        v2.setNbHabitants(70000);
        v2.setDepartement(dept34);
        villeRepo.save(v2);
    }

    @Test
    void testRechercherVillesPlusPeuplees() {
        List<Ville> result = villeService.rechercherVillesPlusPeuplees(34, 1);
        assertEquals(1, result.size());
        assertEquals("Montpellier", result.get(0).getNom());
    }

    @Test
    void testRechercherVillesParTranche() {
        List<Ville> result = villeService.rechercherVillesParTranchePopulation(34, 50000, 200000);
        assertEquals(1, result.size());
        assertEquals("Béziers", result.get(0).getNom());
    }

    @Test
    void testInsererVilleDuplicateShouldFail() {
        Ville dup = new Ville();
        dup.setNom("Montpellier");
        dup.setNbHabitants(100000);
        dup.setDepartement(dept34);

        Exception ex = assertThrows(RuntimeException.class,
                () -> villeService.insererVille(dup));  // ou BusinessException si vous l'avez
        assertTrue(ex.getMessage().contains("existe déjà"));
    }
}
