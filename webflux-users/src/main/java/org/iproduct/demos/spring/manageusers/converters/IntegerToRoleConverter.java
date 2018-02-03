package org.iproduct.demos.spring.manageusers.converters;

import org.iproduct.demos.spring.manageusers.domain.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IntegerToRoleConverter implements Converter<Integer, Role> {

    public Role convert(Integer source) {
        for (Role role : Role.values()) {
            if (role.getValue() == source)
                return role;
        }
        return null;
    }
}