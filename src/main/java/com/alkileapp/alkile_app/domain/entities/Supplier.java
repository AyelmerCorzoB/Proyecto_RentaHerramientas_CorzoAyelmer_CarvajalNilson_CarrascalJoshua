package com.alkileapp.alkile_app.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    private Audit audit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    
}