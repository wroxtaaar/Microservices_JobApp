package com.embarkx.companyms.company;


import jakarta.persistence.*;
import lombok.Data;


@Data @Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double rating;

//    @JsonIgnore
//    @OneToMany(mappedBy = "company") //(mappedBy = "company") removed the x table that contained only ids
//    private List<Job> jobs;          //company_id and job_id, now both are connected without a mediator
//
//    @OneToMany(mappedBy = "company")
//    private List<Review> reviews;


}
