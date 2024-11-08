package com.contenttree.CampaignManager;

import com.contenttree.admin.Admin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Webinar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String webinarName;

    private String webinarSubject;

    private List<String> webinarSpeakers;

    private String liveLink;

    @ManyToOne
    private Admin admin;
    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }



}
