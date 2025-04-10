package fr.diginamic.hello.mappers;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.models.Ville;

public class VilleMapper {

    public static VilleDto toDto(Ville ville) {
        if (ville == null || ville.getDepartement() == null) return null;

        return new VilleDto(
                ville.getNom(),
                ville.getNbHabitants(),
                ville.getDepartement().getId(),
                ville.getDepartement().getNom()
        );
    }
}
