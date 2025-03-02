package com.complaint.infrastructure.repository;

import com.complaint.service.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    @Query("SELECT c FROM Complaint c JOIN FETCH c.product JOIN FETCH c.complainer")
    List<Complaint> findAll();
}
