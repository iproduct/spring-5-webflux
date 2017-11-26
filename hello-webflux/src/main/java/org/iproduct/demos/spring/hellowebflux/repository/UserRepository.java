package org.iproduct.demos.spring.hellowebflux.repository;

import org.iproduct.demos.spring.hellowebflux.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);
}
