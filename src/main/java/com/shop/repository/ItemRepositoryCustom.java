package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;

public interface ItemRepositoryCustom {
	
	//상품관리 페이지 결과 페이징 처리
	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable );
	
	//메인페이지 상품 리스트 가져오는 메소드
	Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,Pageable pageable);
}
