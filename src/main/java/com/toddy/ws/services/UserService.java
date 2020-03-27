package com.toddy.ws.services;

import com.toddy.ws.model.Role;
import com.toddy.ws.model.User;
import com.toddy.ws.dto.UserDTO;
import com.toddy.ws.model.VerificationToken;
import com.toddy.ws.repository.RoleRepository;
import com.toddy.ws.repository.UserRepository;
import com.toddy.ws.repository.VerificationTokenRepository;
import com.toddy.ws.services.email.EmailService;
import com.toddy.ws.services.exception.ObjectAlreadyExistsException;
import com.toddy.ws.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User fetchUserById(String id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public User insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User fromDTO(UserDTO userDTO){
        return new User(userDTO);
    }

    public User updateUser(User user){
        Optional<User> updateUser = userRepository.findById(user.getId());
        return updateUser.map(u -> userRepository.save(new User(u.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), u.getRoles(), u.getPassword(), u.getEnabled()))).
                orElseThrow(() -> new ObjectNotFoundException("Usuário não existe"));
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public User registerUser(User user){
        if (emailExist(user.getEmail())){
            throw new ObjectAlreadyExistsException("Já existe uma conta com esse endereço de email");
        }
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
        user.setEnabled(false);
        user = insertUser(user);
        this.emailService.sendConfirmationHtmlEmail(user,null);
        return user;
    }

    public void createVerificationTokenForUser(User user, String token){
        final VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }

    private boolean emailExist(final String email){
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            return true;
        }
        return false;
    }

    public String validateVerificationToken(String token){
        final  Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (!verificationToken.isPresent()){
            return "Invalid token";
        }
        final User user = verificationToken.get().getUser();
        final Calendar calendar = Calendar.getInstance();
        if ((verificationToken.get().getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0){
            return "Expired token";
        }
        user.setEnabled(true);
        this.userRepository.save(user);
        return null;
    }

    public User findByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }

    public VerificationToken generateNewVerificationToken(String email) {
        User user = this.findByEmail(email);
        Optional<VerificationToken> vToken = verificationTokenRepository.findByUser(user);
        vToken.get().updateToken(UUID.randomUUID().toString());
        VerificationToken updateVToken = verificationTokenRepository.save(vToken.get());
        emailService.sendConfirmationHtmlEmail(user, updateVToken);
        return updateVToken;
    }

}
