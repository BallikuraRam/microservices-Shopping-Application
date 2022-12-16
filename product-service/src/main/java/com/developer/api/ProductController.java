package com.developer.api;

import com.developer.dto.ProductRequest;
import com.developer.dto.ProductResponse;
import com.developer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService ;

    @RequestMapping
    @ResponseStatus(HttpStatus.CREATED)
    public  void createProduct(@RequestBody ProductRequest productRequest)
    {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllproducts()
    {
        return productService.getAllProducts();
    }
}
