package com.contenttree.CountryCode;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country_code")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "country_name")
    private String name;

    @Column(name = "country_code")
    private String code;

}
