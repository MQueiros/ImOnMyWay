package com.example.imonmyway;

import java.io.Serializable;
import java.util.List;

public class ActionsStruct implements Serializable {

	private static final long serialVersionUID = 1L;
	private int action_type; // 0 - message; 1 - call
	private List<Contact> contact_list;
	private String location;
	private int radius = 100;
	private String text;
	private int used = 0; // 0 - not used; 1 - waiting; 2 - finished

	public ActionsStruct() {
	}

	public ActionsStruct(int action_type, List<Contact> contact_list,
			String location, int radius, String text, int used) {
		this.action_type = action_type;
		this.contact_list = contact_list;
		this.location = location;
		this.text = text;
		this.radius = radius;
		this.used = used;
	}

	public int getType() {
		return action_type;
	}

	public List<Contact> getContactList() {
		return contact_list;
	}

	public String getLocation() {
		return location;
	}

	public String getText() {
		return text;
	}

	public int getRadius() {
		return radius;
	}

	public int getUsedState() {
		return used;
	}

	public void setType(int action_type) {
		this.action_type = action_type;
	}

	public void setContactList(List<Contact> contact_list) {
		this.contact_list = contact_list;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setUsedState(int used) {
		this.used = used;
	}

}