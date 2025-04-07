package com.shinde.desicart.dto;

import lombok.*;

import java.util.Set;


public class UserDto {
    // Getters and Setters
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
