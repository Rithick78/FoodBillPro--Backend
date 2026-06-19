package com.foodbillpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponse {
    private Long totalBills;
    private Double totalSales;
    private String date;
}