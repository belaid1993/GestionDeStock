package com.belaid.gestionDeStock.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryDto {

    private String code;

    private String designation;

    private List<AdresseDto> articles;
}
