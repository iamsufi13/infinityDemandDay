package com.contenttree.Jwt;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String jwtToken;

    private String username;

    private Long id;
}
