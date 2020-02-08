package com.toddy.ws.resources;

import com.toddy.ws.model.Role;
import com.toddy.ws.model.User;
import com.toddy.ws.dto.UserDTO;
import com.toddy.ws.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> users = userService.findAll();
        List<UserDTO> usersDTO = users.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(usersDTO);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable String id) {
        User user = userService.fetchUserById(id);
        return ResponseEntity.ok().body(new UserDTO(user));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO userDTO){
        User user = userService.fromDTO(userDTO);
        return ResponseEntity.ok().body(new UserDTO(userService.insertUser(user)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO){
        User user = userService.fromDTO(userDTO);
        user.setId(id);
        return ResponseEntity.ok().body(new UserDTO(userService.updateUser(user)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}/roles")
    public ResponseEntity<List<Role>> fetchRoles(@PathVariable String id){
        User user = userService.fetchUserById(id);

        return ResponseEntity.ok().body(user.getRoles());
    }

    @GetMapping(value="/users/main")
    public ResponseEntity<UserDTO> getUserMain(Principal principal){
        User user = userService.findByEmail(principal.getName());
        UserDTO userDTO = new UserDTO(user);
        userDTO.setPassword("");
        return ResponseEntity.ok().body(userDTO);
    }

}
