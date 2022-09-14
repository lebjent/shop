package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
	
	//동적으로 쿼리를 생성하기 위해서 JPAQueryFactory 클래스를 사용.
	private JPAQueryFactory queryFactory;
	
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	//상품 판매조건이 null일경우 null을 리턴 
	//상태조건이 null이 아니라 판매중 or 품절 상태라면 해당 조건의 상품만 조회한다. 
	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
		return searchSellStatus == null ? null:QItem.item.itemSellStatus.eq(searchSellStatus);
	}
	
	//날짜 조회하는 쿼리
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();//현재 지역의 시간
		
		if(StringUtils.equals("all", searchDateType)||searchDateType == null) {
			return null;
		}else if(StringUtils.equals("1d", searchDateType)) {
			dateTime = dateTime.minusDays(1);
		}else if(StringUtils.equals("1w", searchDateType)) {
			dateTime = dateTime.minusWeeks(1);
		}else if(StringUtils.equals("1m", searchDateType)) {
			dateTime = dateTime.minusMonths(1);
		}else if(StringUtils.equals("6m", searchDateType)) {
			dateTime = dateTime.minusMonths(6);
		}
		
		return QItem.item.regTime.after(dateTime);
		
	}
	
	public BooleanExpression searchByLike(String searchBy, String searchQuery) {
		
		if(StringUtils.equals("itemNm", searchBy)) {
			return QItem.item.itemNm.like("%"+searchQuery+"%");
		}else if(StringUtils.equals("createdBy", searchBy)) {
			return QItem.item.createdBy.like("%"+searchQuery+"%");
		}
		
		return null;
	}
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		
	     QueryResults<Item> results = queryFactory
	                .selectFrom(QItem.item)
	                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
	                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
	                        searchByLike(itemSearchDto.getSearchBy(),
	                                itemSearchDto.getSearchQuery()))
	                .orderBy(QItem.item.id.desc())
	                .offset(pageable.getOffset())
	                .limit(pageable.getPageSize())
	                .fetchResults();
	     
	        List<Item> content = results.getResults();
	        long total = results.getTotal();

	        return new PageImpl<>(content, pageable, total);
	     
		
	}
	
	private BooleanExpression itemNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null :QItem.item.itemNm.like("%"+searchQuery+"%");
	}
	
	//메인페이지의 데이터를 가져오는 메소드
	@Override
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		
		QItem item = QItem.item;
		
		QItemImg itemImg = QItemImg.itemImg;
		
		QueryResults<MainItemDto> results = queryFactory.select(
											new QMainItemDto( //@QueryProjection을 사용하면 DTO로 바로 조회가 가능
													item.id,
													item.itemNm,
													item.itemDetail,
													itemImg.imgUrl,
													item.price)
											)
											.from(itemImg)
											.join(itemImg.item,item)
											.where(itemImg.repimgYn.eq("Y"))
											.where(itemNmLike(itemSearchDto.getSearchQuery()))
											.orderBy(item.id.desc())
											.offset(pageable.getOffset())
											.limit(pageable.getPageSize())
											.fetchResults();
		
		List<MainItemDto> content = results.getResults();
		
		System.out.println(content);
		
		long total = results.getTotal();
		
		return new PageImpl<> (content,pageable,total);
	}
	
}
