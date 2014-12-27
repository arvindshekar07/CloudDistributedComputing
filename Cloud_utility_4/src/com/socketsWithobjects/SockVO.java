package com.socketsWithobjects;

import java.io.Serializable;

public class SockVO  implements Serializable{
	private String firstName = null;
	private String lastname = null;

	public SockVO(String firstName, String lastname) {
		super();
		this.firstName = firstName;
		this.lastname = lastname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}
