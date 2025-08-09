package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.user.UserRequest;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.user.UserDto;
import pl.xsware.domain.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllClients() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> getClient(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/exist")
    public ResponseEntity<Boolean> existUser(@RequestBody @Valid UserRequest user) {
        boolean exist = userService.existUser(user);
        return ResponseEntity.ok(exist);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> createUser(@RequestBody @Valid UserRequest user) {
        userService.createUser(user);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/edit")
    public ResponseEntity<Response> editUser(@RequestBody @Valid UserRequest user) {
        userService.editUser(user);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<Response>  removeClient(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok(Response.create("OK"));
    }
}
