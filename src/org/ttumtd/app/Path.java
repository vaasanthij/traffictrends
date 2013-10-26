package org.ttumtd.app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path 
{
	private Coordinate startPoint;
	private Coordinate endPoint;
	
	public Path (Coordinate start, Coordinate end)
	{
		startPoint = start;
		endPoint = end;
	}
	
	public Coordinate getStart ()
	{
		return startPoint;
	}
	
	public Coordinate getPoint ()
	{
		return endPoint;
	}
	
	public static List<Path> breakIntoMultiplePaths (String latLngPath)
	{
		List<Path> paths = new ArrayList<Path>();	
		String[] pointsAlongTheWay = latLngPath.split(",");
		
		Coordinate startPoint = getPoint(pointsAlongTheWay[0]);
		Coordinate endPoint;
		for (int i=1; i<pointsAlongTheWay.length; i++) {
		    endPoint = getPoint(pointsAlongTheWay[i]);
		    paths.add(new Path(startPoint, endPoint));
		    startPoint = endPoint;
		}
		return paths;		
	}
	
	private static Pattern pointPattern = Pattern.compile("\"([0-9]+.[0-9]+):([0-9]+.[0-9]+)\"");
	private static Coordinate getPoint (String regex)
	{
		Matcher matcher = pointPattern.matcher(regex);
		double lat = Double.parseDouble(matcher.group(1));
		double lng = Double.parseDouble(matcher.group(2));
		return new Coordinate(lat, lng);
	}
}
