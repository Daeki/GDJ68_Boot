package com.winter.app.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatRoom/*")
public class ChatController {
	
	
	@GetMapping("enter")
	public void chatRoom() {
		
	}

}
