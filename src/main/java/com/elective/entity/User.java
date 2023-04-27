package com.elective.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name ="email")
    private String email;
    @Column(name ="password")
    private String password;
    @Column(name ="first_name")
    private String firstName;
    @Column(name ="second_name")
    private String secondName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();
    @Column(name ="is_blocked")
    private boolean isBlocked;

    public User(String email, String password, String firstName, String secondName, boolean blocked, List<Role> role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.isBlocked = blocked;
        this.roles = role;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
