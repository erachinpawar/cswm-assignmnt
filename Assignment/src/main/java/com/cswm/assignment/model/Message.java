package com.cswm.assignment.model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.http.HttpStatus;

@XmlRootElement
public class Message {

	private HttpStatus errorCode;
	private String message;
	private LocalDateTime created;

	public Message() {
	}

	public Message(HttpStatus id, String message) {
		this.errorCode = id;
		this.message = message;
		this.created = LocalDateTime.now();
	}

	public HttpStatus getId() {
		return errorCode;
	}

	public void setId(HttpStatus id) {
		this.errorCode = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

}
