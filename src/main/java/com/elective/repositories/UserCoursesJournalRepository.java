package com.elective.repositories;

import com.elective.entity.Course;
import com.elective.entity.User;
import com.elective.entity.UserCoursesJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCoursesJournalRepository extends JpaRepository<UserCoursesJournal, Integer> {
    List<UserCoursesJournal> findAllByUser(User user);
    List<UserCoursesJournal> findAllByCourse(Course course);

    @Query("SELECT u FROM UserCoursesJournal u WHERE u.id = :id")
    UserCoursesJournal findById(int id);

    UserCoursesJournal findByCourseAndUser(Course course, User user);

    @Modifying
    @Query("delete from UserCoursesJournal b where b.id=:id")
    void deleteUserCoursesJournalById(@Param("id") int id);
}
