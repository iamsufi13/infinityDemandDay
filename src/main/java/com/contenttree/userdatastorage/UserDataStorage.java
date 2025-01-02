package com.contenttree.userdatastorage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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
    private long solutionSetId;
    private long categoryId;
    private int download;
    private int view;
    private int save;


    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }

}
