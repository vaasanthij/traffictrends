package org.ttumtd.app;

public class Coordinate 
{
	private final double Lat, Lng;
	
	public Coordinate (String latLng, String delimiter)
	{
		String[] tokens = latLng.split(delimiter);
		Lat = Double.parseDouble(tokens[0]);
		Lng = Double.parseDouble(tokens[1]);
	}
	
	public Coordinate (double lat, double lng)
	{
		Lat = lat;
		Lng = lng;
	}
	
	public double getLat ()
	{
		return Lat;
	}
	
	public double getLng ()
	{
		return Lng;
	}
	
	public double getDistanceFromPoint (Coordinate point)
	{
		return Math.sqrt(Math.pow((Lng - point.getLng()), 2) + 
				       Math.pow((Lat - point.getLng()), 2));
	}
	
	public String toString ()
	{
	   return "(" + Lat +"," + Lng + ")";	
	}
}
