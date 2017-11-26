package org.iproduct.demos.spring.hellowebflux.repository;

import org.iproduct.demos.spring.hellowebflux.domain.Role;
import org.iproduct.demos.spring.hellowebflux.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MongoDBPopulator implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    /* Pre-populate MongoDB with default admin user */
    public void run(String... args) throws Exception {
        final User defaultAdmin = new User("", "admin",
                "{bcrypt}$2a$10$y3WZ2gUVsYkgTurMIIDQueSKtEI3nQtUl6Y4VH3vk0izY8gFcfexe",
                "Default", "Admin", Role.ADMIN, true);
        userRepository.save(defaultAdmin);
    }

}
