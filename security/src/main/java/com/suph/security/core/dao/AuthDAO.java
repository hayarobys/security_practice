package com.suph.security.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.suph.security.core.dto.AuthDTO;

@Repository
public interface AuthDAO{
	
	/**
	 * 모든 권한 목록을 반환합니다.
	 * @return
	 */
	public abstract List<AuthDTO> getAuthList();
	
	/**
	 * 권한을 등록합니다.
	 * @param authDTO
	 */
	public abstract void insertAuth(AuthDTO authDTO);
	
	/**
	 * 특정 권한을 수정합니다.
	 * @param authDTO
	 */
	public abstract void updateAuthByAuthNo(AuthDTO authDTO);
	
	/**
	 * 특정 권한을 제거합니다.
	 * @param authNo	제거할 권한의 일련 번호
	 */
	public abstract void deleteAuthByAuthNo(Integer authNo);
}
