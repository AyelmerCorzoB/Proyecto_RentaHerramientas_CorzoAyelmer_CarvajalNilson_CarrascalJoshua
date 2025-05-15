package com.alkileapp.alkile_app.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_preferences", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "customer_id", "preference" })
})
public class CustomerPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, length = 100)
    private String preference;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

}