package com.complaint.service;

import com.complaint.infrastructure.repository.ComplaintRepository;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.common.Result;
import com.complaint.service.entity.Complaint;
import com.complaint.service.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public Result<List<ComplaintDto>> getAllComplaints() {
        List<Complaint> complaints = complaintRepository.findAll();
        return new Result<>(
                HttpStatus.OK.value(),
                mapToDto(complaints)
        );
    }

    private List<ComplaintDto> mapToDto(List<Complaint> complaints) {
        return complaints.stream()
                .map(ComplaintMapper.INSTANCE::complaintToComplaintDto)
                .toList();
    }
}
