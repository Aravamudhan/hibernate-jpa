package com.amudhan.jpatest.model.concurrency.version;

@SuppressWarnings("serial")
public class InvalidBidException extends Exception {

	public InvalidBidException(String message){
		super(message);
	}
}
