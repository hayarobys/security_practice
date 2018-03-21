package com.suph.security.core.service;

import java.util.Date;
import java.util.Map;

import com.suph.security.core.dto.BlockMemberDTO;

public interface BlockMemberService{
	/**
	 * 현시간 기준, 특정 계정의 차단 종료일이 남아 있을 경우, 그 차단 정보를 조회합니다.
	 * @param memNo
	 * @return
	 */
	public abstract Map<String, Object> getBlockMemberByMemNoAndExpireDateIsAfterTheCurrentDate(Integer memNo);
	
	/**
	 * 현시간 기준, 차단 종료일이 남아있는 모든 차단 목록 조회
	 * @return
	 */
	public abstract Map<String, Object> getBlockMember(Date blockStartDate, Date blockExpireDate, String[] searchTime);
	
	/**
	 * 차단 계정을 추가 합니다.
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	public abstract Map<String, Object> postBlockMember(BlockMemberDTO blockMemberDTO);
	
	/**
	 * 차단 계정 정보를 수정 합니다.
	 * @param memNo
	 * @param blockMemberDTO
	 * @return
	 */
	public abstract Map<String, Object> patchBlockMember(Integer memNo, BlockMemberDTO blockMemberDTO);
	
	/**
	 * 차단 계정 정보를 삭제 합니다.
	 * @param memNo
	 * @return
	 */
	public abstract Map<String, Object> deleteBlockMember(Integer memNo);
}
