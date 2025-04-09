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
    public List<Departement> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> getById(@PathVariable int id) {
        Departement d = service.getById(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Departement> create(@Valid @RequestBody Departement dep) {
        return ResponseEntity.ok(service.create(dep));
    }

    @PutMapping
    public ResponseEntity<Departement> update(@Valid @RequestBody Departement dep) {
        return ResponseEntity.ok(service.update(dep));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // BONUS 1 : villes les + peuplées
    @GetMapping("/{id}/villes-plus-grandes")
    public List<Ville> getLargestVilles(@PathVariable int id, @RequestParam(defaultValue = "5") int n) {
        return service.getLargestVilles(id, n);
    }

    // BONUS 2 : villes par tranche de population
    @GetMapping("/{id}/villes-par-population")
    public List<Ville> getVillesInPopulationRange(
            @PathVariable int id,
            @RequestParam int min,
            @RequestParam int max
    ) {
        return service.getVillesByPopulationRange(id, min, max);
    }
}
