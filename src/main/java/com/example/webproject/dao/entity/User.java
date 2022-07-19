package com.example.webproject.dao.entity;

import java.util.Objects;

public class User extends BaseEntity{
    private String email;
    private UserRole userRole;
    private boolean isDeleted;
    private String phone;

    public User(int id,String email, UserRole userRole, boolean isDeleted, String phone) {
        super(id);
        this.email = email;
        this.userRole = userRole;
        this.isDeleted = isDeleted;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isDeleted == user.isDeleted && Objects.equals(email, user.email) && userRole == user.userRole && Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, userRole, isDeleted, phone);
    }

    public enum UserRole{
        CUSTOMER,
        EMPLOYEE,
        ADMINISTRATOR,
        GUEST
    }

    public static UserBuilder getBuilder(){
        return new UserBuilder();
    }

    public static class UserBuilder{
        private int builtId=UNDEFINED_ID;
        private String builtEmail;
        private UserRole builtUserRole;
        private boolean builtIsDeleted;
        private String builtPhone;

        public UserBuilder id(int id){
            builtId=id;
            return this;
        }

        public UserBuilder email(String email){
            builtEmail=email;
            return this;
        }

        public UserBuilder userRole(UserRole userRole){
            builtUserRole=userRole;
            return this;
        }
        public UserBuilder isDeleted(boolean isDeleted){
            builtIsDeleted=isDeleted;
            return this;
        }


        public UserBuilder phone(String phone){
            builtPhone=phone;
            return this;
        }


        public User build(){
            return new User(builtId,builtEmail,builtUserRole,
                    builtIsDeleted,builtPhone);
        }
    }
}
