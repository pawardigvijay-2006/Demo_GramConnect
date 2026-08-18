package com.tech_fusion.model.user;

public class User {

    String name;
    String contact;

    String email;
    String villageName;
    String role;

    public User() {
    }

    public User(String name,
            String contact,

            String email,
            String villageName,
            String role) {

        this.name = name;
        this.contact = contact;

        this.email = email;
        this.villageName = villageName;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
