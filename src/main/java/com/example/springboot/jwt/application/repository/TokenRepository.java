package com.example.springboot.jwt.application.repository;

import com.example.springboot.jwt.application.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author Prashant Patel
 * Date: 7/22/2020
 **/
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUsernameAndToken(String username, String token);

    Token saveAndFlush(Token e);

    @Modifying
    @Transactional
    @Query("DELETE from Token t where t.username = :user")
    void deleteByUser(String user);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.token = :token, t.expiryTime = :expiryTime where LOWER(t.username) = LOWER(:user)")
    void refreshToken(@Param("user") String user, @Param("token") String token, @Param("expiryTime") Date expiryTime);

}
