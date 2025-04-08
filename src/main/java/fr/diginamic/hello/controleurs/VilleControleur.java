package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.entities.Ville;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/villes") // URL de base
public class VilleControleur {

    // Liste partagée entre les méthodes GET et POST
    private List<Ville> villes = new ArrayList<>();

    public VilleControleur() {
        // villes initiales
        villes.add(new Ville(1, "Paris", 2148000));
        villes.add(new Ville(2, "Lyon", 513000));
    }

    // GET: liste complète
    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    // GET: une ville par ID
    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable int id) {
        for (Ville ville : villes) {
            if (ville.getId() == id) {
                return ResponseEntity.ok(ville);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        // Vérifie si une ville avec le même nom existe déjà
        for (Ville ville : villes) {
            if (ville.getId()== nouvelleVille.getId()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Une ville avec cet ID existe déjà.");
            }
        }
        villes.add(nouvelleVille);
        return ResponseEntity.ok("Ville ajoutée avec succès");
    }

    // PUT: modifier une ville par ID
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @RequestBody Ville villeModifiee) {
        for (Ville ville : villes) {
            if (ville.getId() == id) {
                ville.setNom(villeModifiee.getNom());
                ville.setNbHabitants(villeModifiee.getNbHabitants());
                return ResponseEntity.ok("Ville modifiée avec succès");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ville non trouvée");
    }

    // DELETE: supprimer une ville par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        Optional<Ville> villeASupprimer = villes.stream()
                .filter(v -> v.getId() == id)
                .findFirst();

        if (villeASupprimer.isPresent()) {
            villes.remove(villeASupprimer.get());
            return ResponseEntity.ok("Ville supprimée avec succès");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ville non trouvée");
        }
    }
}