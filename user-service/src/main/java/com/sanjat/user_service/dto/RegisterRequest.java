package com.sanjat.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Email je obavezan!")
    @Email(message = "Email nije validan! Mora biti u formatu : example@example.com")
    private String email;

    @NotBlank(message = "Lozinka je obavezna!")
    @Size(min = 6, message = "Lozinka mora imati najmanje 6 karaktera")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).+$", message = "Lozinka mora imati barem jedno veliko slovo, jedan broj i jedan specijalan znak")
    private String password;

    @NotBlank(message = "Username je obavezan!")
    private String username;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
