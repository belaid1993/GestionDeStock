package com.belaid.gestionDeStock.dto;

import com.belaid.gestionDeStock.model.Roles;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolesDto {

    private Integer id;

    private String roleName;

    private UtilisateurDto utilisateur;

    private Integer idEntreprise;

    public static RolesDto fromEntity(Roles roles) {
        if (roles == null) {
            return null;
        }

        return RolesDto.builder()
                .id(roles.getId())
                .roleName(roles.getRoleName())
                .build();
    }

    public Roles toEntity(RolesDto rolesDto) {
        if (rolesDto == null) {
            return null;
        }

        Roles roles = new Roles();
        roles.setId(rolesDto.getId());
        roles.setRoleName(rolesDto.getRoleName());
        roles.setUtilisateur(UtilisateurDto.toEntity(rolesDto.getUtilisateur()));

        return roles;
    }
}
