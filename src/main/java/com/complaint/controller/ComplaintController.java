package com.complaint.controller;

import com.complaint.controller.dto.CreateComplaintRequest;
import com.complaint.controller.dto.UpdateComplaintContentRequest;
import com.complaint.service.ComplaintService;
import com.complaint.service.dto.ComplaintDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/{complaintId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ComplaintDto> getComplaintById(@PathVariable int complaintId) {
        return ResponseEntity.ok(complaintService.getComplaintById(complaintId));
    }

    @PatchMapping(value = "/{complaintId}/content", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ComplaintDto> updateComplaintContent(
            @PathVariable int complaintId,
            @RequestBody @Valid UpdateComplaintContentRequest request) {

        ComplaintDto updatedComplaint = complaintService.updateComplaintContent(complaintId, request.content());
        return ResponseEntity.ok(updatedComplaint);
    }

    @PostMapping
    ResponseEntity<ComplaintDto> addComplaint(@RequestBody @Valid CreateComplaintRequest request) {
        ComplaintDto newComplaint = complaintService.addComplaint(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComplaint);
    }
}
