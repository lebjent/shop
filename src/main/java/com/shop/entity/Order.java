package com.shop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;

import com.shop.constant.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "order_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;//한명의 회원은 여러번 주문 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑을 한다.
	
	private LocalDateTime orderDate; //주문일
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;//주문상태
	
	/* Cascade TypeAll 옵션설정 방법
	 * PERSIST : 부모 엔티티가 영속화될 때 자식 엔티티도 영속화
	 * MERGE : 부모 엔티티가 병합될 때 자식 엔티티도 병합 
	 * REMOVE : 부모 엔티티가 삭제될 때 연관된 자식 엔티티도 삭제 
	 * REFRESH : 부모 엔티티가 refresh되면 연관된 자식 엔티티도 refresh
	 * DETACH : 부모 엔티티가 detach 되면 연관된 자식 엔티티도 detach 상태로 변경
	 * ALL : 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이
	 */
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
	private List<OrderItem> orderItems = new ArrayList<>();
	
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	public static Order createOrder(Member member, List<OrderItem> orderItemList) {
		Order order = new Order();
		order.setMember(member); //상품을 주문한 회원정보를 세팅
		
		/*
		 * 상품 페이지에서 1개를 주문하지만 장바구니 페이지는 여러개의 상품을 주문할 수 있으므로
		 * 리스트 형태로 받는다.
		 */
		for(OrderItem orderItem : orderItemList) {
			order.addOrderItem(orderItem);
		}
		order.setOrderStatus(OrderStatus.ORDER); //주문상태 세팅
		order.setOrderDate(LocalDateTime.now()); //주문일자 세팅
		
		return order;
	}
	
	//총 주문 금액을 구하는 메소드
	public int getTotalPrice() {
		int totalPrice = 0;
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
	}
	
	//주문상태를 취소상태로 바꿔주는 메소드
	public void cancelOrder() {
		this.orderStatus = OrderStatus.CANCEL;
		
		for(OrderItem orderItem:orderItems) {
			orderItem.cancel();
		}
	}
	
}
