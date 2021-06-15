package com.belaid.gestionDeStock.dto;


import com.belaid.gestionDeStock.model.MvtStk;
import com.belaid.gestionDeStock.model.SourceMvtStk;
import com.belaid.gestionDeStock.model.TypeMvt;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class MvtStkDto {

    private Integer id;

    private Instant dateMvt;

    private BigDecimal quantite;

    private ArticleDto article;

    private TypeMvt typeMvt;

    private SourceMvtStk sourceMvt;

    private Integer idEntreprise;

    public static MvtStkDto fromEntity(MvtStk mvtStk) {
        if (mvtStk == null) {
            return null;
        }

        return MvtStkDto.builder()
                .id(mvtStk.getId())
                .dateMvt(mvtStk.getDateMvt())
                .quantite(mvtStk.getQuantite())
                .article(ArticleDto.fromEntity(mvtStk.getArticle()))
                .typeMvt(mvtStk.getTypeMvt())
                .sourceMvt(mvtStk.getSourceMvt())
                .idEntreprise(mvtStk.getIdEntreprise())
                .build();
    }

    public static MvtStk toEntity(MvtStkDto mvtStkDto) {
        if (mvtStkDto == null) {
            return null;
        }

        MvtStk mvtStk = new MvtStk();
        mvtStk.setId(mvtStkDto.getId());
        mvtStk.setQuantite(mvtStkDto.getQuantite());
        mvtStk.setArticle(ArticleDto.toEntity(mvtStkDto.getArticle()));
        mvtStk.setDateMvt(mvtStkDto.getDateMvt());
        mvtStk.setTypeMvt(mvtStkDto.getTypeMvt());
        mvtStk.setSourceMvt(mvtStkDto.getSourceMvt());
        mvtStk.setIdEntreprise(mvtStkDto.getIdEntreprise());

        return mvtStk;
    }
}
