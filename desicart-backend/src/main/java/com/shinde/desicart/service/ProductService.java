package com.shinde.desicart.service;

import com.shinde.desicart.dto.ProductDto;
import com.shinde.desicart.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto, Long userId) throws CustomException;
    ProductDto updateProduct(Long productId, ProductDto productDto) throws CustomException;
    void deleteProduct(Long productId) throws CustomException;
    ProductDto getProductById(Long productId) throws CustomException;
    List<ProductDto> getAllProducts();
    List<ProductDto> getProductsByCategory(Long categoryId);
    List<ProductDto> getProductsByUser(Long userId);
    List<ProductDto> searchProducts(String query);
    Page<ProductDto> getProductsWithPagination(Pageable pageable);
}
