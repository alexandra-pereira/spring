package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.mappers.VilleMapper;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private final VilleService villeService;

    public VilleControleur(VilleService villeService) {
        this.villeService = villeService;
    }

    @GetMapping
    public List<VilleDto> rechercherToutesLesVilles() {
        return villeService.rechercherToutesLesVilles()
                .stream()
                .map(VilleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> rechercherVilleParId(@PathVariable int id) {
        Ville ville = villeService.rechercherVilleParId(id);
        return ville != null ? ResponseEntity.ok(VilleMapper.toDto(ville)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<VilleDto> rechercherVilleParNom(@PathVariable String nom) {
        Ville ville = villeService.rechercherVilleParNom(nom);
        return ville != null ? ResponseEntity.ok(VilleMapper.toDto(ville)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/plus-peuplees/{min}")
    public List<VilleDto> rechercherVillesLesPlusPeuplees(@PathVariable int min) {
        return villeService.rechercherVillesLesPlusPeuplees(min)
                .stream()
                .map(VilleMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> insererVille(@Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            List<String> erreurs = result.getFieldErrors().stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        return ResponseEntity.ok(
                villeService.insererVille(ville)
                        .stream()
                        .map(VilleMapper::toDto)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifierVille(@PathVariable int id, @Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            List<String> erreurs = result.getFieldErrors().stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        return ResponseEntity.ok(
                villeService.modifierVille(id, ville)
                        .stream()
                        .map(VilleMapper::toDto)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<VilleDto>> supprimerVille(@PathVariable int id) {
        return ResponseEntity.ok(
                villeService.supprimerVille(id)
                        .stream()
                        .map(VilleMapper::toDto)
                        .toList()
        );
    }
}
