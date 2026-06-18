package com.foodbillpro.service;

import com.foodbillpro.entity.Product;
import com.foodbillpro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailable(true);
    }

    public List<Product> getByCategory(Product.Category category) {
        return productRepository.findByCategoryAndAvailable(category, true);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    //Convert image to Base64 string and store in DB
    private String convertToBase64(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        byte[] bytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        String mimeType = file.getContentType();
        // Return as data URL so frontend can use directly as img
        return "data:" + mimeType + ";base64," + base64;
    }

    public Product addProduct(String name, String category, Double price,
                              String description, String vegType,
                              Boolean isSignature, Boolean isFastMoving,
                              MultipartFile image) throws IOException {

        return productRepository.save(Product.builder()
                .name(name)
                .category(Product.Category.valueOf(category.toUpperCase()))
                .price(price)
                .description(description)
                .vegType(Product.VegType.valueOf(vegType.toUpperCase()))
                .imageData(convertToBase64(image))
                .available(true)
                .isSignature(isSignature != null && isSignature)
                .isFastMoving(isFastMoving != null && isFastMoving)
                .build());
    }

    public Product updateProduct(Long id, String name, String category,
                                 Double price, String description,
                                 String vegType, Boolean isSignature,
                                 Boolean isFastMoving,
                                 MultipartFile image) throws IOException {

        Product product = getById(id);

        product.setName(name);
        product.setCategory(Product.Category.valueOf(category.toUpperCase()));
        product.setPrice(price);
        product.setDescription(description);
        product.setVegType(Product.VegType.valueOf(vegType.toUpperCase()));
        product.setIsSignature(isSignature != null && isSignature);
        product.setIsFastMoving(isFastMoving != null && isFastMoving);

        // Only update image if new one is uploaded
        if (image != null && !image.isEmpty()) {
            product.setImageData(convertToBase64(image));
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getById(id);
        productRepository.delete(product);
    }

    public Product toggleAvailability(Long id) {
        Product product = getById(id);
        product.setAvailable(!product.getAvailable());
        return productRepository.save(product);
    }

    public Product toggleSignature(Long id) {
        Product product = getById(id);
        boolean current = product.getIsSignature() != null && product.getIsSignature();
        product.setIsSignature(!current);
        return productRepository.save(product);
    }

    public Product toggleFastMoving(Long id) {
        Product product = getById(id);
        boolean current = product.getIsFastMoving() != null && product.getIsFastMoving();
        product.setIsFastMoving(!current);
        return productRepository.save(product);
    }
}