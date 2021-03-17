package com.belaid.gestionDeStock.dto;

import com.belaid.gestionDeStock.model.Ventes;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class VentesDto {

    private Integer id;

    private String code;

    private Instant dateVente;

    private String comentaire;

    private Integer idEntreprise;

    public static VentesDto fromEntity(Ventes ventes){
        if(ventes == null){
            return null;
        }

        return VentesDto.builder()
                .id(ventes.getId())
                .code(ventes.getCode())
                .dateVente(ventes.getDateVente())
                .comentaire(ventes.getComentaire())
                .idEntreprise(ventes.getIdEntreprise())
                .build();
    }

    public Ventes toEntity(VentesDto ventesDto){
        if (ventesDto == null){
            return null;
        }

        Ventes ventes = new Ventes();
        ventes.setId(ventesDto.getId());
        ventes.setCode(ventesDto.getCode());
        ventes.setDateVente(ventesDto.getDateVente());
        ventes.setComentaire(ventesDto.getComentaire());
        ventes.setIdEntreprise(ventesDto.getIdEntreprise());

        return ventes;
    }
}
