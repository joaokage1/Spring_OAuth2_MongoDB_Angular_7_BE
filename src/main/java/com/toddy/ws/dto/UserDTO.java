package com.toddy.ws.dto;

import com.toddy.ws.model.Role;
import com.toddy.ws.model.User;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String firstName;
    private String lastName;
    private String email;

    private List<Role> roles;

    // Authentication
    private String password;
    private Boolean enabled;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.enabled = user.getEnabled();
        this.roles = user.getRoles();
        this.password = user.getPassword();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
