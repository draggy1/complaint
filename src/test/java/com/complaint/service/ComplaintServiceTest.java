package com.complaint.service;

import com.complaint.infrastructure.repository.ComplaintRepository;
import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.dto.ProductDto;
import com.complaint.common.Result;
import com.complaint.service.entity.Complainer;
import com.complaint.service.entity.Complaint;
import com.complaint.service.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    void shouldGetAllComplaintsSuccessfully() {
        //given

        OffsetDateTime createdAt1 = OffsetDateTime.parse("2023-03-15T10:15:30+00:00");
        OffsetDateTime createdAt2 = OffsetDateTime.parse("2023-03-16T11:20:00+00:00");
        Complainer complainer = new Complainer(1, "Jack", "Strong");
        when(complaintRepository.findAll()).thenReturn(
                List.of(
                        new Complaint(1, new Product(1, "MP3"), complainer, "Some content", createdAt1, "Poland", 1),
                        new Complaint(2, new Product(2, "MP4"), complainer, "Some content2", createdAt2, "US", 2)
                )
        );

        //when
        Result<List<ComplaintDto>> actual = complaintService.getAllComplaints();

        //then
        assertThat(actual.statusCode()).isEqualTo(200);
        assertThat(actual.data()).containsExactlyInAnyOrder(
                new ComplaintDto(1, new ProductDto(1, "MP3"), new ComplainerDto(1, "Jack", "Strong"), "Some content", createdAt1, "Poland", 1),
                new ComplaintDto(2, new ProductDto(2, "MP4"), new ComplainerDto(1, "Jack", "Strong"), "Some content2", createdAt2, "US", 2)
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoComplaintsExist() {
        // given
        when(complaintRepository.findAll()).thenReturn(List.of());

        // when
        Result<List<ComplaintDto>> result = complaintService.getAllComplaints();

        // then
        assertThat(result.statusCode()).isEqualTo(200);
        assertThat(result.data()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenRepositoryFails() {
        // given
        when(complaintRepository.findAll()).thenThrow(new DataAccessException("Database error") {});

        // when & then
        assertThatThrownBy(() -> complaintService.getAllComplaints())
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    void shouldFetchAllComplaintsWithSingleQuery() {
        // when
        complaintService.getAllComplaints();

        // then
        verify(complaintRepository, times(1)).findAll();
    }
}