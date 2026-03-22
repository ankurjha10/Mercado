package com.shopping.mercado.dto.order;

import com.shopping.mercado.entity.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private OrderStatus orderStatus;
}
