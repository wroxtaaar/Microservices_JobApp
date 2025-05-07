package com.embarkx.companyms.company;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

//    @JsonIgnore
//    @OneToMany(mappedBy = "company") //(mappedBy = "company") removed the x table that contained only ids
//    private List<Job> jobs;          //company_id and job_id, now both are connected without a mediator
//
//    @OneToMany(mappedBy = "company")
//    private List<Review> reviews;


}
