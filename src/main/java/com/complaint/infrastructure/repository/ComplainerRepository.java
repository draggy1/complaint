package com.complaint.infrastructure.repository;

import com.complaint.service.entity.Complainer;
import org.springframework.data.repository.CrudRepository;

public interface ComplainerRepository extends CrudRepository<Complainer, Integer> {
}
