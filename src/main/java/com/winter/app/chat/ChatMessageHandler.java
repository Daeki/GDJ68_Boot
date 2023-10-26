package com.winter.app.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Getter
@Setter
@ToString
public class ChatMessageHandler extends TextWebSocketHandler {
	
	//private ArrayList<WebSocketSession> sessions = new ArrayList<>();
	private Map<String, WebSocketSession> sessions = new HashMap<>();
	
	//대화방
	private Map<Long, Set<String>> single= new HashMap<>();
	
	//누가 몇번방
	private Map<String, Set<Long>> hasRoom = new HashMap<>();
	
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		log.info("======== WebSocket 연결 완료 ==========");
		log.info("WebSession : {}", session);
		log.info("Session User ===> {}", session.getPrincipal().getName());
		if(session.getPrincipal()!=null) {
			sessions.put(session.getPrincipal().getName(), session);
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		log.info("======== WebSocket 메세지 전송 ==========");
		log.info("======== TextMessage : {} ==========", message);
		
		//string -> json
		//message에서 방번화, 메세지를 객체로 변환
		String m = message.getPayload();
		ObjectMapper mapper = new ObjectMapper();
		ChatMessageVO chatMessageVO = mapper.readValue(m, ChatMessageVO.class);
		log.info("======== ChatMessageVO {} ==========", chatMessageVO);
		
		chatMessageVO.setSender(session.getPrincipal().getName());
		
		//방번호로 사용자 명단 추출 후 메세지 전송
		Set<String> members = single.get(chatMessageVO.getRoomnum());
		
		//m = mapper.writeValueAsString(chatMessageVO);
		
		members.forEach(member->{
			try {
				sessions.get(member).sendMessage(new TextMessage(mapper.writeValueAsString(chatMessageVO)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
//		sessions.forEach((k,v)-> {
//			try {
//				sessions.get(k).sendMessage(new TextMessage(session.getPrincipal().getName() + ": " + message.getPayload()));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		log.info("======== WebSocket 연결 해제 ==========");
		String username = session.getPrincipal().getName();
		Set<Long> rooms =this.hasRoom.get(username);
		rooms.forEach(room-> {
			this.single.get(room).remove(username);
		});
		
		sessions.remove(session.getPrincipal().getName());
		
		
		
	}
	
	
	
	

}
