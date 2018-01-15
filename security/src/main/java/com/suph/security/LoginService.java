package com.suph.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginService extends UserDetailsService{

	/**
	 * ID와 일치하는 계정의 정보를 조회하는 메서드 입니다.
	 * 다음의 구현 원칙을 따르십시오.
	 * ㄱ. id에 해당하는 데이터가 존재하면 UserDetails 타입의 객체를 리턴한다.
	 * ㄴ. id에 해당하는 데이터가 존재하지만, 해당 사용자가 어떤 권한(GrantedAuthority)도 갖고 있지 않을 경우 UserNotFoundException을 발생시킨다.
	 * ㄷ. username에 해당하는 데이터가 존재하지 않으면, UserNotFoundException을 발생시킨다.
	 * 
	 * @param id 조회할 계정 ID 
	 */
	public abstract UserDetails loadUserByUsername(String id) throws UsernameNotFoundException;
}
