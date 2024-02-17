package io.naddral.adessotest.mapper;

import io.naddral.adessotest.dto.OrderDTO;
import io.naddral.adessotest.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "price", target = "totalPrice")
    OrderDTO toDto(OrderModel source);

    List<OrderDTO> toDtoList(List<OrderModel> source);

    OrderModel toModel(OrderDTO source);
}
