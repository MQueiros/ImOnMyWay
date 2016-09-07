package com.example.imonmyway;

import java.io.Serializable;

import android.location.Location;

public class LocationsStruct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name, info;
	private Location coordinates;

	public LocationsStruct() {
	}

	public LocationsStruct(String name, Location coordinates, String info) {
		this.name = name;
		this.coordinates = coordinates;
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public Location getCoordinates() {
		return coordinates;
	}

	public String getInfo() {
		return info;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(Location coordinates) {
		this.coordinates = coordinates;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}