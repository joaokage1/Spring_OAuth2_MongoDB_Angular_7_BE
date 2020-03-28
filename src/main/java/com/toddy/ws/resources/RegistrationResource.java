package com.toddy.ws.resources;

import com.toddy.ws.dto.UserDTO;
import com.toddy.ws.model.User;
import com.toddy.ws.model.VerificationToken;
import com.toddy.ws.resources.util.GenericResponse;
import com.toddy.ws.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/public")
public class RegistrationResource {

    @Autowired
    UserService userService;

    @PostMapping("/registration/users")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO){
        User user = this.userService.fromDTO(userDTO);
        this.userService.registerUser(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/registrationConfirm/users")
    public ResponseEntity<GenericResponse> confirmRegistrationUser(@RequestParam("token") String token){
        final Object result = this.userService.validateVerificationToken(token);

        if (result == null){
            return ResponseEntity.ok().body(new GenericResponse("Success"));
        }
        return  ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
    }

    @GetMapping("/resendRegistrationToken/users")
    public ResponseEntity<Void> resendRegistrationToken(@RequestParam("email") String email) {
        this.userService.generateNewVerificationToken(email, 0);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/changePassword/users")
    public ResponseEntity<GenericResponse> changePassword(@RequestParam("id") String idUser, @RequestParam("token") String token) {
        final String result = userService.validateVerificationToken(idUser, token);
        if (result == null){
            return ResponseEntity.ok().body(new GenericResponse("success"));
        }
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
    }

    @PostMapping("/savePassword/users")
    public ResponseEntity<GenericResponse> savePassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        final Object result = this.userService.validateVerificationToken(token);
        if (result != null){
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
        }
        final VerificationToken verificationToken = userService.getVerificationByToken(token);
        if (verificationToken != null){
            userService.changeUserPassworc(verificationToken.getUser(), password);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/resetPassword/users")
    public ResponseEntity<Void> resetPassword (@RequestParam("email") final String email){
        this.userService.generateNewVerificationToken(email, 1);
        return ResponseEntity.noContent().build();
    }

}
