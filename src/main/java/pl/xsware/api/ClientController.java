package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.user.PasswordRequest;
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

    @PostMapping("/exist/{login}")
    public ResponseEntity<Boolean> existUser(@PathVariable String login) {
        boolean exist = userService.existUser(login);
        return ResponseEntity.ok(exist);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> createUser(@RequestBody @Valid UserRequest user) {
        userService.createUser(user);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/edit")
    public ResponseEntity<Response> editUser(@RequestBody @Valid UserRequest data) {
        UserDto user = UserDto.fromRegisterRequest(data);
        user.setPassword("****");
        userService.editUser(user);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid PasswordRequest data) {
        UserDto user = userService.getUserById(data.getId());
        user.setPassword(data.getPassword());
        userService.editUser(user);
        return ResponseEntity.ok(Response.create("OK"));
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<Response>  removeClient(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok(Response.create("OK"));
    }
}
