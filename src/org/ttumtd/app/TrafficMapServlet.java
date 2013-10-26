package org.ttumtd.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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
	    		                         Integer.parseInt(request.getParameter("mapsize")));
	        List<Path> pathsAlongRoute = 
	        		Path.breakIntoMultiplePaths(request.getParameter("latlangpath"));
	        Map<Path, Integer> weightedPath = 
	        		PathUsageCalculator.calculateWeightedPath(pathsAlongRoute, 
	        		                                          getTimestamp(request, "to"), 
	        		                                          getTimestamp(request, "from"));
	       
	        response.setContentType("application/json");
	        // Get the printwriter object from response to write the required json object to the output stream      
	        PrintWriter out = response.getWriter();  
	        out.print(new Gson().toJson(weightedPath));
	        out.flush();
	     
        } catch (Exception ex) {
		       ex.printStackTrace ();
	    }
	}
	
    private long getTimestamp (HttpServletRequest request, String datePrefix) {
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd kk:mm yyyy");
        
         String month = request.getParameter(datePrefix + "Month");
         String day = request.getParameter(datePrefix + "Day");
         String year = request.getParameter(datePrefix + "Year");
         String hr = request.getParameter(datePrefix + "Hr");
         String min = request.getParameter(datePrefix + "Min");
         Date d;
         try {
             d = sdf.parse(month + " " + day + " " + hr + ":" + min + " " + year);
         }
         catch (Exception e) {
             d = null;
         }
         if (d != null) {
        	 return d.getTime();
         }
         
         return 0;
    }
    

}
