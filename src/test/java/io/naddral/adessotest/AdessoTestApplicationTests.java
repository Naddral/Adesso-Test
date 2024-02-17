package io.naddral.adessotest;

import io.naddral.adessotest.dto.OrderDTO;
import io.naddral.adessotest.dto.PizzaDTO;
import io.naddral.adessotest.exception.AwesomePizzaException;
import io.naddral.adessotest.mapper.OrderMapper;
import io.naddral.adessotest.model.OrderModel;
import io.naddral.adessotest.repository.OrderRepository;
import io.naddral.adessotest.repository.PizzaRepository;
import io.naddral.adessotest.service.OrderService;
import io.naddral.adessotest.util.StatusOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AdessoTestApplicationTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PizzaRepository pizzaRepository;

    OrderDTO buildOrderExample(){
        PizzaDTO pizza1 = PizzaDTO.builder()
                .name("Diavola")
                .price(BigDecimal.TEN)
                .quantity(2)
                .build();
        PizzaDTO pizza2 = PizzaDTO.builder()
                .name("Diavola")
                .price(BigDecimal.TEN)
                .quantity(2)
                .build();
        return OrderDTO.builder()
                .customer("customer1")
                .date(new Date())
                .status(StatusOrder.PENDING)
                .pizzas(List.of(pizza1, pizza2))
                .build();
    }

    @Test
    void createOrder_ShouldSaveOrderAndPizzas() throws AwesomePizzaException {
        OrderDTO orderDTO = buildOrderExample();
        OrderModel orderModel = OrderMapper.INSTANCE.toModel(orderDTO);
        when(orderRepository.save(any())).thenReturn(orderModel);
        OrderDTO result = orderService.createOrder(orderDTO);
        verify(orderRepository).save(any());
        verify(pizzaRepository, times(orderDTO.getPizzas().size())).save(any());
        assertNotNull(result);
    }

    @Test
    void createOrder_ShouldThrowExceptionOnFailure() {
        OrderDTO orderDTO = buildOrderExample();
        lenient().when(orderRepository.save(any())).thenThrow(new RuntimeException("Database error"));
        assertThrows(AwesomePizzaException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    void getOrder_ExistingOrder_ShouldReturnOrderDTO() throws AwesomePizzaException {
        String orderNumber = "123";
        OrderModel existingOrder = OrderMapper.INSTANCE.toModel(buildOrderExample());
        when(orderRepository.findById(orderNumber)).thenReturn(Optional.of(existingOrder));
        OrderDTO result = orderService.getOrder(orderNumber);
        assertNotNull(result);
    }

    @Test
    void getOrder_NonExistingOrder() throws AwesomePizzaException {
        String orderNumber = "456";
        when(orderRepository.findById(orderNumber)).thenReturn(Optional.empty());
        OrderDTO orderDTO = orderService.getOrder(orderNumber);
        assertNull(orderDTO);
    }

    @Test
    void getOrder_ShouldThrowException() {
        String orderNumber = "456";
        when(orderRepository.findById(orderNumber)).thenThrow(new RuntimeException("Order Not Existing."));
        assertThrows(AwesomePizzaException.class, () -> orderService.getOrder(orderNumber));
    }

    @Test
    void getNextOrder_ExistingOrder_ShouldReturnOrderDTO() throws AwesomePizzaException {
        List<StatusOrder> excludedStatuses = List.of(StatusOrder.IN_PROGRESS, StatusOrder.COMPLETED);
        OrderModel existingOrder = OrderMapper.INSTANCE.toModel(buildOrderExample());
        when(orderRepository.findFirstByStatusNotIn(excludedStatuses, Sort.by(Sort.Direction.ASC, "date")))
                .thenReturn(existingOrder);
        OrderDTO result = orderService.getNextOrder();
        assertNotNull(result);
    }

    @Test
    void getNextOrder_NoOrderFound() throws AwesomePizzaException {
        List<StatusOrder> excludedStatuses = List.of(StatusOrder.IN_PROGRESS, StatusOrder.COMPLETED);
        when(orderRepository.findFirstByStatusNotIn(excludedStatuses, Sort.by(Sort.Direction.ASC, "date")))
                .thenReturn(null);
        OrderDTO orderDTO = orderService.getNextOrder();
        assertNull(orderDTO);
    }

    @Test
    void getNextOrder_ShouldThrowException() {
        List<StatusOrder> excludedStatuses = List.of(StatusOrder.IN_PROGRESS, StatusOrder.COMPLETED);
        when(orderRepository.findFirstByStatusNotIn(excludedStatuses, Sort.by(Sort.Direction.ASC, "date")))
                .thenThrow(new RuntimeException("Order Not Found."));
        assertThrows(AwesomePizzaException.class, () -> orderService.getNextOrder());
    }

    @Test
    void updateOrderStatus_ExistingOrder_ShouldReturnTrue() throws AwesomePizzaException {
        String orderNumber = "123";
        OrderModel existingOrder = OrderMapper.INSTANCE.toModel(buildOrderExample());
        when(orderRepository.findById(orderNumber)).thenReturn(Optional.of(existingOrder));
        boolean result = orderService.updateOrderStatus(orderNumber, StatusOrder.COMPLETED);
        assertTrue(result);
    }

    @Test
    void updateOrderStatus_NonExistingOrder() throws AwesomePizzaException {
        String orderNumber = "456";
        when(orderRepository.findById(orderNumber)).thenReturn(Optional.empty());
        boolean result = orderService.updateOrderStatus(orderNumber, StatusOrder.COMPLETED);
        assertFalse(result);
    }

    @Test
    void updateOrderStatus_ShouldThrowException() {
        String orderNumber = "456";
        when(orderRepository.findById(orderNumber)).thenThrow(new RuntimeException("Order Not Found."));
        assertThrows(AwesomePizzaException.class, () -> orderService.updateOrderStatus(orderNumber, StatusOrder.COMPLETED));
    }

    @Test
    void getAllOrder_ExistingOrders_ShouldReturnOrderDTOList() throws AwesomePizzaException {
        List<OrderModel> existingOrders = List.of(OrderMapper.INSTANCE.toModel(buildOrderExample()));
        when(orderRepository.findByOrderByDateAsc()).thenReturn(existingOrders);
        List<OrderDTO> result = orderService.getAllOrder();
        assertNotNull(result);
        assertEquals(existingOrders.size(), result.size());
    }

    @Test
    void getAllOrder_NoOrdersFound() throws AwesomePizzaException {
        when(orderRepository.findByOrderByDateAsc()).thenReturn(Collections.emptyList());
        List<OrderDTO> orderDTO = orderService.getAllOrder();
        assertEquals(Collections.emptyList(), orderDTO);
    }

    @Test
    void getAllOrder_ShouldThrowException() {
        when(orderRepository.findByOrderByDateAsc()).thenThrow(new RuntimeException("Order Not Found."));
        assertThrows(AwesomePizzaException.class, () -> orderService.getAllOrder());
    }

}
