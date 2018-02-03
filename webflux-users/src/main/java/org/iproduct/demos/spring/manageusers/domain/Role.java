package org.iproduct.demos.spring.manageusers.domain;

public enum Role {
    CUSTOMER(1), AUTHOR(2), ADMIN(3);

    private int value;

    Role(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }



//    @Component
//    static class RoleToIntegerConverter implements Converter<Role, Integer> {
//
//        @Override
//        public Integer convert(Role role) {
//            return role.getValue();
//        }
//    }
//
//    @Component
//    static class IntegerToRoleConverter implements Converter<Integer, Role> {
//
//        public Role convert(Integer source) {
//            Role[] roles = Role.values();
//            for(int i = 0; i < roles.length; i++) {
//                if(roles[i].getValue() == source) return roles[i];
//            }
//            return null;
//        }
//    }
}
