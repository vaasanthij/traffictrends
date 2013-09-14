package org.ttumtd.app;

import java.util.HashMap;
import java.util.Map;

public class Sector 
{
	private Map<Integer, Coordinate> bounds = new HashMap<Integer, Coordinate>();
	private int sectorId;
	private double lengthOfDiagonal;
	public static final int north = 0, east = 1, west = 2, south = 3;
	
	
	public Sector(int id,
			      Coordinate northPoint,
			      Coordinate eastPoint,
			      Coordinate westPoint,
			      Coordinate southPoint) 
	{
		sectorId = id;
		bounds.put(north,northPoint);
		bounds.put(east, eastPoint);
		bounds.put(west, westPoint);
		bounds.put(south, southPoint);
		lengthOfDiagonal = bounds.get(north).getDistanceFromPoint(bounds.get(south));		
	}
	
	public boolean isPointInMe (Coordinate point) 
	{
		for (Integer corner: bounds.keySet()) {
			if (point.getDistanceFromPoint(bounds.get(corner)) > lengthOfDiagonal) {
				return false;
			}
		}
		return true;		
	}
	
	public Map<Integer,Coordinate> getBounds ()
	{
		return bounds;
	}
	
	public int getId ()
	{
		return sectorId;
	}

}
