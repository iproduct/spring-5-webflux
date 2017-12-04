package org.iproduct.demos.spring.hellowebflux.converters;

import org.iproduct.demos.spring.hellowebflux.domain.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class RoleToIntegerConverter implements Converter<Role, Integer> {

    @Override
    public Integer convert(Role role) {
        return role.getValue();
    }
}

