package com.suph.security.core.dto;

/**
 * TB_BLOCK_MEMBER 차단 계정 테이블의 정보를 옮기는 역할
 */
public class BlockMemberDTO{
	/** 계정 일련 번호 */
	private Integer memNo;
	/** 차단 시작 일자 */
	private java.util.Date blockStartDate;
	/** 차단 만료 일자 */
	private java.util.Date blockExpireDate;
	/** 차단 사유 */
	private String blockCause;
	
	public Integer getMemNo(){
		return memNo;
	}
	
	public void setMemNo(Integer memNo){
		this.memNo = memNo;
	}
	
	public java.util.Date getBlockStartDate(){
		return blockStartDate;
	}
	
	public void setBlockStartDate(java.util.Date blockStartDate){
		this.blockStartDate = blockStartDate;
	}
	
	public java.util.Date getBlockExpireDate(){
		return blockExpireDate;
	}
	
	public void setBlockExpireDate(java.util.Date blockExpireDate){
		this.blockExpireDate = blockExpireDate;
	}
	
	public String getBlockCause(){
		return blockCause;
	}
	
	public void setBlockCause(String blockCause){
		this.blockCause = blockCause;
	}

	@Override
	public String toString(){
		return "BlockMemberDTO [memNo=" + memNo + ", blockStartDate=" + blockStartDate + ", blockExpireDate="
				+ blockExpireDate + ", blockCause=" + blockCause + "]";
	}
}
