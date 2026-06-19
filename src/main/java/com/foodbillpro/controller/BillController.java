package com.foodbillpro.controller;

import com.foodbillpro.dto.BillRequest;
import com.foodbillpro.dto.DashboardResponse;
import com.foodbillpro.entity.Bill;
import com.foodbillpro.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/bills")
    public ResponseEntity<Bill> createBill(@RequestBody BillRequest request) {
        return ResponseEntity.ok(billService.createBill(request));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(billService.getDashboard());
    }

    @PostMapping("/dashboard/reset")
    public ResponseEntity<?> resetDashboard() {
        billService.resetDailySummary();
        return ResponseEntity.ok("Reset successful");
    }

    @GetMapping("/bills/today")
    public ResponseEntity<List<Bill>> getTodayBills() {
        return ResponseEntity.ok(billService.getTodayBills());
    }

    // get all bills
    @GetMapping("/bills/all")
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    // delete single bill
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted");
    }
}