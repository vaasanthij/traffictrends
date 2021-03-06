package org.ttumtd.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SectorMapper 
{
    private Map<Integer, Sector> sectors = new HashMap<Integer, Sector>();
    private Pattern boundsPattern = 
    		Pattern.compile("..([0-9]+.[0-9]+,\\s[0-9]+.[0-9]+).,\\s.([0-9]+.[0-9]+,\\s[0-9]+.[0-9]+)..");

    
    private Coordinate northeast, southwest;
    private double scaleMultiplier, inverseScaleMultiplier;
    private int sectorSide = 200;
    
    
	public SectorMapper (String bounds, double span)
	        
	{
	     Matcher boundsPatternMatch = boundsPattern.matcher(bounds);
	     if (boundsPatternMatch.matches()) {
	         southwest = new Coordinate(boundsPatternMatch.group(1), ", ");
	         northeast = new Coordinate(boundsPatternMatch.group(2), ", ");
	     }
	     double distUnitsBetweenCornerCoodinates = northeast.getDistanceFromPoint(southwest);
	     scaleMultiplier = distUnitsBetweenCornerCoodinates /span;   
	     inverseScaleMultiplier = span / distUnitsBetweenCornerCoodinates;
    }

    public void createSectorsAndMapBTSs () {
	     createSectors();
	     mapBTSIntoSectors();
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
		if ((nw.getDistanceFromPoint(ne)) <= sectorSide) {
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
	
	private void mapBTSIntoSectors ()
	{
		List<BTStation> allBTSs = BTStation.getAllBTSs();
		for (BTStation bts: allBTSs) {
			for (Sector sector: sectors.values()) {
				if (sector.isBTSinMe(bts.getLoc(), 400)) {
					sector.addBTS(bts);
				}
			}
		}	
	}
	
	public Sector getSector (int id)
	{
		return sectors.get(id);
	}
	
	public Coordinate getSouthWestBound ()
	{
		return southwest;
	}
	
	public Coordinate getNorthEastBound ()
	{
		return northeast;
	}
	
	public Sector getSectorForPoint (Coordinate point)
	{
		for (Sector sector: sectors.values()) {
			if (sector.isPointInMe(point)) {
				return sector;
			}
		}
		return null;
	}
	
}
