package com.claddino.irishnationalparks;

public class ParkCoordinates {

	private double latitude;
	private double longtitude;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public ParkCoordinates(double latitude, double longtitude) {
		super();
		this.latitude = latitude;
		this.longtitude = longtitude;
	}
}
