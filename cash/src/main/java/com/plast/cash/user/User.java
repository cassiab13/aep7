package com.plast.cash.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plast.cash.garrafa.Reciclagem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String nome;

    @NotEmpty
    @Column(unique = true)
    private String cpf;

    @Setter
    @NotNull
    private String senha;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Reciclagem> reciclagens = new ArrayList<>();

    @JsonProperty("quantidadeTotal")
    public Integer getQuantidadeTotal() {
        return reciclagens
                .stream()
                .mapToInt(Reciclagem::getQuantidade)
                .sum();
    }

}
