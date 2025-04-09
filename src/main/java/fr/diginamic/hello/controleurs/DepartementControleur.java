package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.DepartementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departements")
public class DepartementControleur {

    private final DepartementService service;

    public DepartementControleur(DepartementService service) {
        this.service = service;
    }

    // CRUD

    @GetMapping
    public List<Departement> rechercherTous() {
        return service.rechercherTous();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> rechercherParId(@PathVariable int id) {
        Departement d = service.rechercherParId(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Departement> creer(@Valid @RequestBody Departement dep) {
        return ResponseEntity.ok(service.creer(dep));
    }

    @PutMapping
    public ResponseEntity<Departement> modifier(@Valid @RequestBody Departement dep) {
        return ResponseEntity.ok(service.modifier(dep));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable int id) {
        service.supprimer(id);
        return ResponseEntity.ok().build();
    }

    // BONUS 1 : villes les plus peuplées
    @GetMapping("/{id}/villes-plus-grandes")
    public List<Ville> rechercherVillesPlusPeuplees(@PathVariable int id, @RequestParam(defaultValue = "5") int n) {
        return service.rechercherVillesPlusPeuplees(id, n);
    }

    // BONUS 2 : villes dans une tranche de population
    @GetMapping("/{id}/villes-par-population")
    public List<Ville> rechercherVillesParTranchePopulation(
            @PathVariable int id,
            @RequestParam int min,
            @RequestParam int max
    ) {
        return service.rechercherVillesParTranchePopulation(id, min, max);
    }
}
