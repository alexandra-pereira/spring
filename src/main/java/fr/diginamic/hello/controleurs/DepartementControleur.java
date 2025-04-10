package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.mappers.DepartementMapper;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.DepartementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departements")
public class DepartementControleur {

    private final DepartementService service;

    public DepartementControleur(DepartementService service) {
        this.service = service;
    }

    // ✅ GET : Afficher les départements (avec total habitants)
    @GetMapping
    public List<DepartementDto> rechercherTous() {
        return service.rechercherTous()
                .stream()
                .map(DepartementMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> rechercherParId(@PathVariable int id) {
        Departement dep = service.rechercherParId(id);
        return dep != null ? ResponseEntity.ok(DepartementMapper.toDto(dep)) : ResponseEntity.notFound().build();
    }

    // ✅ POST, PUT, DELETE : pas besoin de mapper
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

    // BONUS 1 : villes les + peuplées
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
