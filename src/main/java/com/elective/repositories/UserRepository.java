package com.elective.repositories;

import com.elective.entity.Role;
import com.elective.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findById(int id);
}
