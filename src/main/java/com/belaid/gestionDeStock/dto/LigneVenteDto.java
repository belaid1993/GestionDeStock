package com.belaid.gestionDeStock.dto;


import com.belaid.gestionDeStock.model.LigneVente;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneVenteDto {

    private Integer id;

    private VentesDto vente;

    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    public LigneVenteDto fromEntity(LigneVente ligneVente) {
        if (ligneVente == null) {
            return null;
        }

        return LigneVenteDto.builder()
                .id(ligneVente.getId())
                .vente(VentesDto.fromEntity(ligneVente.getVente()))
                .quantite(ligneVente.getQuantite())
                .prixUnitaire(ligneVente.getPrixUnitaire())
                .build();
    }

    public LigneVente toEntity(LigneVenteDto ligneVenteDto) {
        if (ligneVenteDto == null) {
            return null;
        }

        LigneVente ligneVente = new LigneVente();
        ligneVente.setId(ligneVenteDto.getId());
        ligneVente.setQuantite(ligneVenteDto.getQuantite());
        ligneVente.setPrixUnitaire(ligneVenteDto.getPrixUnitaire());

        return ligneVente;
    }
}
