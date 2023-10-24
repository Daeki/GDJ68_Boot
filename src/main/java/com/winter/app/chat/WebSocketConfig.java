package com.winter.app.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Autowired
	private ChatMessageHandler chatMessageHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addHandler(chatMessageHandler, "/chat")
				.addInterceptors(new HttpSessionHandshakeInterceptor())
				.setAllowedOrigins("http://localhost:82/chat")
				.withSockJS()
				.setClientLibraryUrl("http://localhost:82/js/chat/chat.js")
				;
	}
	
	

}
