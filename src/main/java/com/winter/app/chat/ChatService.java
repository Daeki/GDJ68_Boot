package com.winter.app.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	
	@Autowired
	private ChatDAO chatDAO;
	
	public int createRoom(ChatMemberVO chatMemberVO) throws Exception{
		return chatDAO.createRoom(chatMemberVO);
	}

	public ChatMemberVO getRoom(ChatMemberVO chatMemberVO) throws Exception{
		ChatMemberVO checkVO = chatDAO.getRoom(chatMemberVO);
		
		if(checkVO == null) {
			int result = chatDAO.createRoom(chatMemberVO);
			return chatMemberVO;
		}
		
		return checkVO;
	}
	
	

}
