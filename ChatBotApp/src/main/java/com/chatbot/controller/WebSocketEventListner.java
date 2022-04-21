package com.chatbot.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chatbot.model.ChatMessage;
import com.chatbot.model.MessageType;

@Component
public class WebSocketEventListner {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WebSocketEventListner.class);

	@Autowired
	private SimpMessageSendingOperations sendingOperations;

	@EventListener
	public void handleWebSocketConnectListner(SessionConnectEvent event) {

		logger.info("Boom Baam Connection !!!");
	}

	@EventListener
	public void handleWebSocketDisconnectListner(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		final String username = (String) headerAccessor.getSessionAttributes().get("username");

		final ChatMessage chatMessage = ChatMessage.builder().type(MessageType.DISCONNECT).sender(username).build();
		
		sendingOperations.convertAndSend("/topic/public", chatMessage);
	}
}
