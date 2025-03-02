package com.complaint.controller;

import com.complaint.service.ComplaintService;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.common.Result;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/complaint", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComplaintController {

    private final ComplaintService complaintService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<List<ComplaintDto>>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }
}
