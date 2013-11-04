package org.ttumtd.app;

import org.ttumtd.connector.SimpleHBaseClusterConnector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            //new SimpleHBaseClusterConnector().getInfoPerBTSID("","","","");
		}
		return weightedPath;
	}
	
	
	public static Map<Path, Integer> calculateWeightForEachPath (
			                            List<Path> paths,
			                            SectorMapper sectorMap,
			                            String startDate,
			                            String endDate,
			                            String startTime,
			                            String endTime)
	{
		int maxWeight = 0;
		Map<Path, Integer> weightedPath = new HashMap<Path, Integer>();
		for (Path path: paths) {
			// Get the BTSs that serve the start point and the end point
			Set<BTStation> startPointBTSsToQuery = getBTSsToQuery(sectorMap, path.getStart());		
			Set<BTStation> endPointBTSsToQuery = getBTSsToQuery(sectorMap, path.getEnd());
			// Get the list of unique IMSIs registered at this time for each BTS
			Set<String> startPointIMSIs = queryFromConnector(startPointBTSsToQuery, 
					                                          startDate, endDate, startTime, endTime);
			Set<String> endPointIMSIs = queryFromConnector(endPointBTSsToQuery, 
                    startDate, endDate, startTime, endTime);
			Set<String> intersectingIMSIs = intersect(startPointIMSIs, endPointIMSIs);
			int pathWeight = intersectingIMSIs.size();
			if (pathWeight > maxWeight) {
				maxWeight = pathWeight;
			}
			weightedPath.put(path, pathWeight);			
		}	
		recalculateWeightBasedOnMaxWeight(weightedPath, maxWeight);
		return weightedPath;
	}
	
	private static Set<BTStation> getBTSsToQuery (SectorMapper sectorMap, Coordinate point)
	{
		Sector sector = sectorMap.getSectorForPoint(point);
		return sector.allBTSsInMe();
	}
	
	private static Set<String> queryFromConnector (Set<BTStation> BTSs,
			                                       String startDate,
			                                       String endDate,
			                                       String startTime,
			                                       String endTime) 
    {
		Set<String> allIMSIs = new HashSet<String>();
		try {
		    SimpleHBaseClusterConnector connector = new SimpleHBaseClusterConnector();
		    for (BTStation BTS: BTSs) {
		    	allIMSIs.addAll(connector.getInfoPerBTSID(BTS.getUniqueID(), startDate, startTime, endTime));	
		    	allIMSIs.addAll(connector.getInfoPerBTSID(BTS.getUniqueID(), endDate, startTime, endTime));
		    }
		}
		catch (Exception e) {
			// Some error occurred when querying
			e.printStackTrace();
		}	
		return allIMSIs;
	}
	

	private static Set<String> intersect (Set<String> setA, Set<String> setB) 
	{
		Set<String> intersection = new HashSet<String>();
		for (String element: setA) {
			if (setB.contains(element)) {
				intersection.add(element);
			}
		}
		return intersection;	
	}
	private static void recalculateWeightBasedOnMaxWeight (Map<Path, Integer> weightedPath,
			                                               int maxWeight)
	{
		
	}
			                          

}
