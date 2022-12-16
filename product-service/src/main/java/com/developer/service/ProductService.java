package com.developer.service;

import com.developer.dto.ProductRequest;
import com.developer.dto.ProductResponse;
import com.developer.model.Product;
import com.developer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository ;

    public void createProduct(ProductRequest productRequest)
    {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);

        log.info("product {} is saved ",product.getId());
    }

    public List<ProductResponse> getAllProducts()
    {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductresponse).toList();
    }

    private ProductResponse mapToProductresponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
