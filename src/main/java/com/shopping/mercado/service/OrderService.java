package com.shopping.mercado.service;

import com.shopping.mercado.dto.order.CheckoutRequest;
import com.shopping.mercado.dto.order.OrderItemResponse;
import com.shopping.mercado.dto.order.OrderResponse;
import com.shopping.mercado.entity.*;
import com.shopping.mercado.repository.AddressRepository;
import com.shopping.mercado.repository.CartRepository;
import com.shopping.mercado.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.shopping.mercado.entity.enums.OrderStatus;
import com.shopping.mercado.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse placeOrder(UUID customerId, CheckoutRequest request){
        // Fetch the cart for the customer
        Cart cart = cartRepository.findByUserId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + customerId));

        List<CartItem> cartItemList = getCartItems(customerId, cart);

        //Fetch the Address
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found for user: " + customerId));

        if (!address.getUser().getUserId().equals(customerId)) {
            throw new RuntimeException("Use a valid address");
        }

        //Creating a new Order
        Orders orders = Orders.builder()
                .customer(cart.getCustomer())
                .shippingAddress(address)
                .paymentStatus(PaymentStatus.PENDING)
                .orderStatus(OrderStatus.PENDING)
                .build();

        //Converting Cart Items to Order Items
        List<OrderItems> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItemList){
            OrderItems items = OrderItems.builder()
                    .order(orders)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(cartItem.getProduct().getProductPrice())
                    .build();

            orderItems.add(items);
        }

        //Calculating the Total Amount
        BigDecimal total = orderItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orders.setOrderItems(orderItems);
        orders.setTotalAmount(total);

        //Save the Order
        Orders placedOrder = orderRepository.save(orders);

        //Clearing Cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(placedOrder.getOrderId());
        orderResponse.setOrderStatus(placedOrder.getOrderStatus().name());
        orderResponse.setShippingAddress(placedOrder.getShippingAddress().getAddressLine1()
                + ", " + placedOrder.getShippingAddress().getAddressLine2()
                + ", " + placedOrder.getShippingAddress().getCity()
                + ", " + placedOrder.getShippingAddress().getState()
                + ", " + placedOrder.getShippingAddress().getPostalCode()
                + ", " + placedOrder.getShippingAddress().getCountry()
        );

        orderResponse.setOrderItems(placedOrder.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductId(item.getProduct().getProductId());
            itemResponse.setProductName(item.getProduct().getProductName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());
            itemResponse.setSubtotal(item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())));
            return itemResponse;
        }).toList());

        orderResponse.setPlacedAt(placedOrder.getPlacedAt());
        orderResponse.setTotalAmount(placedOrder.getTotalAmount());

        return orderResponse;
    }

    private static List<CartItem> getCartItems(UUID customerId, Cart cart) {
        List<CartItem> cartItemList = cart.getCartItems();

        //Check if cart is empty or not
        if (cartItemList.isEmpty()) {
            throw new RuntimeException("Cart is empty for user: " + customerId);
        }

        //Checking Stock Availability
        for (CartItem cartItem : cartItemList){
            Product product = cartItem.getProduct();
            if (product.getProductStock() < cartItem.getQuantity()){
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
            }
            else {
                // Reduce the stock
                product.setProductStock(product.getProductStock() - cartItem.getQuantity());
            }
        }
        return cartItemList;
    }
}
