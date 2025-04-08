package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.entities.Ville;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/villes") // URL de base
public class VilleControleur {
    @GetMapping
    public List<Ville> getVilles() {
        return Arrays.asList(
                new Ville("Paris", 2148000),
                new Ville("Lyon", 513000),
                new Ville("Marseille", 861000)
        );
    }
}
