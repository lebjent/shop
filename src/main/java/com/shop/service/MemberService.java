package com.shop.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional //로직을 처리하다가 에러가 발생하면 변경된 데이터를 로직을 수행하기 이전 상태로 콜백시켜주는 역할을 한다.
@RequiredArgsConstructor //이 어노테이션은 final이나 @NonNull이 붙은 필드에 생성자를 생성해준다. @Autowired를 별도로 설정하지 않아도 된다.
public class MemberService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	
	//회원가입하는 멤버를 저장해주는 메서드
	public Member saveMember(Member member) {
		validateDuplicateMember(member);
		return memberRepository.save(member);
	}
	
	//중복된 이메일을 가진 회원이 있는지 체크
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		//이메일에 맞는 가입정보를 불러온다.
		Member member = memberRepository.findByEmail(email);
		
		if(member == null) {
			throw new UsernameNotFoundException(email);
		}
		
		
		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
	
}
