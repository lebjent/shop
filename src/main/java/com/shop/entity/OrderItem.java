package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{
	
	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;//하나의 상품은 여러주문 상품으로 들어갈 수 있으므로 주문상품 기준으로 다대일 단방향 매핑을 설정한다.
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;//한번의 주문에 여러개의 상품을 주문할 수 있으므로 주문 엔티티를 다대일 단방향 매핑을 먼저 설정한다.
	
	private int orderPrice;//주문가격
	
	private int count;//수량
	
	public static OrderItem createOrderItem(Item item, int count) {
		OrderItem orderItem = new OrderItem();
		//주문할 상품과 상품수량 세팅
		orderItem.setItem(item);
		orderItem.setCount(count);
		
		//현재시간을 기준으로 주문가격 세팅
		orderItem.setOrderPrice(item.getPrice());
		
		//주문 수량만큼 상품의 재고수량을 감소
		item.removeStock(count);
		return orderItem;
		
	}
	
	//주문가격과 주문수량을 곱해서 해당상품을 주문한 총가격을 계산하는 메소드
	public int getTotalPrice() {
		return orderPrice * count;
	}
	
	//주문취소시 주문수량 만큼 상품의 재고를 더해준다.
	public void cancel() {
		this.getItem().addStock(count);
	}
	
	
//	private LocalDateTime regTime; (BaseEntity를 상속받으므로 주석처리)
	
//	private LocalDateTime updateTime; (BaseEntity를 상속받으므로 주석처리)
	
}
