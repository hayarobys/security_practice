package com.suph.security.core.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberService extends UserDetailsService{
	/**
	 * ID와 일치하는 계정의 정보를 조회하는 메서드 입니다.
	 * 다음의 구현 원칙을 따르십시오.
	 * ㄱ. id에 해당하는 데이터가 존재하면 UserDetails 타입의 객체를 리턴한다. (아이디, 비밀번호, 보유권한 목록을 지니고 있어야 한다)
	 * ㄴ. id에 해당하는 데이터가 존재하지만, 해당 사용자가 어떤 권한(GrantedAuthority)도 갖고 있지 않을 경우 UserNotFoundException을 발생시킨다.
	 * ㄷ. id에 해당하는 데이터가 존재하지 않으면, UserNotFoundException을 발생시킨다.
	 * 
	 * @param id 조회할 계정 ID 
	 */
	@Override
	public abstract UserDetails loadUserByUsername(String id) throws UsernameNotFoundException;
	
	/**
	 * 모든 계정의 패스워드를 제외한 정보들을 조회합니다.
	 * @return
	 */
	public abstract Map<String, Object> getMember();
	
	/**
	 * 이 아이디가 다른 계정과 중복없이 사용 가능한지 검사합니다.
	 * @param memId
	 * @return
	 */
	public abstract Map<String, Object> postMemberIdDuplicateCheck(String memId);
}
