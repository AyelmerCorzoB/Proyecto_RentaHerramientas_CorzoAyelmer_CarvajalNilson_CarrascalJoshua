package com.alkileapp.alkile_app.domain.entities;

import java.time.LocalDateTime;
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
@Table(name = "tools")
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255, columnDefinition = "TEXT")
    private String description;

    @Column(name = "daily_cost", columnDefinition = "NUMERIC(10, 2)", nullable = false)
    private double dailyCost;

    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private Integer stock = 1;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "creation_date", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean available = true;

    @OneToMany(mappedBy = "tool")
    private List<Reservation> reservations;
@Embedded
    Audit audit = new Audit();
}
