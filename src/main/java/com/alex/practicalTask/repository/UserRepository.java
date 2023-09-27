package com.alex.practicalTask.repository;

import com.alex.practicalTask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.dateOfBirth between :fromDate and :toDate")
    List<User> findUsersByDateOfBirth(@Param("fromDate") LocalDate fromDate,
                                      @Param("toDate") LocalDate toDate);
}
