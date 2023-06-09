package com.portfolio.mourad.repository;

import java.util.Optional;

import com.portfolio.mourad.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portfolio.mourad.models.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> getUserById(Integer userId);

  @Modifying
  @Query("UPDATE User u SET u.password = :newPassword WHERE u.username = :username")
  int updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);
}
