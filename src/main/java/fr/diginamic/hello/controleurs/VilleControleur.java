package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private final VilleService villeService;

    public VilleControleur(VilleService villeService) {
        this.villeService = villeService;
    }

    @GetMapping
    public List<Ville> rechercherToutesLesVilles() {
        return villeService.rechercherToutesLesVilles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ville> rechercherVilleParId(@PathVariable int id) {
        Ville ville = villeService.rechercherVilleParId(id);
        return ville != null ? ResponseEntity.ok(ville) : ResponseEntity.notFound().build();
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Ville> rechercherVilleParNom(@PathVariable String nom) {
        Ville ville = villeService.rechercherVilleParNom(nom);
        return ville != null ? ResponseEntity.ok(ville) : ResponseEntity.notFound().build();
    }

    @GetMapping("/plus-peuplees/{min}")
    public List<Ville> rechercherVillesLesPlusPeuplees(@PathVariable int min) {
        return villeService.rechercherVillesLesPlusPeuplees(min);
    }

    @PostMapping
    public ResponseEntity<?> insererVille(@Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            List<String> erreurs = result.getFieldErrors().stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        return ResponseEntity.ok(villeService.insererVille(ville));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifierVille(@PathVariable int id, @Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            List<String> erreurs = result.getFieldErrors().stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        return ResponseEntity.ok(villeService.modifierVille(id, ville));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Ville>> supprimerVille(@PathVariable int id) {
        return ResponseEntity.ok(villeService.supprimerVille(id));
    }
}
