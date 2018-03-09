package com.suph.security.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.catalina.webresources.EmptyResource;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.suph.security.core.dao.MemberDAO;
import com.suph.security.core.dto.MemberDTO;
import com.suph.security.core.enums.MemberState;
import com.suph.security.core.service.MemberAuthService;
import com.suph.security.core.service.MemberService;
import com.suph.security.core.userdetails.MemberInfo;

@Service("memberService")
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberAuthService memberAuthService;
	
	@Autowired
	private MemberDAO memberDAO;
		
	protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	protected Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	/*
	public void setMemberAuthService(MemberAuthService memberAuthService){
		this.memberAuthService = memberAuthService;
	}
	*/
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
		// DB로부터 해당 ID의 계정 정보 조회
		MemberDTO user = memberDAO.getMemberInfoById(id);
		
		// DB로부터 해당 ID와 일치하는 계정을 찾지 못했다면 예외 발생
		if(user == null){
			logger.debug("Query returned no results for user '" + id + "'");
			UsernameNotFoundException ue = new UsernameNotFoundException(
					messages.getMessage("JdbcDaoImpl.notFound", new Object[]{ id },
					"User {0} not found")
			);
			
			throw ue;
		}
		
		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
		
		// 이 계정의 권한 목록 반환
		dbAuthsSet.addAll( memberAuthService.loadUserAuthorities(user.getMemNo()) );
		
		// 계정은 있지만 어떠한 권한도 조회되지 않았다면 예외 발생.
		if(dbAuthsSet.size() == 0){
			// 이 계정에는 어떠한 권한도 없으며, '찾을 수 없음'으로 간주/처리 됩니다.
			logger.debug("User '" + id + "' has no authorities and will be treated as 'not found'");
			UsernameNotFoundException ue = new UsernameNotFoundException(
					messages.getMessage(
							"JdbcDaoImpl.noAuthority", 
							new Object[] {id},
							"User {0} has no GrantedAuthority"
					)
			);
			
			throw ue;
		}

		// 이 계정의 권한들을 Set -> List로 변환하여 계정 객체에 저장.
		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
		
		MemberInfo result = new MemberInfo(
				user.getMemNo(),
				user.getMemId(),
				user.getMemPassword(),
				user.getMemNicknm(),
				MemberState.ACTIVE.toString().equals( user.getMemState() ),
				dbAuths
		);
		
		return result;
	}
	
	@Override
	public Map<String, Object> getMember(){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		List<MemberDTO> list = null;
		try{
			list = memberDAO.selectMember();
			returnMap.put("list", list);
			returnMap.put("result", "success");
		}catch(DataAccessException de){
			returnMap.put("result", "fail");
			de.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Map<String, Object> postMemberIdDuplicateCheck(String memId){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{	
			if(StringUtils.hasText(memId) == false){
				// 비어있는 문자열일 시 NullPointerException 발생
				throw new NullPointerException();
			}
			
			memId = memId.trim();
			String dbId = memberDAO.selectMemId(memId);
			
			if(memId.equals(dbId) == true){
				// 아이디 중복시 DupicateName 예외 발생
				throw new DuplicateName();
			}
			
			returnMap.put("result", "success");
			returnMap.put("message", memId + "는 사용 가능 합니다.");
			
		}catch(NullPointerException npe){
			returnMap.put("result", "empty");
			returnMap.put("message", "아이디를 입력해 주세요.");
			//npe.printStackTrace();
		}catch(DuplicateName dn){
			returnMap.put("result", "duplicate");
			returnMap.put("message", memId + "는 이미 사용 중 입니다.");
			//dn.printStackTrace();
		}catch(DataAccessException de){
			returnMap.put("result", "fail");
			returnMap.put("message", "중복 검사에 실패했습니다.\n잠시 후 다시 시도해 주세요.");
			//de.printStackTrace();
		}
		
		return returnMap;
	}
}









