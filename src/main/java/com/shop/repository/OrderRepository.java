package com.shop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	//JPQL로 작성 쿼리가 어렵거나 길 경우 QueryDsl을 이용 
	
	@Query("select o from Order o " +
		   "where o.member.email = :email " +
		   "order by o.orderDate desc")
	List<Order> findOrders(@Param("email")String email, Pageable pageable);
	
	//현재 로그인한 회원의 주문개수가 몇개인지 조회
	@Query("select count(o) from Order o " +
		   "where o.member.email = :email")
	Long countOrder(@Param("email")String email);
}
