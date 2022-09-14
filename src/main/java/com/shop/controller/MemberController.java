package com.shop.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	
	//회원가입 뷰로 이동
	@GetMapping(value = "/new")
	public String memberForm(Model model) throws Exception{
			
		model.addAttribute("memberFormDto", new MemberFormDto());
		
		return "member/memberForm";
		
	}
	
	//회원가입 실질적인 진행
	//+ validation을 통하여 회원가입 유효성 검사를 실행
	@PostMapping(value = "/new")
	public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) throws Exception{
		
		//DTO에서 validation을 설정한 에러를 메세지 처리
		if(bindingResult.hasErrors()) {
			return "member/memberForm";
		}
		
		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			//중복체크시 에러가 발생하면 메시지를 model객체에 담아준다.
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}
		
		
		return "redirect:/";
	}
	
	//로그인 페이지로 이동
	@GetMapping(value = "/login")
	public String loginMember() {
		return "/member/memberLoginForm";
	}
	
	//로그인시 에러 발생시 문구
	@GetMapping(value = "/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
		return "/member/memberLoginForm";
	}
}
