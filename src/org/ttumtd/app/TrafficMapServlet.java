package org.ttumtd.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TrafficMapServlet
 */
public class TrafficMapServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrafficMapServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
		    // Set the attribute and Forward to hello.jsp
		    //getServletConfig().getServletContext().getRequestDispatcher("/jsp/map_drawwaypt2.html").forward(request, response);
		    
		    //StringBuilder sbr = new StringBuilder();
		    /*sbr.append("From Lat Lng:").append(request.getParameter("fromLatLng")).append("\n")
		    .append("To Lat Lng:").append(request.getParameter("toLatLng")).append("\n")
		    .append("Start time:").append(getDate(request,"from")).append("\n")
		    .append("End Time:").append(getDate(request,"to")).append("\n");   */
	        /*Map paramMap = request.getParameterMap();
	        System.out.println(paramMap);
	        for (Object param: paramMap.keySet()) {
	            sbr.append(param).append("-->").append(request.getParameter((String)param)).append("\n");
	        }

	        PrintWriter out = response.getWriter();
	        out.println(sbr.toString());*/
	        SectorMapper sectorMap = new SectorMapper (request.getParameter("mapbounds"), 
	    		                         Double.parseDouble(request.getParameter("mapsize")));
	        List<Path> pathsAlongRoute = 
	        		Path.breakIntoMultiplePaths(request.getParameter("latlangpath"));
	        Map<Path, Integer> weightedPath = 
	        		PathUsageCalculator.calculateWeightedPath(pathsAlongRoute,
	        				                                  sectorMap,
	        		                                          getTimestamp(request, "to"), 
	        		                                          getTimestamp(request, "from"));
	       
	        // response.setContentType("application/json");
	        // Get the printwriter object from response to write the required json object to the output stream      
	        PrintWriter out = response.getWriter();  
	        out.print(new HTMLGenerator().getMapHTML(weightedPath, 
	        		           sectorMap.getSouthWestBound(), 
	        		           sectorMap.getNorthEastBound()));
	     
        } catch (Exception ex) {
		       ex.printStackTrace ();
	    }
	}
	
	private static final String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	
    private long getTimestamp (HttpServletRequest request, String datePrefix) {
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd kk:mm yyyy");
        
         String month = request.getParameter(datePrefix + "Month");
         String day = request.getParameter(datePrefix + "Date");
         String year = request.getParameter(datePrefix + "Year");
         String hr = request.getParameter(datePrefix + "Hr");
         String min = request.getParameter(datePrefix + "Min");
         Date d;
         try {
             d = sdf.parse(months[Integer.parseInt(month)] + " " + day + " " + hr + ":" + min + " " + year);
         }
         catch (Exception e) {
             d = null;
         }
         if (d != null) {
        	 return d.getTime();
         }
         
         return 0;
    }
    
    private String createJson (Map<Path, Integer> weightedPath) 
    {
    	StringBuilder sbr = new StringBuilder();
    	sbr.append("{\"paths\":[");
    	Iterator<Path> itr = weightedPath.keySet().iterator();
    	while (itr.hasNext()) {
    		Path path = itr.next();
    		sbr.append("{");
    		sbr.append("\"start\":{\"lat\":" + path.getStart().getLat() + "," + "\"lng\":" + path.getStart().getLng() +"},");
    		sbr.append("\"end\":{\"lat\":" + path.getEnd().getLat() + "," + "\"lng\":" + path.getEnd().getLng() +"},");
    		sbr.append("\"weight\":" + weightedPath.get(path));
    		sbr.append("}");
    		if (itr.hasNext()) {
    			sbr.append(",");
    		}
    	}
    	sbr.append("]}");
    	return sbr.toString();
    }

}
