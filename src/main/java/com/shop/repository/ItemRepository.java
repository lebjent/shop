package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> , QuerydslPredicateExecutor<Item>,ItemRepositoryCustom {
	
	//상품이름으로 조회하기
	List<Item> findByItemNm(String itemNm);
	
	//상품이름 또는 상품설명 조회
	List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

	//파라미터로 넘어온 price 변수부터 값이 작은 상품 데이터를 조회하는 메소드
	List<Item>findByPriceLessThan(Integer price);
	
	//상품가격 내림차순으로 조회
	List<Item>findByPriceLessThanOrderByPriceDesc(Integer price);
	
	//@Query어노테이션
	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item>findByItemDetail(@Param("itemDetail")String itemDetail);

	//nativeQuery 속성을 이용한 상품 조회 리스트 (기존에 작성한 쿼리를 그대로 이용할 수 있는 장점이 있다.)
	//통계용 쿼리처럼 복잡한 쿼리를 사용해야 하는경우 활용 할 수 있다.
	@Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery=true)
	List<Item>findByItemDetailByNative(@Param("itemDetail")String itemDetail);
	
}
