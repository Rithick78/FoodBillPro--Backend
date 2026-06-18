package com.foodbillpro.service;

import com.foodbillpro.dto.BillRequest;
import com.foodbillpro.dto.DashboardResponse;
import com.foodbillpro.entity.*;
import com.foodbillpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    private final DailySummaryRepository dailySummaryRepository;

    // Create Bill
    @Transactional
    public Bill createBill(BillRequest request) {
        List<BillItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            double subtotal = product.getPrice() * itemReq.getQuantity();

            return BillItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getPrice())
                    .subtotal(subtotal)
                    .build();
        }).collect(Collectors.toList());

        double total = items.stream().mapToDouble(BillItem::getSubtotal).sum();

        Bill bill = Bill.builder()
                .customerName(request.getCustomerName())
                .whatsappNumber(request.getWhatsappNumber())
                .totalAmount(total)
                .build();

        bill = billRepository.save(bill);

        Bill finalBill = bill;
        items.forEach(item -> item.setBill(finalBill));
        bill.setItems(items);
        bill = billRepository.save(bill);

        updateDailySummary(total);

        return bill;
    }

    // Update Daily Summary
    private void updateDailySummary(double amount) {
        LocalDate today = LocalDate.now();
        DailySummary summary = dailySummaryRepository
                .findBySummaryDate(today)
                .orElse(DailySummary.builder()
                        .summaryDate(today)
                        .totalBills(0)
                        .totalSales(0.0)
                        .build());

        summary.setTotalBills(summary.getTotalBills() + 1);
        summary.setTotalSales(summary.getTotalSales() + amount);
        dailySummaryRepository.save(summary);
    }

    // Dashboard
    public DashboardResponse getDashboard() {
        LocalDate today = LocalDate.now();
        DailySummary summary = dailySummaryRepository
                .findBySummaryDate(today)
                .orElse(DailySummary.builder()
                        .summaryDate(today)
                        .totalBills(0)
                        .totalSales(0.0)
                        .build());

        return new DashboardResponse(
                summary.getTotalBills().longValue(),
                summary.getTotalSales(),
                today.toString()
        );
    }

    // EOD Reset
    @Transactional
    public void resetDailySummary() {
        LocalDate today = LocalDate.now();
        dailySummaryRepository.findBySummaryDate(today).ifPresent(summary -> {
            summary.setTotalBills(0);
            summary.setTotalSales(0.0);
            dailySummaryRepository.save(summary);
        });
    }

    // Get Today's Bills
    public List<Bill> getTodayBills() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime end = LocalDateTime.now();
        return billRepository.findByCreatedAtBetween(start, end);
    }

    // Get All Bills Ever
    public List<Bill> getAllBills() {
        return billRepository.findAllByOrderByCreatedAtDesc();
    }

    // Delete Single Bill
    @Transactional
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}