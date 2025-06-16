package com.plast.cash.user;

import com.plast.cash.garrafa.Reciclagem;
import com.plast.cash.listener.SerialListener;
import com.plast.cash.user.dto.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final SerialListener listener;
    private final UserService service;

    @GetMapping("/start/{id}")
    public void sendStart(@PathVariable Long id) throws Exception {
        final User user = service.findById(id);
        listener.enviarMensagem(user.getCpf());
    }

    @GetMapping("/stop")
    public void sendStop() throws Exception {
        listener.enviarMensagem("STOP");
    }

    @PostMapping("/auth")
    public ResponseEntity<User> login(@RequestBody LoginDTO login) {
        final User user = service.login(login);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        final User createdUser = service.create(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findByid(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/garrafas")
    public ResponseEntity<List<Reciclagem>> findReciclagens(@PathVariable Long id) {
        return ResponseEntity.ok(service.findGarrafasByUser(id));
    }

}
