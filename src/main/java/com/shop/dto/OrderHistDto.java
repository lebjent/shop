package com.shop.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;

import lombok.Getter;
import lombok.Setter;

//주문정보 담을 클래스

@Getter
@Setter
public class OrderHistDto {
	
	public OrderHistDto(Order order) {
		this.orderId = order.getId();
		this.orderStatus = order.getOrderStatus();
		//주문 날짜의 경우 화면에 yyyy-MM-dd HH:mm형태로 전달하기위해서
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
	
	//주문상품 리스트
	private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
	
	private Long orderId; //주문 아이디
	private String orderDate;//주문날짜
	private OrderStatus orderStatus;//주문 상태
	
	//orderItemDto객체를 주문리스트에 추가
	public void addOrderItemDto(OrderItemDto orderItemDto) {
		orderItemDtoList.add(orderItemDto);
	}
}
