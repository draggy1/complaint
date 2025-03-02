package com.complaint.controller;

import com.complaint.service.ComplaintService;
import com.complaint.service.dto.ComplaintDto;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/complaint", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComplaintController {

    private final ComplaintService complaintService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ComplaintDto>> getAllComplaints() {
        List<ComplaintDto> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/{complaintId}")
    ResponseEntity<ComplaintDto> getComplaintById(@PathVariable int complaintId) {
        return ResponseEntity.ok(complaintService.getComplaintById(complaintId));
    }
}
