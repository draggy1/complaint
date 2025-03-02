package com.complaint.service.mapper;

import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.entity.Complainer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ComplainerMapper {
    ComplainerMapper INSTANCE = Mappers.getMapper(ComplainerMapper.class);

    ComplainerDto complainerToComplainerDto(Complainer complainer);
}
