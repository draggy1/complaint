package com.complaint.service.mapper;

import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.entity.Complainer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComplainerMapperTest {

    @Test
    void shouldMapComplainerToComplainerDto() {
        // given
        Complainer complainer = new Complainer(1, "John", "Doe", 0);

        // when
        ComplainerDto complainerDto = ComplainerMapper.INSTANCE.complainerToComplainerDto(complainer);

        // then
        assertThat(complainerDto).isEqualTo(new ComplainerDto(1, "John", "Doe"));
    }
}