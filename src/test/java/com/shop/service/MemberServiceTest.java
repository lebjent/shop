package com.shop.service;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
public class MemberServiceTest {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	//멤버 생성
	public Member createMember() {
		
		MemberFormDto memberFormDto = new MemberFormDto();
		
		memberFormDto.setEmail("test@email.com");
		memberFormDto.setName("홍길동");
		memberFormDto.setAddress("서울시 마포구 합정동");
		memberFormDto.setPassword("1234");
		
		return Member.createMember(memberFormDto, passwordEncoder);
		
	}
	
	@Test
	@DisplayName("회원가입 테스트")
	public void saveMemberTest() {
		Member member = createMember();
		Member saveMember = memberService.saveMember(member);
		
		assertEquals(member.getEmail(), saveMember.getEmail());
		assertEquals(member.getName(), saveMember.getName());
		assertEquals(member.getAddress(), saveMember.getAddress());
		assertEquals(member.getPassword(), saveMember.getPassword());
		assertEquals(member.getRole(), saveMember.getRole());
		
	}
	
}
