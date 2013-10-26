package org.ttumtd.app;

import java.util.Map;

public class HTMLGenerator 
{
	private String[] colours = {
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
	+	"height: 100%;"
	+	"width: 85%;"
	+	"float: left"
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
	+	"<div id=\"map-canvas\"></div>"
    +  "</body>"
    + "</html>";

	public String getMapHTML (Map<Path, Integer> weightedPath,
			                  Coordinate southwest,
			                  Coordinate northeast) {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for (Path path: weightedPath.keySet()) {
			sb.append("var service = new google.maps.DirectionsService(),polylinePath" + i + ",snap_path" + i + "=[]; ");
			sb.append("var pathCoordinates" + i + " = [ ");
			sb.append("new google.maps.LatLng(" + path.getStart().getLat() + ", " + path.getStart().getLng() + "), ");
			sb.append("new google.maps.LatLng(" + path.getEnd().getLat() + ", " + path.getEnd().getLng() +")]; ");
			sb.append("var polylinePath" + i + " = new google.maps.Polyline({ ");
			sb.append("path: pathCoordinates" + i +", ");
			sb.append("geodesic: true, ");
			sb.append("strokeColor: '#" + colours[weightedPath.get(path)]+ "', ");
			sb.append("strokeOpacity: 1.0, ");
			sb.append("strokeWeight: " + (int)((weightedPath.get(path) + 2) / 2) + " ");	
			sb.append("}); ");
			sb.append("polylinePath" + i +".setMap(map); ");
			sb.append("service.route({origin: " + "new google.maps.LatLng(" + path.getStart().getLat() + ", " + path.getStart().getLng() + ")" + 
			   ",destination: " + "new google.maps.LatLng(" + path.getEnd().getLat() + ", " + path.getEnd().getLng() +")" +
					",travelMode: google.maps.DirectionsTravelMode.DRIVING},function(result, status) { " +               
                "if(status == google.maps.DirectionsStatus.OK) { " +                
                      "snap_path" + i + " = snap_path" + i + ".concat(result.routes[0].overview_path); " +
                      "polylinePath" + i + ".setPath(snap_path" + i + ");" +
                "}});");
			i++;
		}
		sb.append("var bounds = new google.maps.LatLngBounds(");
		sb.append("new google.maps.LatLng(" + southwest.getLat() + ", " + southwest.getLng() + "), ");
		sb.append("new google.maps.LatLng(" + northeast.getLat() + ", " + northeast.getLng() +"));");
		sb.append("map.fitbounds(bounds);");
	 	String html = mapHTML.replace("##DRAW POLYLINES##", sb.toString());
	 	return html;
	}
}
