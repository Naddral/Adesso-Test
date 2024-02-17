package io.naddral.adessotest.service;

import io.naddral.adessotest.dto.OrderDTO;
import io.naddral.adessotest.dto.PizzaDTO;
import io.naddral.adessotest.exception.AwesomePizzaException;
import io.naddral.adessotest.mapper.OrderMapper;
import io.naddral.adessotest.model.OrderModel;
import io.naddral.adessotest.model.PizzaModel;
import io.naddral.adessotest.repository.OrderRepository;
import io.naddral.adessotest.repository.PizzaRepository;
import io.naddral.adessotest.util.StatusOrder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderService {

    OrderRepository orderRepository;

    PizzaRepository pizzaRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, PizzaRepository pizzaRepository) {
        this.orderRepository = orderRepository;
        this.pizzaRepository = pizzaRepository;
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) throws AwesomePizzaException{
        try {
            OrderModel order = OrderMapper.INSTANCE.toModel(orderDTO);
            order.setNumber(UUID.randomUUID().toString());
            order.setDate(new Date());
            order.setPrice(calculateTotalPrice(orderDTO.getPizzas()));
            OrderModel orderSaved = orderRepository.save(order);
            for(PizzaModel pizza : order.getPizzas()){
                pizza.setOrder(orderSaved);
                pizzaRepository.save(pizza);
            }
            return OrderMapper.INSTANCE.toDto(orderSaved);
        }catch(Exception ex) {
            throw new AwesomePizzaException(List.of(), Strings.EMPTY, ExceptionUtils.getMessage(ex));
        }
    }

    private BigDecimal calculateTotalPrice(List<PizzaDTO> pizzas) {
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.valueOf(0));
        pizzas.forEach(pizza -> {
            Integer quantity = pizza.getQuantity();
            BigDecimal price = pizza.getPrice();
            totalPrice.updateAndGet(v -> v.add(BigDecimal.valueOf(quantity).multiply(price)));
        });
        return totalPrice.get();
    }

    public OrderDTO getOrder(String number) throws AwesomePizzaException{
        try {
            return OrderMapper.INSTANCE.toDto(orderRepository.findById(number).orElse(null));
        }catch(Exception ex) {
            throw new AwesomePizzaException(List.of(), Strings.EMPTY, ExceptionUtils.getMessage(ex));
        }
    }

    public OrderDTO getNextOrder() throws AwesomePizzaException{
        try {
            List<StatusOrder> statusOrderList = List.of(StatusOrder.IN_PROGRESS, StatusOrder.COMPLETED);
            return OrderMapper.INSTANCE.toDto(
                    orderRepository.findFirstByStatusNotIn(statusOrderList,
                            Sort.by(Sort.Direction.ASC, "date")));
        }catch(Exception ex) {
            throw new AwesomePizzaException(List.of(), Strings.EMPTY, ExceptionUtils.getMessage(ex));
        }
    }

    public Boolean updateOrderStatus(String orderNumber, StatusOrder status) throws AwesomePizzaException{
        boolean updated = false;
        try {
            Optional<OrderModel> orderOpt = orderRepository.findById(orderNumber);
            if(orderOpt.isPresent()) {
                OrderModel order = orderOpt.get();
                order.setStatus(status);
                order.setLastUpdate(new Date());
                orderRepository.save(order);
                updated = true;
            }
        }catch(Exception ex) {
            throw new AwesomePizzaException(List.of(), Strings.EMPTY, ExceptionUtils.getMessage(ex));
        }
        return updated;
    }

    public List<OrderDTO> getAllOrder() throws AwesomePizzaException{
        try {
            return OrderMapper.INSTANCE.toDtoList(orderRepository.findByOrderByDateAsc());
        }catch(Exception ex) {
            throw new AwesomePizzaException(List.of(), Strings.EMPTY, ExceptionUtils.getMessage(ex));
        }
    }

}
