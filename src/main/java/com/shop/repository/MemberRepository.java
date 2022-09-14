package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	//회원가입 중복확인을 하기위한 쿼리 메소드
	Member findByEmail(String email);
	
}
