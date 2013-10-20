package org.ttumtd.app;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SectorMapper 
{
    private Map<Integer, Sector> sectors = new HashMap<Integer, Sector>();
    private Pattern boundsPattern = 
    		Pattern.compile("[(][(](\\d+.\\d+,\\d+.\\d+),"
    				+ "(\\d+.\\d+,\\d+.\\d+)[)][)]");
    private Coordinate northeast, southwest;
    private double scaleMultiplier, inverseScaleMultiplier;
    private int sectorSide = 200;
    
    
	public SectorMapper (String bounds, double span)
	        
	{
	     Matcher boundsPatternMatch = boundsPattern.matcher(bounds);
	     southwest = new Coordinate(boundsPatternMatch.group(1), ",");
	     northeast = new Coordinate(boundsPatternMatch.group(2), ",");
	     double distUnitsBetweenCornerCoodinates = northeast.getDistanceFromPoint(southwest);
	     scaleMultiplier = distUnitsBetweenCornerCoodinates /span;   
	     inverseScaleMultiplier = span / distUnitsBetweenCornerCoodinates;
	     createSectors();
	}
	
	private void createSectors ()
	{
		Coordinate northwest = new Coordinate(southwest.getLat(), northeast.getLng());
		Coordinate southeast = new Coordinate(northeast.getLat(), southwest.getLng());
		divideAreaByFour(northwest, northeast, southwest, southeast, 0x1);
	}
	
	private void divideAreaByFour (Coordinate nw, 
			                       Coordinate ne,
			                       Coordinate sw,
			                       Coordinate se,
			                       int id) 
	{
		if ((nw.getDistanceFromPoint(ne) * inverseScaleMultiplier) <= sectorSide) {
			Sector sector = new Sector(id, nw, ne, se, sw);
			sectors.put(id, sector);
			return;
		}
		Coordinate nCentre = new Coordinate((ne.getLat() - nw.getLat())/2, ne.getLng());
		Coordinate eCentre = new Coordinate(ne.getLat(), (ne.getLng() - se.getLng())/2);
		Coordinate sCentre = new Coordinate((se.getLat() - sw.getLat())/2, se.getLng());
		Coordinate wCentre = new Coordinate(nw.getLat(), (sw.getLng() - nw.getLng())/2);
		Coordinate centre = new Coordinate (nCentre.getLat(), eCentre.getLng());
		int newID = id<<2;
		divideAreaByFour(nw, nCentre, wCentre, centre, newID|00);
		divideAreaByFour(nCentre, ne, centre, eCentre, newID|01);
		divideAreaByFour(wCentre, centre, sw, sCentre, newID|10);
		divideAreaByFour(centre, eCentre, sCentre, se, newID|11);		
	}
	
	public Sector getSector (int id)
	{
		return sectors.get(id);
	}
}
