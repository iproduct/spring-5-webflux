package org.iproduct.demos.spring.hellowebflux.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

//import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
public class User {

    @Id
    private String id;

    @NotNull
    @Length(min = 3, max = 30)
    private String email;

    @NotNull
    @Length(min = 5, max = 30)
    private String password;

    @NotNull
    @Length(min = 1, max = 30)
    private String fname;

    @NotNull
    @Length(min = 1, max = 30)
    private String lname;

    @NotNull
    @Length(min = 1, max = 30)
    private int role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
