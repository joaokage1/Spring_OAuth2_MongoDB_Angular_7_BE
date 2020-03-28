package com.toddy.ws.resources;

import com.toddy.ws.model.Role;
import com.toddy.ws.model.User;
import com.toddy.ws.dto.UserDTO;
import com.toddy.ws.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Api(description = "Endpoints para criar, retornar, atualizar e deletar usuários.")
public class UserResource {

    @Autowired
    private UserService userService;

    TokenStore tokenStore = new InMemoryTokenStore();

    @Autowired
    DefaultTokenServices tokenServices = new DefaultTokenServices();

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> users = userService.findAll();
        List<UserDTO> usersDTO = users.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(usersDTO);
    }

    @GetMapping("/users/{id}")
    @ApiOperation("Retorna um especifico usuário através do seu identificador.")
    public ResponseEntity<UserDTO> fetchUserById(@ApiParam("Id do usuário. Não pode ser vazio") @PathVariable String id) {
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

    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenServices.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            tokenServices.revokeToken(String.valueOf(accessToken));
        }
        return ResponseEntity.noContent().build();
    }

}
