package com.shopping.mercado.service;

import com.shopping.mercado.dto.order.OrderResponse;
import com.shopping.mercado.dto.order.OrderStatusUpdateRequest;
import com.shopping.mercado.dto.seller.SellerProfileRequest;
import com.shopping.mercado.dto.seller.SellerProfileResponse;
import com.shopping.mercado.entity.Orders;
import com.shopping.mercado.entity.Seller;
import com.shopping.mercado.entity.User;
import com.shopping.mercado.repository.OrderRepository;
import com.shopping.mercado.repository.SellerRepository;
import com.shopping.mercado.repository.UserRepository;
import com.shopping.mercado.util.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    @Transactional
    public SellerProfileResponse createSeller(SellerProfileRequest request, UUID userId) {

        if (sellerRepository.findByUserUserId(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Seller Profile already exists");
        }

        if (sellerRepository.findByStoreName(request.getStoreName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Store name already exists");
        }

        if (sellerRepository.findByGstNumber(request.getGstNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "GST already exists");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Seller seller = Seller.builder()
                .user(user)
                .storeName(request.getStoreName())
                .storeDescription(request.getStoreDescription())
                .storeLogoUrl(request.getStoreLogoUrl())
                .phoneNumber(request.getPhoneNumber())
                .gstNumber(request.getGstNumber())
                .isVerified(false)
                .build();

        SellerProfileResponse response;
        try {
            Seller savedSeller = sellerRepository.save(seller);

            response = new SellerProfileResponse();
            response.setSellerId(savedSeller.getSellerId());
            response.setStoreName(savedSeller.getStoreName());
            response.setStoreLogoUrl(savedSeller.getStoreLogoUrl());
            response.setStoreDescription(savedSeller.getStoreDescription());
            response.setPhoneNumber(savedSeller.getPhoneNumber());
            response.setGstNumber(savedSeller.getGstNumber());
            response.setVerified(savedSeller.isVerified());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate or Invalid Data");
        }

        return response;
    }

    @Transactional
    public SellerProfileResponse getSellerProfile(UUID userId) {
        Seller seller = sellerRepository.findByUserUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!seller.isVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller not verified, Kindly wait for approval");
        }

        SellerProfileResponse response;
        try {
            response = new SellerProfileResponse();
            response.setSellerId(seller.getSellerId());
            response.setStoreName(seller.getStoreName());
            response.setStoreLogoUrl(seller.getStoreLogoUrl());
            response.setStoreDescription(seller.getStoreDescription());
            response.setPhoneNumber(seller.getPhoneNumber());
            response.setGstNumber(seller.getGstNumber());
            response.setVerified(seller.isVerified());

            return response;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
    }

    @Transactional
    public SellerProfileResponse updateSellerProfile(UUID userId, SellerProfileRequest request) {
        Seller seller = sellerRepository.findByUserUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));

        if (sellerRepository.existsByStoreNameAndSellerIdNot(request.getStoreName(), seller.getSellerId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Store name already exists");
        }

        if (sellerRepository.existsByGstNumberAndSellerIdNot(request.getGstNumber(), seller.getSellerId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "GST number already exists");
        }

        seller.setStoreName(request.getStoreName());
        seller.setStoreLogoUrl(request.getStoreLogoUrl());
        seller.setStoreDescription(request.getStoreDescription());
        seller.setPhoneNumber(request.getPhoneNumber());
        seller.setGstNumber(request.getGstNumber());

        return new SellerProfileResponse(
                seller.getSellerId(),
                seller.getStoreName(),
                seller.getStoreDescription(),
                seller.getStoreLogoUrl(),
                seller.getPhoneNumber(),
                seller.getGstNumber(),
                seller.isVerified()
        );
    }

    public List<OrderResponse> getMyOrders(UUID userId) {
        Seller seller = sellerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));

        if (!seller.isVerified())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seller not verified");

        List<Orders> orders = orderRepository.findByOrderItems_Seller_SellerId(seller.getSellerId());

        if (orders.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No orders found");

        return orders.stream().map(orderMapper::toOrderResponse).toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID userId, UUID orderId, OrderStatusUpdateRequest request) {
        Seller seller = sellerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));

        if (!seller.isVerified())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seller not verified");

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

        boolean isSellersOrder = order.getOrderItems().stream()
                .anyMatch(item -> item.getSeller().getSellerId().equals(seller.getSellerId()));

        if (!isSellersOrder)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this order");

        order.getOrderItems().stream()
                .filter(item -> item.getSeller().getSellerId().equals(seller.getSellerId()))
                .forEach(item -> item.setOrderStatus(request.getOrderStatus()));

        Orders updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(updatedOrder);
    }
}