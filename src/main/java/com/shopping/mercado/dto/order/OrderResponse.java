package com.shopping.mercado.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private String orderStatus;
    private String shippingAddress;
    private LocalDateTime placedAt;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> orderItems;
}
