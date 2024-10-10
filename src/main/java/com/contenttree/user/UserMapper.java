package com.contenttree.user;


public class UserMapper {
    public static UserDto toUserDto(String email,String name){
        if (email==null&&name==null){
            return null;
        }
        return new UserDto(email,name);
    }
}
