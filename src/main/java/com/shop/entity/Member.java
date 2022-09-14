package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
	@Id
	@Column(name = "member")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private String address;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public static Member createMember(MemberFormDto memberFromDto,PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setName(memberFromDto.getName());
		member.setEmail(memberFromDto.getEmail());
		member.setAddress(memberFromDto.getAddress());
		String password = passwordEncoder.encode(memberFromDto.getPassword());
		member.setPassword(password);
		member.setRole(memberFromDto.getRole());
		
		return member;
		
	}
	
}
