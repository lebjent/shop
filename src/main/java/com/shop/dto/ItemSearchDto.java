package com.shop.dto;

import com.shop.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
	
	/* 날짜조회타입 기준
	 * all:상품 등록일 전체
	 * 1d: 최근 하루 동안 등록된 상품
	 * 1w: 최근 일주일 동안 등록된 상품
	 * 1m: 최근 한달 동안 등록된 상품
	 * 6m: 최근 6개월동안 등록된 상품
	 */
	private String searchDateType; 
	
	
	private ItemSellStatus searchSellStatus; //상품상태에 따른 조회
	
	private String searchBy;//상품을 조회할때 어떤 유형으로 조회할지 선택;
	
	private String searchQuery = "";
	
	
}
