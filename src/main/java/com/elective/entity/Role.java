package com.elective.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_role")
    private int idRole;

    @Column(name="role_name")
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
