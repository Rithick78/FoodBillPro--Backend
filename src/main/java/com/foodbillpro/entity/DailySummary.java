package com.foodbillpro.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "summary_date", unique = true)
    private LocalDate summaryDate;

    @Column(name = "total_bills")
    private Integer totalBills = 0;

    @Column(name = "total_sales")
    private Double totalSales = 0.0;
}