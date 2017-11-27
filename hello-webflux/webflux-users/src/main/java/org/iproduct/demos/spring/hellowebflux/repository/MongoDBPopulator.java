package org.iproduct.demos.spring.hellowebflux.repository;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.demos.spring.hellowebflux.domain.Role;
import org.iproduct.demos.spring.hellowebflux.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongoDBPopulator implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    /* Pre-populate MongoDB with default admin user */
    public void run(String... args) throws Exception {
        final User defaultAdmin = new User("", "admin",
                "{bcrypt}$2a$10$y3WZ2gUVsYkgTurMIIDQueSKtEI3nQtUl6Y4VH3vk0izY8gFcfexe",
                "Default", "Admin", Role.ADMIN, true);

        userRepository.count()
                .filter(count -> count == 0)
                .doOnNext(count -> userRepository.save(defaultAdmin)
                        .subscribe(user -> log.info("Users not found - adding default Admin user: " + user))
                ).subscribe();
    }

}
