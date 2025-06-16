package com.plast.cash.user;

import com.plast.cash.garrafa.Reciclagem;
import com.plast.cash.password.Password;
import com.plast.cash.user.dto.LoginDTO;
import com.plast.cash.user.dto.ReciclagemDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User create(final User user) {
        user.setSenha(Password.encode(user.getSenha()));
        return repository.save(user);
    }

    public User findById(final Long id) {
        return repository.findById(id).orElseThrow(notFound());
    }

    public List<Reciclagem> findGarrafasByUser(final Long id) {
        return findById(id).getReciclagens();
    }

    public User login(LoginDTO login) {

        final User user = repository.findByCpf(login.cpf()).orElseThrow(notFound());

        if (!Password.matches(login.senha(), user.getSenha())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Senha inválida");
        }

        return user;
    }

    @Transactional
    public void saveReciclagem(ReciclagemDTO reciclagem) {

        final Optional<User> user = repository.findByCpf(reciclagem.cpf());

        if (user.isEmpty()) return;

        final User userFound = user.get();
        final Reciclagem reciclagemToSave = new Reciclagem(null, reciclagem.quantidade(), LocalDate.now());
        userFound.getReciclagens().add(reciclagemToSave);

        repository.save(userFound);
    }

    private Supplier<ResponseStatusException> notFound() {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"Não encontrado");
    }
}
