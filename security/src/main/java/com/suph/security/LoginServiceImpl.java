package com.suph.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private AuthDAO authDAO;
	
	@Autowired
	private LoginDAO loginDAO;
		
	protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	protected Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
		logger.debug("id로 계정 조회 동작 {}", id);
		// DB로부터 해당 ID의 계정 정보 조회
		MemberInfo user = loginDAO.getMemberInfoById(id);
		
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
		dbAuthsSet.addAll( loadUserAuthorities(user.getUsername()) );
		
		// 이 계정의 권한들을 Set -> List로 변환하여 계정 객체에 저장.
		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
		user.setAuthorities(dbAuths);
		
		// 만약 어떠한 권한도 조회되지 않았다면 익셉션 발생.
		if(dbAuths.size() == 0){
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
		
		logger.debug("user {}", user);
		
		return user;
	}
	
	/**
	 * DB의 권한 테이블에서 해당 계정의 권한 목록을 조회합니다.
	 * [{1,'admin','관리자','Y',1,'ROLE_ADMIN','관리자에게 주어지는 최고권한 입니다. 관리자 이외 누구에게도 부여하지 마십시오.'},
	 * {1,'user','일반인','Y','2','ROLE_USER','회원에게 부여되는 권한 입니다. 일반적인 조회,삭제,수정 권한을 지니고 있습니다.'}]
	 * @param userNo 조회할 계정 일련 번호
	 * @return 해당 계정의 권한 목록
	 */
	protected List<GrantedAuthority> loadUserAuthorities(String id){
		logger.debug("id로 권한 조회 동작 {}", id);
		List<AuthVO> list = authDAO.getAuthListById(id);
		List<GrantedAuthority> resultList = new ArrayList<GrantedAuthority>();
		for(AuthVO vo : list){
			resultList.add( new SimpleGrantedAuthority(vo.getName()) );
		}
		
		return resultList;
	}
}
