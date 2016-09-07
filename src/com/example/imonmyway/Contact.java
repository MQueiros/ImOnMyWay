package com.example.imonmyway;

import java.io.Serializable;

public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contact_name;
	private int contact_number;

	public Contact(String contact_name, int contact_number) {
		this.contact_name = contact_name;
		this.contact_number = contact_number;
	}

	public String getName() {
		return contact_name;
	}

	public int getNumber() {
		return contact_number;
	}

	public void setName(String contact_name) {
		this.contact_name = contact_name;
	}

	public void setNumber(int contact_number) {
		this.contact_number = contact_number;
	}
}