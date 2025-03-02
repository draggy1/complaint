package com.complaint.service.mapper;

import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.entity.Complaint;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ProductMapper.class, ComplainerMapper.class})
public interface ComplaintMapper {
    ComplaintMapper INSTANCE = Mappers.getMapper(ComplaintMapper.class);

    ComplaintDto complaintToComplaintDto(Complaint complaint);
}
