package com.elective.repositories;

import com.elective.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {

    Role getRolesByIdRole(int id);

    List<Role> findAll();
}
