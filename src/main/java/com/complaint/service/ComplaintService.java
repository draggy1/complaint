package com.complaint.service;

import com.complaint.infrastructure.repository.ComplaintRepository;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.exception.ComplaintNotFoundException;
import com.complaint.service.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public List<ComplaintDto> getAllComplaints() {
        return complaintRepository.findAll()
                .stream()
                .map(ComplaintMapper.INSTANCE::complaintToComplaintDto)
                .toList();
    }

    public ComplaintDto getComplaintById(int complaintId) {
        return complaintRepository.getComplaintById(complaintId)
                .map(ComplaintMapper.INSTANCE::complaintToComplaintDto)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found"));
    }
}
