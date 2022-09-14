package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
	
	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//상품코드
	
	@Column(nullable = false,length = 50)
	private String itemNm;//상품명
	
	@Column(name = "price", nullable = false)
	private int price;//가격
	
	@Column(nullable = false)
	private int stockNumber;//재고수량
	
	@Lob
	@Column(nullable = false)
	private String itemDetail; //상품 상세 설명
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus; //상품 판매 상태
	
	public void updateItem(ItemFormDto itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}
	
	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber - stockNumber; //상품의 재고수량에서 주문후 남은수량을 체크
		//상품의 재고가 주문수량보다 적은경우 Exception을 발생
		if(restStock<0) {
			throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량:"+ this.stockNumber +")");
		}
		this.stockNumber = restStock; //재고수량을 업데이트
	}
	
	//주문 취소시 상품의 재고를 증가시키는 메소드
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}
	
	
}
