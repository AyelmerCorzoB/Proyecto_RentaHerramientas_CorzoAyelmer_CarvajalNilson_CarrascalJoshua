package com.alkileapp.alkile_app.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import jakarta.validation.constraints.FutureOrPresent;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User customer;

    @ManyToOne
    @JoinColumn(name = "tool_id", nullable = false)
    @JsonBackReference("tool-reservations")
    private Tool tool;

    @Column(name = "start_date", nullable = false)
    @FutureOrPresent
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @FutureOrPresent
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "creation_date", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private DamageReport damageReport;

    public enum ReservationStatus {
        PENDING, APPROVED, REJECTED, COMPLETED, CANCELED
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    @Embedded
    private Audit audit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public DamageReport getDamageReport() {
        return damageReport;
    }

    public void setDamageReport(DamageReport damageReport) {
        this.damageReport = damageReport;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

}