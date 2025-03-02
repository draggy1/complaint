package com.complaint.service;

import com.complaint.controller.dto.CreateComplaintRequest;
import com.complaint.infrastructure.repository.ComplainerRepository;
import com.complaint.infrastructure.repository.ComplaintRepository;
import com.complaint.infrastructure.repository.ProductRepository;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.entity.Complainer;
import com.complaint.service.entity.Complaint;
import com.complaint.service.entity.Product;
import com.complaint.service.exception.ComplainerNotFoundException;
import com.complaint.service.exception.ComplaintNotFoundException;
import com.complaint.service.exception.ProductNotFoundException;
import com.complaint.service.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ProductRepository productRepository;
    private final ComplainerRepository complainerRepository;
    private final IpLocationService ipLocationService;

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

    @Transactional
    public ComplaintDto updateComplaintContent(int complaintId, String newContent) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found"));

        complaint.setContent(newContent);
        return ComplaintMapper.INSTANCE.complaintToComplaintDto(complaint);
    }

    @Transactional
    public ComplaintDto addComplaint(CreateComplaintRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Complainer complainer = complainerRepository.findById(request.complainerId())
                .orElseThrow(() -> new ComplainerNotFoundException("Complainer not found"));
        
        String country = ipLocationService.getCountryByIp();

        Complaint complaint = complaintRepository.findByProductAndComplainer(request.productId(), request.complainerId())
                .map(existingComplaint -> {
                    existingComplaint.setComplaintCount(existingComplaint.getComplaintCount() + 1);
                    return existingComplaint;
                })
                .orElseGet(() -> {
                    Complaint newComplaint = getComplaint(product, complainer, country, request.content());
                    return complaintRepository.save(newComplaint);
                });
        return ComplaintMapper.INSTANCE.complaintToComplaintDto(complaint);
    }

    private static Complaint getComplaint(Product product, Complainer complainer, String country, String content) {
        return Complaint.builder()
                .product(product)
                .complainer(complainer)
                .content(content)
                .createdAt(OffsetDateTime.now())
                .complaintCountry(country)
                .complaintCount(1)
                .build();
    }
}
