package com.winter.app.member;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member/*")
@Slf4j
public class MemberController {
	
	//service
	@Autowired
	private MemberService memberService;
	
	@GetMapping("info")
	public void getInfo()throws Exception{
		//DB에서 사용자 정보를 조회 해서 JSP로 보냄
	}
	
	@GetMapping("update")
	public void setUpdate(@AuthenticationPrincipal MemberVO memberVO, Model model)throws Exception{
//		MemberVO memberVO = (MemberVO)principal;
		//memberVO = memberService.getLogin(memberVO);
		
		MemberInfoVO memberInfoVO = new MemberInfoVO();
		memberInfoVO.setName(memberVO.getName());
		memberInfoVO.setBirth(memberVO.getBirth());
		memberInfoVO.setEmail(memberVO.getEmail());
		
		
		
		model.addAttribute("memberInfoVO", memberInfoVO);
	}
	
	@PostMapping("update")
	public String setUpdate(@Valid MemberInfoVO memberInfoVO, BindingResult bindingResult)throws Exception{
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MemberVO memberVO = (MemberVO)obj;
		memberVO.setEmail("UpdateEmail@naver.com");
		
		return "redirect:/";
	}
	

	
	@GetMapping("login")
	public String getLogin(@ModelAttribute MemberVO memberVO)throws Exception{
		SecurityContext context = SecurityContextHolder.getContext();
		
		String check=context.getAuthentication().getPrincipal().toString();
		
		log.info("===== Name : {} =====", context.getAuthentication().getPrincipal().toString());
		
		if(!check.equals("anonymousUser")) {
			return "redirect:/";
		}
		
		return "member/login";
		
	}
	

//	@GetMapping("join")
//	public void setJoin(Model model)throws Exception{
//		MemberVO memberVO = new MemberVO();
//		model.addAttribute("memberVO", memberVO);
//	}
	@GetMapping("join")
	public void setJoin(@ModelAttribute MemberVO memberVO)throws Exception{
		
	}
	
	@PostMapping("join")
	public String setJoin(@Valid MemberVO memberVO,BindingResult bindingResult, MultipartFile photo)throws Exception{
		
		 boolean check = memberService.getMemberError(memberVO, bindingResult);
		
		
		if(bindingResult.hasErrors() || check) {
			return "member/join"; 
		}
		
		int result = memberService.setJoin(memberVO);
		
		
		log.info("Photo : {} --- size : {}", photo.getOriginalFilename(), photo.getSize());
		return "redirect:../";
	}
	

}
