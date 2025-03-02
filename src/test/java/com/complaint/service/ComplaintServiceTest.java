package com.complaint.service;

import com.complaint.controller.dto.CreateComplaintRequest;
import com.complaint.infrastructure.repository.ComplainerRepository;
import com.complaint.infrastructure.repository.ComplaintRepository;
import com.complaint.infrastructure.repository.ProductRepository;
import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.dto.ProductDto;
import com.complaint.service.entity.Complainer;
import com.complaint.service.entity.Complaint;
import com.complaint.service.entity.Product;
import com.complaint.service.exception.ComplainerNotFoundException;
import com.complaint.service.exception.ComplaintNotFoundException;
import com.complaint.service.exception.ProductNotFoundException;
import com.complaint.service.mapper.ComplaintMapper;
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
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ComplainerRepository complainerRepository;
    @Mock
    private IpLocationService ipLocationService;

    @InjectMocks
    private ComplaintService complaintService;

    private static final OffsetDateTime CREATED_AT_1 = OffsetDateTime.parse("2023-03-15T10:15:30+00:00");
    private static final OffsetDateTime CREATED_AT_2 = OffsetDateTime.parse("2023-03-16T11:20:00+00:00");

    private static final Complainer COMPLAINER = new Complainer(1, "Jack", "Strong", 0);

    private static final Complaint COMPLAINT_1 = new Complaint(
            1, new Product(1, "MP3", 0), COMPLAINER, "Some content", CREATED_AT_1, "Poland", 1, 0);

    private static final Complaint COMPLAINT_2 = new Complaint(
            2, new Product(2, "MP4", 0), COMPLAINER, "Some content2", CREATED_AT_2, "US", 2, 0
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
        when(complaintRepository.findAll()).thenThrow(new DataAccessException("Database error") {
        });

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
        when(complaintRepository.getComplaintById(1)).thenThrow(new DataAccessException("Database error") {
        });

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

    @Test
    void shouldUpdateComplaintContentSuccessfully() {
        // given
        String newContent = "Updated content";
        Complaint givenComplain = new Complaint(
                1, new Product(1, "MP3", 0), COMPLAINER, "Some Content", CREATED_AT_1, "Poland", 1, 0
        );
        ComplaintDto expectedDto = new ComplaintDto(
                1, new ProductDto(1, "MP3"), new ComplainerDto(1, "Jack", "Strong"),
                newContent, CREATED_AT_1, "Poland", 1
        );

        when(complaintRepository.findById(4)).thenReturn(Optional.of(givenComplain));

        // when
        ComplaintDto actual = complaintService.updateComplaintContent(4, newContent);

        // then
        assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    void shouldThrowComplaintNotFoundExceptionWhenUpdatingNonExistingComplaint() {
        // given
        String newContent = "Updated content";
        when(complaintRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> complaintService.updateComplaintContent(999, newContent))
                .isInstanceOf(ComplaintNotFoundException.class)
                .hasMessageContaining("Complaint not found");
    }

    @Test
    void shouldThrowExceptionWhenDatabaseFailsForUpdateComplaintContent() {
        // given
        String newContent = "Updated content";
        when(complaintRepository.findById(1)).thenThrow(new DataAccessException("Database error") {
        });

        // when & then
        assertThatThrownBy(() -> complaintService.updateComplaintContent(1, newContent))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    void shouldAddNewComplaintSuccessfully() {
        // given
        CreateComplaintRequest request = new CreateComplaintRequest(1, 1, "New complaint content");

        Product product = new Product(1, "MP3", 0);
        Complainer complainer = new Complainer(1, "Jack", "Strong", 0);
        Complaint newComplaint = new Complaint(1, product, complainer, "New complaint content", OffsetDateTime.now(), "Poland", 1, 0);
        ComplaintDto expectedDto = ComplaintMapper.INSTANCE.complaintToComplaintDto(newComplaint);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(complainerRepository.findById(1)).thenReturn(Optional.of(complainer));
        when(ipLocationService.getCountryByIp()).thenReturn("Poland");
        when(complaintRepository.findByProductAndComplainer(1, 1)).thenReturn(Optional.empty());
        when(complaintRepository.save(any(Complaint.class))).thenReturn(newComplaint);

        // when
        ComplaintDto actual = complaintService.addComplaint(request);

        // then
        assertThat(actual).isEqualTo(expectedDto);
        verify(complaintRepository).save(any(Complaint.class));
    }

    @Test
    void shouldIncreaseComplaintCountIfDuplicateComplaintExists() {
        // given
        CreateComplaintRequest request = new CreateComplaintRequest(1, 1, "Some other content");

        Product product = new Product(1, "MP3", 0);
        Complainer complainer = new Complainer(1, "Jack", "Strong", 0);
        Complaint existingComplaint = new Complaint(1, product, complainer, "Original content", OffsetDateTime.now(), "Poland", 1, 0);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(complainerRepository.findById(1)).thenReturn(Optional.of(complainer));
        when(ipLocationService.getCountryByIp()).thenReturn("Poland");
        when(complaintRepository.findByProductAndComplainer(1, 1)).thenReturn(Optional.of(existingComplaint));

        // when
        ComplaintDto result = complaintService.addComplaint(request);

        // then
        assertThat(result.complaintCount()).isEqualTo(2);
        verify(complaintRepository, never()).save(any(Complaint.class));
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        // given
        CreateComplaintRequest request = new CreateComplaintRequest(999, 1, "Some content");

        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> complaintService.addComplaint(request))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void shouldThrowComplainerNotFoundExceptionWhenComplainerDoesNotExist() {
        // given
        CreateComplaintRequest request = new CreateComplaintRequest(1, 999, "Some content");

        when(productRepository.findById(1)).thenReturn(Optional.of(new Product(1, "MP3", 0)));
        when(complainerRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> complaintService.addComplaint(request))
                .isInstanceOf(ComplainerNotFoundException.class)
                .hasMessageContaining("Complainer not found");
    }

    @Test
    void shouldThrowDataAccessExceptionWhenDatabaseFailsForAddComplaint() {
        // given
        CreateComplaintRequest request = new CreateComplaintRequest(1, 1, "Some content");

        when(productRepository.findById(1)).thenThrow(new DataAccessException("Database error") {});

        // when & then
        assertThatThrownBy(() -> complaintService.addComplaint(request))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Database error");
    }

}
