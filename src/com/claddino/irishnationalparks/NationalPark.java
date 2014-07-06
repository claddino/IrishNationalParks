package com.claddino.irishnationalparks;

public class NationalPark {

	
	private String email;
	private String phone;
	private String info;
	private ParkCoordinates coords;
	public NationalPark(ParkCoordinates coords, String email,
			String phone, String info) {
		super();
		this.coords = coords;
		this.email = email;
		this.phone = phone;
		this.info = info;
	}
	public ParkCoordinates getCoords() {
		return coords;
	}
	public void setCoords(ParkCoordinates coords) {
		this.coords = coords;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public NationalPark() {
		// TODO Auto-generated constructor stub
	}
}
