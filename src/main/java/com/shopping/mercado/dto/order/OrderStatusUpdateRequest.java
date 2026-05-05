package com.shopping.mercado.dto.order;

import com.shopping.mercado.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;
}
