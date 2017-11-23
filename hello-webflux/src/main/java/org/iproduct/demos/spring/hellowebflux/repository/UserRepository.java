package org.iproduct.demos.spring.hellowebflux.repository;

import org.iproduct.demos.spring.hellowebflux.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String username);
}
