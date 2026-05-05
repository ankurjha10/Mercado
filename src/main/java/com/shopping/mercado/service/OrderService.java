package com.shopping.mercado.service;

import com.shopping.mercado.dto.order.CheckoutRequest;
import com.shopping.mercado.dto.order.OrderItemResponse;
import com.shopping.mercado.dto.order.OrderResponse;
import com.shopping.mercado.dto.order.OrderStatusUpdateRequest;
import com.shopping.mercado.entity.*;
import com.shopping.mercado.repository.AddressRepository;
import com.shopping.mercado.repository.CartRepository;
import com.shopping.mercado.repository.OrderRepository;
import com.shopping.mercado.repository.ProductRepository;
import com.shopping.mercado.util.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shopping.mercado.entity.enums.OrderStatus;
import com.shopping.mercado.entity.enums.PaymentStatus;
import org.springframework.web.server.ResponseStatusException;

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
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;


    @Transactional
    public OrderResponse placeOrder(UUID customerId, CheckoutRequest request){
        // Fetch the cart for the customer
        Cart cart = cartRepository.findByUserId(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for user: " + customerId));

        List<CartItem> cartItemList = getCartItems(customerId, cart);

        //Fetch the Address
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found for user: " + customerId));

        if (!address.getUser().getUserId().equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Use a valid address");
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

        return orderMapper.toOrderResponse(placedOrder);
    }


    //To Get All the Orders Placed
    public List<OrderResponse> getMyOrders(UUID customerId) {

        List<Orders> orders = orderRepository.findByCustomer_UserId(customerId);

        if (orders.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for user: " + customerId);

        return orders.stream().map(orderMapper::toOrderResponse).toList();
    }

    //TO Get a Particular Order by ID
    public OrderResponse getOrderById(UUID orderId, UUID customerId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

        if (!order.getCustomer().getUserId().equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to this order");
        }

        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(UUID orderId, UUID customerId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

        if (!order.getCustomer().getUserId().equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to this order");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending orders can be cancelled");
        }

        // Restore stock
        for (OrderItems item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setProductStock(product.getProductStock() + item.getQuantity());
            productRepository.save(product);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        Orders cancelledOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(cancelledOrder);
    }


    private List<CartItem> getCartItems(UUID customerId, Cart cart) {
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
                productRepository.save(product);
            }
        }
        return cartItemList;
    }
}