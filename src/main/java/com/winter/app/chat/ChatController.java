package com.winter.app.chat;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winter.app.member.MemberVO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/chatRoom/*")
public class ChatController {
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private ChatMessageHandler chatMessageHandler;
	
	@GetMapping("check")
	@ResponseBody
	public Set<String> check(Principal principal) {
		
		return chatMessageHandler.getSessions().keySet();
		
	}
	
	@GetMapping("enter")
	public void chatRoom(String receiver, Principal principal, Model model) throws Exception {
		//현재 대화 상대가 있는 지 확인
		boolean check =chatMessageHandler.getSessions().containsKey(receiver);
		
		log.info("********** check : {}", check);
		if(check) {
			/**
			 * DB에서 채팅방이 있는지 확인하고, 없으면 새로운방을 만들고 방번호 리턴
			 * */
			//두 사용자간 전 채팅 방이 있는지 확인
			
			ChatMemberVO chatMemberVO = new ChatMemberVO();
			chatMemberVO.setUser1(receiver);
			chatMemberVO.setUser2(principal.getName());
			
			chatMemberVO= chatService.getRoom(chatMemberVO);
			
			

			
			//user1 채팅방
			Set<Long> rooms = chatMessageHandler.getHasRoom().get(chatMemberVO.getUser1());
			
			//처음이면 rooms가 null
			if(rooms==null) {
				Map<String, Set<Long>> map = new HashMap<>();
				Set<Long> newrooms = new HashSet<>();
				newrooms.add(chatMemberVO.getRoomNo());
				map.put(chatMemberVO.getUser1(), rooms);
				chatMessageHandler.setHasRoom(map);
			}else {
				if(!rooms.contains(chatMemberVO.getRoomNo())) {
					rooms.add(chatMemberVO.getRoomNo());
				}
			}
			//user2 채팅방
			rooms = chatMessageHandler.getHasRoom().get(chatMemberVO.getUser2());
			if(rooms==null) {
				Map<String, Set<Long>> map = new HashMap<>();
				Set<Long> newrooms = new HashSet<>();
				newrooms.add(chatMemberVO.getRoomNo());
				map.put(chatMemberVO.getUser2(), rooms);
				chatMessageHandler.setHasRoom(map);
			}else {
				if(!rooms.contains(chatMemberVO.getRoomNo())) {
					rooms.add(chatMemberVO.getRoomNo());
				}
			}
			
			
			//채팅방이 있는지 확인
			check = chatMessageHandler.getSingle().containsKey(chatMemberVO.getRoomNo());			
			
			if(!check) {
				Set<String> members = new HashSet<>();
				members.add(receiver);
				members.add(principal.getName());
				chatMessageHandler.getSingle().put(chatMemberVO.getRoomNo(), members);
			}
				
			
			model.addAttribute("roomnum", chatMemberVO.getRoomNo());
		}
	}

}
