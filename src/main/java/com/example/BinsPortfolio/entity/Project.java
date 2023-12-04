package com.example.BinsPortfolio.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length=27)
    private String title;

    @Column(length=516)
    private String introduction;

    @Column(length=516)
    private String skills;

    @Column(length=50)
    private String link;

    private Date startDate;

    private String endDate;
    private String path;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Image image;

}