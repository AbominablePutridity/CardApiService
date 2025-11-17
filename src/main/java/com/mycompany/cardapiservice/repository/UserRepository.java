package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("SELECT u from User u WHERE u.login = :userLogin")
    public Optional<User> findByLogin(@Param("userLogin") String userLogin);
}
