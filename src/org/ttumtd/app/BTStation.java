package org.ttumtd.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BTStation 
{
	private Set<Integer> sectorsIBelongTo = new HashSet<Integer>();
	private int myId;
	
	public BTStation (int id)
	{
		myId = id;
	}
	
	public int getId ()
	{
		return myId;
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
}
