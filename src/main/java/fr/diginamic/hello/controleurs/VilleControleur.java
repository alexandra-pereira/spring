package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.mappers.VilleMapper;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/villes")
public class VilleControleur {

    // ⚠️ on nomme bien le champ "villeService"
    private final VilleService villeService;
    private final RestTemplate restTemplate;

    // ⚠️ et le constructeur prend la même variable
    @Autowired
    public VilleControleur(VilleService villeService, RestTemplate restTemplate) {
        this.villeService = villeService;
        this.restTemplate = restTemplate;    }

    @GetMapping
    public Page<VilleDto> rechercherToutesLesVilles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<Ville> p = villeService.rechercherToutesLesVilles(PageRequest.of(page, size));
        return p.map(VilleMapper::toDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> rechercherVilleParId(@PathVariable int id) {
        Ville ville = villeService.rechercherVilleParId(id);
        return ville != null
                ? ResponseEntity.ok(VilleMapper.toDto(ville))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<VilleDto> rechercherVilleParNom(@PathVariable String nom) {
        Ville ville = villeService.rechercherVilleParNom(nom);
        return ville != null
                ? ResponseEntity.ok(VilleMapper.toDto(ville))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/commence-par/{prefix}")
    public List<VilleDto> rechercherVillesCommencantPar(@PathVariable String prefix) {
        return villeService.rechercherVillesCommencantPar(prefix)
                .stream()
                .map(VilleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/population/min/{min}")
    public List<VilleDto> rechercherVillesPlusPeuplees(@PathVariable int min) {
        return villeService.rechercherVillesPlusPeuplees(min)
                .stream()
                .map(VilleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/population/{min}/{max}")
    public List<VilleDto> rechercherVillesEntreMinEtMax(@PathVariable int min, @PathVariable int max) {
        return villeService.rechercherVillesEntreMinEtMax(min, max)
                .stream()
                .map(VilleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/departement/{code}/min/{min}")
    public List<VilleDto> rechercherVillesParDepartementEtMin(@PathVariable String code, @PathVariable int min) {
        return villeService.rechercherVillesParDepartementEtMin(code, min)
                .stream()
                .map(VilleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/departement/{code}/between/{min}/{max}")
    public List<VilleDto> rechercherVillesParDepartementEtPlage(
            @PathVariable String code,
            @PathVariable int min,
            @PathVariable int max
    ) {
        return villeService.rechercherVillesParDepartementEtPlage(code, min, max)
                .stream()
                .map(VilleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/departement/{code}/top/{n}")
    public List<VilleDto> rechercherTopNVillesParDepartement(@PathVariable String code, @PathVariable int n) {
        return villeService.rechercherTopNVillesParDepartement(code, n)
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
        Ville saved = villeService.insererVille(ville);
        return ResponseEntity.ok(VilleMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifierVille(
            @PathVariable int id,
            @Valid @RequestBody Ville ville,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> erreurs = result.getFieldErrors().stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }
        Ville updated = villeService.modifierVille(id, ville);
        return ResponseEntity.ok(VilleMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVille(@PathVariable int id) {
        villeService.supprimerVille(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Export CSV de toutes les villes dont la population ≥ min.
     */
    @GetMapping(value = "/export/csv", produces = "text/csv")
    public void exportCsv(
            @RequestParam(name = "min") int min,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=villes.csv");

        PrintWriter writer = response.getWriter();
        // En‑tête CSV
        writer.println("nomVille;nbHabitants;codeDepartement;nomDepartement");

        List<Ville> villes = villeService.rechercherVillesPlusPeuplees(min);
        for (Ville v : villes) {
            String codeDept = v.getDepartement().getCode();
            // Appel API externe pour récupérer le nom du département
            String url = "https://geo.api.gouv.fr/departements/" + codeDept + "?fields=nom,code,codeRegion";
            @SuppressWarnings("unchecked")
            Map<String, Object> dto = restTemplate.getForObject(url, Map.class);
            String nomDept = dto != null ? (String) dto.get("nom") : "";

            // Écrire la ligne CSV (séparateur ;)
            writer.printf(
                    "\"%s\";%d;%s;\"%s\"%n",
                    v.getNom(),
                    v.getNbHabitants(),
                    codeDept,
                    nomDept
            );
        }
        writer.flush();
    }
}
