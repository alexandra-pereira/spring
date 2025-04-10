package fr.diginamic.hello.mappers;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;

public class DepartementMapper {

    public static DepartementDto toDto(Departement dep) {
        if (dep == null) return null;

        int total = 0;
        if (dep.getVilles() != null) {
            for (Ville ville : dep.getVilles()) {
                total += ville.getNbHabitants();
            }
        }

        return new DepartementDto(dep.getId(), dep.getNom(), total);
    }
}
