package com.plast.cash.user.dto;


import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(

        @NotEmpty(message =  "Cpf obrigatório")
        String cpf,

        @NotEmpty(message =  "Senha obrigatório")
        String senha
) {
}
