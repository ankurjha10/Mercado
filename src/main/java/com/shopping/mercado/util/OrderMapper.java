package com.shopping.mercado.util;

import com.shopping.mercado.dto.order.OrderItemResponse;
import com.shopping.mercado.dto.order.OrderResponse;
import com.shopping.mercado.entity.Orders;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Orders order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderStatus(order.getOrderStatus().name());
        response.setTotalAmount(order.getTotalAmount());
        response.setPlacedAt(order.getPlacedAt());
        response.setShippingAddress(
                order.getShippingAddress().getAddressLine1()
                        + ", " + order.getShippingAddress().getAddressLine2()
                        + ", " + order.getShippingAddress().getCity()
                        + ", " + order.getShippingAddress().getState()
                        + ", " + order.getShippingAddress().getPostalCode()
                        + ", " + order.getShippingAddress().getCountry()
        );
        response.setOrderItems(order.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductId(item.getProduct().getProductId());
            itemResponse.setProductName(item.getProduct().getProductName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setProductImage(item.getProduct().getProductImage());
            itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());
            itemResponse.setSubtotal(item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())));
            return itemResponse;
        }).toList());
        return response;
    }
}
