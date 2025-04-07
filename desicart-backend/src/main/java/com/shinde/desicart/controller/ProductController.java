package com.shinde.desicart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinde.desicart.dto.ProductDto;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.service.FileStorageService;
import com.shinde.desicart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductDto>> getProductsWithPagination(Pageable pageable) {
        Page<ProductDto> products = productService.getProductsWithPagination(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) throws CustomException {
        ProductDto product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String query) {
        List<ProductDto> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

//    @PostMapping
//    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
//    public ResponseEntity<ProductDto> createProduct(
//            @RequestBody ProductDto productDto,
//            @RequestParam Long userId) throws CustomException {
//        ProductDto createdProduct = productService.createProduct(productDto, userId);
//        return ResponseEntity.ok(createdProduct);
//    }
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
public ResponseEntity<ProductDto> createProduct(
        @RequestPart("productDto") String productDtoJson,
        @RequestParam Long userId,
        @RequestPart(value = "image", required = false) MultipartFile image) throws CustomException, IOException {

    // Parse JSON to DTO
    ObjectMapper objectMapper = new ObjectMapper();
    ProductDto productDto = objectMapper.readValue(productDtoJson, ProductDto.class);

    // Handle image upload
    if (image != null && !image.isEmpty()) {
        String imageUrl = fileStorageService.storeFile(image);
        productDto.setImageUrl(imageUrl);
    }

    ProductDto createdProduct = productService.createProduct(productDto, userId);
    return ResponseEntity.ok(createdProduct);
}

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDto productDto) throws CustomException {
        ProductDto updatedProduct = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws CustomException {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
