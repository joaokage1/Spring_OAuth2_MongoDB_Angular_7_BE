package com.toddy.ws.repository;

import com.toddy.ws.model.User;
import com.toddy.ws.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

    Optional <VerificationToken> findByToken(String token);
    Optional <VerificationToken> findByUser(User user);
}
