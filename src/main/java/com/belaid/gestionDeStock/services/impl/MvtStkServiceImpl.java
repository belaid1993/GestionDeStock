package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.MvtStkDto;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.model.TypeMvt;
import com.belaid.gestionDeStock.repository.MvtStkRepository;
import com.belaid.gestionDeStock.services.ArticleService;
import com.belaid.gestionDeStock.services.MvtStkService;
import com.belaid.gestionDeStock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {

    private MvtStkRepository mvtStkRepository;
    private ArticleService articleService;

    @Autowired
    public MvtStkServiceImpl(MvtStkRepository mvtStkRepository, ArticleService articleService) {
        this.mvtStkRepository = mvtStkRepository;
        this.articleService = articleService;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.warn("Id article is NULL");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);
        return mvtStkRepository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
        return mvtStkRepository.findAllByArticleId(idArticle).stream()
                .map(MvtStkDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto dto) {
        return entreePositive(dto, TypeMvt.ENTREE);
    }

    @Override
    public MvtStkDto sortieStock(MvtStkDto dto) {
        return sortieNegative(dto, TypeMvt.SORTIE);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto dto) {
        return entreePositive(dto, TypeMvt.CORRECTION_POS);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto dto) {
        return sortieNegative(dto, TypeMvt.CORRECTION_NEG);
    }

    private MvtStkDto entreePositive(MvtStkDto dto, TypeMvt typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("MvtStk is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue())
                )
        );
        dto.setTypeMvt(typeMvtStk);
        return MvtStkDto.fromEntity(
                mvtStkRepository.save(MvtStkDto.toEntity(dto))
        );
    }

    private MvtStkDto sortieNegative(MvtStkDto dto, TypeMvt typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("MvtStk is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue()) * -1
                )
        );
        dto.setTypeMvt(typeMvtStk);
        return MvtStkDto.fromEntity(
                mvtStkRepository.save(MvtStkDto.toEntity(dto))
        );
    }
}
