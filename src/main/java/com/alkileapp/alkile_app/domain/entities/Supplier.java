package com.alkileapp.alkile_app.domain.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "tax_id", length = 13)
    private String taxId;

    @Column(length = 100)
    private String company;

    @Column(columnDefinition = "NUMERIC(3, 2) DEFAULT 0.0")
    private Double rating;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

@Embedded
    Audit audit = new Audit();
   
}