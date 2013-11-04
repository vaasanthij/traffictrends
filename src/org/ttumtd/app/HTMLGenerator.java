package org.ttumtd.app;

import java.util.Map;

public class HTMLGenerator 
{
	/*private String[] colours = {
			"9F8000",
			"9FF000", 
			"FFF000",
			"FF8000",
			"844000",
			"844046",
			"844086",
			"8440FF",
		    "111FFF",
			"111999",
			"960999",
			"DD0088",
			"FF0000"
	};*/
	private String[] colours = {
			"47AB1C",
			"4F6228", 
			"E26B0A",
			"FF471B",
			"FF471B"
	};
	private String mapHTML =  "<!DOCTYPE html>"
	+ "<html>"
	+ "<head>"
	+ "<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" /> "

	+ "<style type=\"text/css\"> "
	+ "html {"
	+   "height: 100%;"
	+   "width: 75%;"
	
	+ "} "

	+ "body {"
	+   "height: 100%;"
	+   "width: 100%;"
	+   "margin: 10;"
	+   "padding: 0"
	+ "} "

	+ "#map-canvas {"
	+	"height: 80%;"
	+	"width: 65%;"
	+	"float: left;"
	+   "padding: 10;"
	+   "border:1px solid #000;"
	+ "} "
	+ "</style>"
	+ "<script "
	+	"src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyBbE4ZuFxcpxe4w7ddJqs3ssNPn2r0cwoA&sensor=false&libraries=places\"></script>"
	+ "<script> "

	+ "var map; "
	+ "function initialize() { "

	+ "var currentLocation = new google.maps.LatLng(13.003638,77.599769); "
	+ "var mapOptions = { "
	+    "zoom: 15, "
	+    "center: currentLocation, "
	+    "scrollwheel: false, "
	+    "scaleControl: false, "
	+    "zoomControl: false, "
	+    "navigationControl: false, "
	+    "mapTypeId: google.maps.MapTypeId.TERRAIN "
	+  "}; "
	+  "map =  new google.maps.Map(document.getElementById(\"map-canvas\"), mapOptions); "

	+    "##DRAW POLYLINES##"  
	+ "} "
	+ "google.maps.event.addDomListener(window, 'load', initialize); "
	+ "</script>"
	+ "</head>"
	+ "<body>"
	+    "<div id=\"header\" style=\"background-color:#FFFFFF;text-align:center;\">"
    +        "<h1 style=\"margin-bottom:5;\"><font color=\"0340CF\" face=\"verdana\">Traffic Trend Analyzer</font></h1>"
    +        "<hr size=\"13\" noshade=\"true\">"
    +    "</div>"
    +    "<div id=\"header\" style=\"background-color:#FFFFFF;text-align:center;\">"
    +         "<h1 style=\"margin-bottom:10;\"><font color=\"6A40FF\" face=\"verdana\"></font></h1>"
    +    "</div>"
    +    "<div id=\"filler\" style=\"background-color:#FFFFFF;height:200px;width:30px;float:left;\"></div>"
	+	"<div id=\"map-canvas\"></div>"
    +   "<div id=\"filler\" style=\"background-color:#FFFFFF;height:200px;width:30px;float:left;\"></div>"
	+   "<div id=\"legend\" style=\"background-color:#FFFFFF;height:20%;width:25%;float:right;\">"
    // +       "<img src=\"file://///Users/Vjagannath/Documents/workspace/traffictrends/WebContent/images/Legend.gif\" height=\"200\" width=\"230\">"
    +       "<img src=\"images/Legend.gif\" height=\"200\" width=\"230\">"
    +   "</div>"
    +  "</body>"
    + "</html>";

	public String getMapHTML (Map<Path, Integer> weightedPath,
			                  Coordinate southwest,
			                  Coordinate northeast) {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		 sb.append("var bounds = new google.maps.LatLngBounds(); ");
		for (Path path: weightedPath.keySet()) {
			sb.append("var service = new google.maps.DirectionsService(),polylinePath" + i + ",snap_path" + i + "=[]; ");
			sb.append("var pathCoordinates" + i + " = [ ");
			sb.append(getLatLngString(path.getStart()) + ", ");
			sb.append(getLatLngString(path.getEnd()) + "]; ");
			sb.append("var polylinePath" + i + " = new google.maps.Polyline({ ");
			sb.append("path: pathCoordinates" + i +", ");
			sb.append("geodesic: true, ");
			sb.append("strokeColor: '#" + colours[(int)((weightedPath.get(path)) % 5)] + "', ");
			sb.append("strokeOpacity: 0.7, ");
			// sb.append("strokeWeight: " + (int)((weightedPath.get(path) % 5) + 6) + " ");	
			sb.append("strokeWeight: " + 12 + " ");	
			sb.append("}); ");
			sb.append("polylinePath" + i +".setMap(map); ");
			sb.append("service.route({origin: " +  getLatLngString(path.getStart()) + 
			   ",destination: " + getLatLngString(path.getEnd()) +
					",travelMode: google.maps.DirectionsTravelMode.DRIVING},function(result, status) { " +               
                "if(status == google.maps.DirectionsStatus.OK) { " +                
                      "snap_path" + i + " = snap_path" + i + ".concat(result.routes[0].overview_path); " +
                      "polylinePath" + i + ".setPath(snap_path" + i + "); " +
                       "var routeBounds = result.routes[0].bounds; " +
                       "bounds = bounds.union({other: routeBounds}); " +
                      
                "}}); ");
			 // sb.append("bounds.extend(" + getLatLngString(path.getStart()) + "); ");
			 // sb.append("bounds.extend(" + getLatLngString(path.getEnd()) + "); ");
			i++;
		}
		/*sb.append("var bounds = new google.maps.LatLngBounds(");
		sb.append("new google.maps.LatLng(" + southwest.getLat() + ", " + southwest.getLng() + "), ");
		sb.append("new google.maps.LatLng(" + northeast.getLat() + ", " + northeast.getLng() +"));");*/
		// sb.append("map.fitBounds(bounds);");
	 	String html = mapHTML.replace("##DRAW POLYLINES##", sb.toString());
	 	return html;
	}
	
	private String getLatLngString (Coordinate point)
	{
		return "new google.maps.LatLng(" + point.getLat() + ", " + point.getLng() + ")";
	}
}
