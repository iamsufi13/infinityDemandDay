package com.contenttree.userdatastorage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserDataStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;

    private long user_id;
    private String ip;
    private String city;
    private String country;
    private String region;
    private String org;
    private String postal;
    private String timezone;
    private String location;
}
