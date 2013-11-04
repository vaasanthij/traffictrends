package org.ttumtd.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BTStation 
{
	private Set<Integer> sectorsIBelongTo = new HashSet<Integer>();
	private Coordinate myLoc;
	private String uniqueId;
	
	private static  List<BTStation> currentBTSs = new ArrayList<BTStation>();
	
	public static List<BTStation> getAllBTSs () {
		if (currentBTSs == null || currentBTSs.isEmpty()) {
		    Collections.addAll(currentBTSs, 	
				new BTStation (new Coordinate (12.992065, 77.602978)),
				new BTStation (new Coordinate (12.992327, 77.6007150)),
				new BTStation (new Coordinate (12.994804, 77.599395)),
				new BTStation (new Coordinate (12.994407, 77.604116)),
				new BTStation (new Coordinate (12.994093, 77.605135)), 
				new BTStation (new Coordinate (12.995892, 77.601455)), 
				new BTStation (new Coordinate (12.999446, 77.600178)),
				new BTStation (new Coordinate (13.002760, 77.598429)),
				new BTStation (new Coordinate (13.005154, 77.594320)),
				new BTStation (new Coordinate (12.995818, 77.596187)),
				new BTStation (new Coordinate (13.000700, 77.594975)),
				new BTStation (new Coordinate (13.009868, 77.594393)), 
				new BTStation (new Coordinate (13.018628, 77.596689)),
				new BTStation (new Coordinate (13.022015, 77.600852)),
				new BTStation (new Coordinate (13.008656, 77.592351)),
				new BTStation (new Coordinate (13.013255, 77.587674)),
				new BTStation (new Coordinate (13.014259, 77.579030)),
				new BTStation (new Coordinate (13.024168, 77.585210)),
				new BTStation (new Coordinate (13.016684, 77.584455)),
				new BTStation (new Coordinate (13.024293, 77.583983)));
		}
		return currentBTSs;
	}
	
	public BTStation (Coordinate btsLoc)
	{
		myLoc = btsLoc;
	}
	
	public Coordinate getLoc ()
	{
		return myLoc;
	}
	public List<String> getLogsForTimestamp (long fromTimestamp,
			                                 long toTimestamp) {
		List<String> logs = new ArrayList<String>();
		// query hadoop
		return logs;
	}
	
	public void addSelfToSector (int sectorID) {
		sectorsIBelongTo.add(sectorID);
	}
	
	public String getUniqueID ()
	{
		return uniqueId;
	}
}
