package com.contenttree.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String name;

    public UserDto(UserDto user) {
        this.email= user.getEmail();
        this.name= user.getName();
    }
}
