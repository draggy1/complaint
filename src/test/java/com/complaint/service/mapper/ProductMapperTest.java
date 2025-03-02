package com.complaint.service.mapper;

import com.complaint.service.dto.ProductDto;
import com.complaint.service.entity.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    @Test
    void shouldMapProductToProductDto() {
        // given
        Product product = new Product(1, "Laptop Pro X");

        // when
        ProductDto productDto = ProductMapper.INSTANCE.productToProductDto(product);

        // then
        assertThat(productDto).isEqualTo(new ProductDto(1, "Laptop Pro X"));
    }
}