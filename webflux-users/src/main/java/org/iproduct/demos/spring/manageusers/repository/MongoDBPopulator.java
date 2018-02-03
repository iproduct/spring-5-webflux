package org.iproduct.demos.spring.manageusers.repository;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.demos.spring.manageusers.domain.Role;
import org.iproduct.demos.spring.manageusers.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongoDBPopulator implements CommandLineRunner {
    private static final User defaultAdmin = new User("", "admin",
            "{bcrypt}$2a$10$y3WZ2gUVsYkgTurMIIDQueSKtEI3nQtUl6Y4VH3vk0izY8gFcfexe",
            "Default", "Admin", Role.ADMIN, true);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.count()
                .doOnNext(count -> {
                    log.info("MongoDB started successfully. Users count: " + count);
                    if (count == 0) createDefaultAdminUser();
                })
                .doOnError(error ->
                        log.warn("Can't connect to MongoDB. Did you forget to start it with 'mongod --dbpath=<path-to-mongo-data-directory>'?"))
                .subscribe();
    }

    /* Pre-populate MongoDB with default admin user */
    private void createDefaultAdminUser() {
        userRepository.save(defaultAdmin)
                .subscribe(user -> log.info("Users not found - adding default Admin user: " + user));
    }

}
