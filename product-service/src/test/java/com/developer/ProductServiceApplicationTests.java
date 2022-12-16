package com.developer;

import com.developer.dto.ProductRequest;
import com.developer.dto.ProductResponse;
import com.developer.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	// for docker image
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc ;

	@Autowired
	private ObjectMapper objectMapper ;

	@Autowired
	private ProductRepository productRepository ;

	// for mongodb connection not using application.properties file
	@DynamicPropertySource
	static void  setProperties(DynamicPropertyRegistry dynamicPropertyRegistry)
	{
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iphone 11")
				.description("iphone 11")
				.price(BigDecimal.valueOf(1200))
				.build();
	}
//	@Test
//	void shouldGetALlProducts() throws Exception {
//		ProductResponse productResponse = getAllProductRespose();
//		String productResponseString = objectMapper.writeValueAsString(productResponse);
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/products")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(productResponseString))
//				.andExpect(status().isCreated());
//		Assertions.assertEquals(1, productRepository.count());
//	}
//
//	private ProductResponse getAllProductRespose() {
//		return ProductResponse.builder()
//				.id("")
//				.name("")
//				.description("")
//				.price(BigDecimal.valueOf(1200))
//				.build();
//	}

}
