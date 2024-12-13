package com.contenttree.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDto {
    private Long id;
    private String name;
    private String phone;
    private String location;
    private String email;
    private boolean enabled;
    private String username;
    private List<String> authorities;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private String campaign;
    private String downloads;
    private String report;
    private String blogs;
    private String whitepapers;
    private String newsletters;
}
