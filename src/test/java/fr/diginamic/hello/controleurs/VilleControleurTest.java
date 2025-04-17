package fr.diginamic.hello.controleurs;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VilleControleur.class)
class VilleControleurTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VilleService villeService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void postValidVilleShouldReturnDto() throws Exception {
        Departement dep = new Departement();
        dep.setCode("34");
        dep.setNom("Hérault");

        Ville in = new Ville();
        in.setNom("Narbonne");
        in.setNbHabitants(53000);
        in.setDepartement(dep);

        // Service returns the same object with an ID
        Ville saved = new Ville();
        saved.setId(5);
        saved.setNom("Narbonne");
        saved.setNbHabitants(53000);
        saved.setDepartement(dep);

        Mockito.when(villeService.insererVille(Mockito.any()))
                .thenReturn(saved);

        mvc.perform(post("/villes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(in))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomVille").value("Narbonne"))
                .andExpect(jsonPath("$.nbHabitants").value(53000));
    }

    @Test
    void postInvalidVilleShouldReturn400() throws Exception {
        Ville bad = new Ville();
        bad.setNom("X");             // trop court
        bad.setNbHabitants(5);       // <10
        // pas de département

        mvc.perform(post("/villes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bad))
                )
                .andExpect(status().isBadRequest());
    }
}
