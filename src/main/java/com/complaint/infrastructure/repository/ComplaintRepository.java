package com.complaint.infrastructure.repository;

import com.complaint.service.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    @Query("SELECT c FROM Complaint c JOIN FETCH c.product JOIN FETCH c.complainer")
    List<Complaint> findAll();

    @Query("SELECT c FROM Complaint c JOIN FETCH c.product JOIN FETCH c.complainer WHERE c.id = :id")
    Optional<Complaint> getComplaintById(@Param("id") int id);

    @Query("SELECT c FROM Complaint c WHERE c.product.id = :productId AND c.complainer.id = :complainerId")
    Optional<Complaint> findByProductAndComplainer(@Param("productId") int productId, @Param("complainerId") int complainerId);
}
