package com.foodbillpro.dto;

import lombok.Data;
import java.util.List;

@Data
public class BillRequest {
    private String customerName;
    private String whatsappNumber;
    private List<BillItemRequest> items;

    @Data
    public static class BillItemRequest {
        private Long productId;
        private Integer quantity;
    }
}