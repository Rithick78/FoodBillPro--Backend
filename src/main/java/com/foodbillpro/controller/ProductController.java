package com.foodbillpro.controller;

import com.foodbillpro.entity.Product;
import com.foodbillpro.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ── Public ──
    @GetMapping("/api/products/public/all")
    public ResponseEntity<List<Product>> getPublicProducts() {
        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    @GetMapping("/api/products/public/menu")
    public ResponseEntity<List<Product>> getMenuProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/api/products/public/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(
                productService.getByCategory(
                        Product.Category.valueOf(category.toUpperCase())));
    }

    // ── Admin ──
    @GetMapping("/api/admin/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/api/admin/products")
    public ResponseEntity<Product> addProduct(
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam Double price,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "VEG") String vegType,
            @RequestParam(defaultValue = "false") Boolean isSignature,
            @RequestParam(defaultValue = "false") Boolean isFastMoving,
            @RequestParam(required = false) MultipartFile image) throws IOException {

        return ResponseEntity.ok(
                productService.addProduct(
                        name, category, price, description,
                        vegType, isSignature, isFastMoving, image));
    }

    @PutMapping("/api/admin/products/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam Double price,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "VEG") String vegType,
            @RequestParam(defaultValue = "false") Boolean isSignature,
            @RequestParam(defaultValue = "false") Boolean isFastMoving,
            @RequestParam(required = false) MultipartFile image) throws IOException {

        return ResponseEntity.ok(
                productService.updateProduct(
                        id, name, category, price, description,
                        vegType, isSignature, isFastMoving, image));
    }

    @DeleteMapping("/api/admin/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Deleted");
    }

    @PatchMapping("/api/admin/products/{id}/toggle")
    public ResponseEntity<Product> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(productService.toggleAvailability(id));
    }

    @PatchMapping("/api/admin/products/{id}/signature")
    public ResponseEntity<Product> toggleSignature(@PathVariable Long id) {
        return ResponseEntity.ok(productService.toggleSignature(id));
    }

    @PatchMapping("/api/admin/products/{id}/fastmoving")
    public ResponseEntity<Product> toggleFastMoving(@PathVariable Long id) {
        return ResponseEntity.ok(productService.toggleFastMoving(id));
    }
}