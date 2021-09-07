package com.itranzition.alex.model.entity;


import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private String surname;


    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER,
        GUEST
    }

    public User() {
    }


    public User(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User(Long id, String email, String name, String password, String surname, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.surname = surname;
        this.role = role;
    }

    public User(String email, String name, String password, String surname, Role role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.surname = surname;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && email.equals(user.email) &&
                name.equals(user.name) && password.equals(user.password) &&
                Objects.equals(surname, user.surname);
    }

    @Override
    public int hashCode() {
        int result=0;
        int multiplier=31;
        result=result+id.hashCode()*multiplier;
        result=result*multiplier+email.hashCode();
        result=result*multiplier+name.hashCode();
        result=result*multiplier+password.hashCode();
        result=result*multiplier+surname.hashCode();
        return result;
    }
}