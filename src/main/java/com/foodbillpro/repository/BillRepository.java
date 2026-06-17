package com.foodbillpro.repository;

import com.foodbillpro.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // correct method name for date range
    List<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // all bills latest first
    List<Bill> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.createdAt BETWEEN :start AND :end")
    Long countByDateRange(@Param("start") LocalDateTime start,
                          @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Bill b WHERE b.createdAt BETWEEN :start AND :end")
    Double sumTotalByDateRange(@Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);
}