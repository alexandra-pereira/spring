package fr.diginamic.hello.controleurs;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.mappers.DepartementMapper;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.DepartementService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departements")
public class DepartementControleur {

    private final DepartementService service;
    private final RestTemplate restTemplate;

    @Autowired
    public DepartementControleur(DepartementService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
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

    /**
     * Export PDF pour un département donné.
     */
    @GetMapping("/{code}/export/pdf")
    public void exportPdf(
            @PathVariable String code,
            HttpServletResponse response
    ) throws DocumentException, IOException {
        // Récupérer le nom du département via l’API externe
        String url = "https://geo.api.gouv.fr/departements/" + code + "?fields=nom,code,codeRegion";
        @SuppressWarnings("unchecked")
        Map<String, Object> dto = restTemplate.getForObject(url, Map.class);
        String nomDept = dto != null ? (String) dto.get("nom") : "Inconnu";

        // Préparer le PDF
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=departement_" + code + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Titre
        document.add(new Paragraph("Département : " + nomDept + " (" + code + ")"));
        document.add(new Paragraph(" "));

        // Table des villes
        List<Ville> villes = service.listerVillesDuDepartement(code);
        PdfPTable table = new PdfPTable(2);
        table.addCell("Ville");
        table.addCell("Population");
        for (Ville v : villes) {
            table.addCell(v.getNom());
            table.addCell(String.valueOf(v.getNbHabitants()));
        }
        document.add(table);

        document.close();
    }
}
