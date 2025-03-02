package com.complaint.service.mapper;

import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.dto.ProductDto;
import com.complaint.service.entity.Complainer;
import com.complaint.service.entity.Complaint;
import com.complaint.service.entity.Product;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ComplaintMapperTest {

    @Test
    void shouldMapComplaintToComplaintDto() {
        // given
        Complainer complainer = new Complainer(1, "Jack", "Strong");
        Product product = new Product(1, "MP3");
        OffsetDateTime createdAt = OffsetDateTime.parse("2023-03-15T10:15:30+00:00");
        Complaint complaint = new Complaint(1, product, complainer, "Some content", createdAt, "Poland", 1, 0);

        // when
        ComplaintDto complaintDto = ComplaintMapper.INSTANCE.complaintToComplaintDto(complaint);

        // then
        assertThat(complaintDto).isEqualTo(
                new ComplaintDto(
                        1,
                        new ProductDto(1, "MP3"),
                        new ComplainerDto(1, "Jack", "Strong"),
                        "Some content",
                        createdAt,
                        "Poland",
                        1
                )
        );
    }
}