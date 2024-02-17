package io.naddral.adessotest.mapper;

import io.naddral.adessotest.dto.PizzaDTO;
import io.naddral.adessotest.model.PizzaModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PizzaMapper {

    @Mapping(source = "order.number", target = "orderNumber")
    PizzaDTO toDto(PizzaModel source);

}
