package com.cswm.assignment.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.http.HttpStatus;
@XmlRootElement
public class Message {

	private HttpStatus errorCode;
    private String message;
    private Date created;
    
    public Message() {
    	
    }
    
    public Message(HttpStatus id, String message) {
    	this.errorCode = id;
    	this.message = message;
    	this.created = new Date();
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
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
}
