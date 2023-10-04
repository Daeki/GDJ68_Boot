package com.winter.app.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.winter.app.board.PostVO;
import com.winter.app.member.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityLogoutAdd implements LogoutHandler{@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
		log.info("====== Logout Add =============");
		
		this.logoutForKakao(authentication);
		
	}

	public void logoutForKakao(Authentication authentication) {
		RestTemplate restTemplate = new RestTemplate();
		MemberVO memberVO = (MemberVO)authentication.getPrincipal();
		log.info("---------- AccessToken {} -------", memberVO.getAccessToken());
			
		
	}


	public void userRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		
//		parameter Post 일 경우
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("postId", "1");
		
		
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(params, null);

//      결과가 하나일 때		
//		ResponseEntity<PostVO> res = restTemplate.getForEntity("https://jsonplaceholder.typicode.com/posts/1", PostVO.class, req);
//		
//		PostVO result= res.getBody();

		
//		결과가 여러개 일 때		
//		List<PostVO> res = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts", List.class, req);
		
		ResponseEntity<String> res = restTemplate.getForEntity("https://jsonplaceholder.typicode.com/comments?postId=1", String.class, req);
		
		
		log.info("***** Comments List : {} *****", res);
		
	}
	
	

}
