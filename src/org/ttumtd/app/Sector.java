package org.ttumtd.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Sector 
{
	private Map<Integer, Coordinate> bounds = new HashMap<Integer, Coordinate>();
	private int sectorId;
	private double lengthOfDiagonal;
	private Set<BTStation> allBTSsInMe = new HashSet<BTStation>();
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
	
	public int distanceFromLine (Coordinate point,
			                         Coordinate x,
			                         Coordinate y)
	{
		double slope = (x.getLng() - y.getLng())/(x.getLat() - y.getLat());
		double angle = Math.abs(Math.atan(slope));
		
		return 0;
	}
	
	public boolean isBTSinMe (Coordinate btsCentre, int radius)
	{
		// There are 3 considerations here
		// 1. Check if the center is with the sector - then this sector covers the BTS
		
		if (isPointInMe(btsCentre)) {
			return true;
		}
		// 2. The distance from any of the vertices to the center is less than that of
		//    the radius of the BTS
		if (btsCentre.getDistanceFromPoint(bounds.get(north)) <= radius ||
				btsCentre.getDistanceFromPoint(bounds.get(south)) <= radius ||
				btsCentre.getDistanceFromPoint(bounds.get(east)) <= radius ||
				btsCentre.getDistanceFromPoint(bounds.get(west)) <= radius) {
			return true;
			
		}
		// 3. The distance of the center to any one of the edges is less than the radius
		//    of the range
		 /*if (distanceFromLine(btsCentre,bounds.get(north), bounds.get(south)) <= radius ||
				distanceFromLine(btsCentre,bounds.get(north), bounds.get(south)) <= radius ||
				distanceFromLine(btsCentre,bounds.get(north), bounds.get(south)) <= radius ||
				distanceFromLine(btsCentre,bounds.get(north), bounds.get(south)) <= radius) {
			
		}*/
		return false;
	}
	
	public void addBTS (BTStation bts) 
	{
		allBTSsInMe.add(bts);
		bts.addSelfToSector(sectorId);
	}
	
	public Set<BTStation> allBTSsInMe ()
	{
		return allBTSsInMe;
	}

}
