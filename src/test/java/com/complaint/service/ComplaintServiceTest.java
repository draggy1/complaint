package com.complaint.service;

import com.complaint.infrastructure.repository.ComplaintRepository;
import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.dto.ProductDto;
import com.complaint.service.entity.Complainer;
import com.complaint.service.entity.Complaint;
import com.complaint.service.entity.Product;
import com.complaint.service.exception.ComplaintNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintService complaintService;

    private static final OffsetDateTime CREATED_AT_1 = OffsetDateTime.parse("2023-03-15T10:15:30+00:00");
    private static final OffsetDateTime CREATED_AT_2 = OffsetDateTime.parse("2023-03-16T11:20:00+00:00");

    private static final Complainer COMPLAINER = new Complainer(1, "Jack", "Strong");

    private static final Complaint COMPLAINT_1 = new Complaint(
            1, new Product(1, "MP3"), COMPLAINER, "Some content", CREATED_AT_1, "Poland", 1
    );

    private static final Complaint COMPLAINT_2 = new Complaint(
            2, new Product(2, "MP4"), COMPLAINER, "Some content2", CREATED_AT_2, "US", 2
    );

    private static final ComplaintDto COMPLAINT_DTO_1 = new ComplaintDto(
            1, new ProductDto(1, "MP3"), new ComplainerDto(1, "Jack", "Strong"),
            "Some content", CREATED_AT_1, "Poland", 1
    );

    private static final ComplaintDto COMPLAINT_DTO_2 = new ComplaintDto(
            2, new ProductDto(2, "MP4"), new ComplainerDto(1, "Jack", "Strong"),
            "Some content2", CREATED_AT_2, "US", 2
    );

    @Test
    void shouldGetAllComplaintsSuccessfully() {
        // given
        when(complaintRepository.findAll()).thenReturn(List.of(COMPLAINT_1, COMPLAINT_2));

        // when
        List<ComplaintDto> actual = complaintService.getAllComplaints();

        // then
        assertThat(actual).containsExactlyInAnyOrder(COMPLAINT_DTO_1, COMPLAINT_DTO_2);
    }

    @Test
    void shouldReturnEmptyListWhenNoComplaintsExist() {
        // given
        when(complaintRepository.findAll()).thenReturn(List.of());

        // when
        List<ComplaintDto> result = complaintService.getAllComplaints();

        // then
        assertThat(result).isEmpty();
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

    @Test
    void shouldGetComplaintByIdSuccessfully() {
        // given
        when(complaintRepository.getComplaintById(1)).thenReturn(Optional.of(COMPLAINT_1));

        // when
        ComplaintDto actual = complaintService.getComplaintById(1);

        // then
        assertThat(actual).isEqualTo(COMPLAINT_DTO_1);
    }

    @Test
    void shouldThrowComplaintNotFoundExceptionWhenComplaintDoesNotExist() {
        // given
        when(complaintRepository.getComplaintById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> complaintService.getComplaintById(999))
                .isInstanceOf(ComplaintNotFoundException.class)
                .hasMessageContaining("Complaint not found");
    }

    @Test
    void shouldThrowExceptionWhenDatabaseFailsForGetComplaintById() {
        // given
        when(complaintRepository.getComplaintById(1)).thenThrow(new DataAccessException("Database error") {});

        // when & then
        assertThatThrownBy(() -> complaintService.getComplaintById(1))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    void shouldFetchComplaintByIdWithSingleQuery() {
        // given
        when(complaintRepository.getComplaintById(1)).thenReturn(Optional.of(COMPLAINT_1));

        // when
        complaintService.getComplaintById(1);

        // then
        verify(complaintRepository, times(1)).getComplaintById(1);
    }
}