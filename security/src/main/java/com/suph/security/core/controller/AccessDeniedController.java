package com.suph.security.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class AccessDeniedController{
	/**
	 * 접근 거부 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/access-denied", method=RequestMethod.GET)
	public String accessDenied(){
		return "access_denied";
	}
}
