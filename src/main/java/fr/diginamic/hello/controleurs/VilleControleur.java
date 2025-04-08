package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.entities.Ville;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/villes") // URL de base
public class VilleControleur {

    // Liste partagée entre les méthodes GET et POST
    private List<Ville> villes = new ArrayList<>();

    public VilleControleur() {
        // villes initiales
        villes.add(new Ville("Paris", 2148000));
        villes.add(new Ville("Lyon", 513000));
    }

    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        // Vérifie si une ville avec le même nom existe déjà
        for (Ville ville : villes) {
            if (ville.getNom().equalsIgnoreCase(nouvelleVille.getNom())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La ville existe déjà");
            }
        }

        villes.add(nouvelleVille);
        return ResponseEntity.ok("Ville insérée avec succès");
    }
}