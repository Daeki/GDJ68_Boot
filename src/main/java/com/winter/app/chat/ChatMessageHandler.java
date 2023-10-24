package com.winter.app.chat;

import java.util.ArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatMessageHandler extends TextWebSocketHandler {
	
	private ArrayList<WebSocketSession> sessions = new ArrayList<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		log.info("======== WebSocket 연결 완료 ==========");
		log.info("WebSession : {}", session);
		sessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		log.info("======== WebSocket 메세지 전송 ==========");
		for (WebSocketSession sess: sessions) {
			sess.sendMessage(new TextMessage(message.getPayload()));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		log.info("======== WebSocket 연결 해제 ==========");
		sessions.remove(session);
	}
	
	
	
	

}
