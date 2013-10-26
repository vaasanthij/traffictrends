package org.ttumtd.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathUsageCalculator {
	
	public PathUsageCalculator ()
	{
		
	}
	
	public static Map<Path, Integer> calculateWeightedPath (List<Path> paths,
			                             SectorMapper sectorMap,
			                             long startTimestamp,
			                             long endTimestamp) 
    {
		Map<Path, Integer> weightedPath = new HashMap<Path, Integer>();
		// for each path query hadoop and get it
		for (Path path: paths) {
		    // for now insert and create a dummy weight
			weightedPath.put(path, (int)((Math.random() * 100) % 13));
		}
		return weightedPath;
	}
			                          

}
