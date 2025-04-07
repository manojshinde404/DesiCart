package com.shinde.desicart.service.impl;

import com.shinde.desicart.model.Category;
import com.shinde.desicart.model.Product;
import com.shinde.desicart.model.User;
import com.shinde.desicart.dto.ProductDto;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.repository.CategoryRepository;
import com.shinde.desicart.repository.ProductRepository;
import com.shinde.desicart.repository.UserRepository;
import com.shinde.desicart.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

//    @Override
//    public ProductDto createProduct(ProductDto productDto, Long userId) throws CustomException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException("User not found with id: " + userId));
//
//        Category category = categoryRepository.findById(productDto.getCategoryId())
//                .orElseThrow(() -> new CustomException("Category not found with id: " + productDto.getCategoryId()));
//
//        Product product = modelMapper.map(productDto, Product.class);
//        product.setUser(user);
//        product.setCategory(category);
//
//        Product savedProduct = productRepository.save(product);
//        return modelMapper.map(savedProduct, ProductDto.class);
//    }
@Override
public ProductDto createProduct(ProductDto productDto, Long userId) throws CustomException {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException("User not found with id: " + userId));

    Category category = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new CustomException("Category not found with id: " + productDto.getCategoryId()));

    Product product = modelMapper.map(productDto, Product.class);
    product.setUser(user);
    product.setCategory(category);

    // Set image URL if exists
    if (productDto.getImageUrl() != null) {
        product.setImageUrl(productDto.getImageUrl());
    }

    Product savedProduct = productRepository.save(product);
    return modelMapper.map(savedProduct, ProductDto.class);
}

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) throws CustomException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found with id: " + productId));

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new CustomException("Category not found with id: " + productDto.getCategoryId()));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long productId) throws CustomException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found with id: " + productId));
        productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(Long productId) throws CustomException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found with id: " + productId));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByUser(Long userId) {
        return productRepository.findByUserId(userId).stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProducts(String query) {
        return productRepository.searchProducts(query).stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> getProductsWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductDto.class));
    }
}
