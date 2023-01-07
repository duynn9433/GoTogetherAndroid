package duynn.gotogether.domain_layer;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.model.model.Place;

import java.util.List;

public class GoogleMapUrlUseCase {
    public static final String GOOGLE_MAP_URL = "https://www.google.com/maps/dir/?api=1&origin=%s&destination=%s&travelmode=%s";
    public static final String GOOGLE_MAP_URL_WITH_WAYPOINTS = "https://www.google.com/maps/dir/?api=1&origin=%s&destination=%s&travelmode=%s&waypoints=%s";

    public static String getGoogleMapUrl(String origin, String destination, String travelMode) {
        return String.format(GOOGLE_MAP_URL, origin, destination, travelMode);
    }

    public static String getGoogleMapUrlWithWaypoints(String origin, String destination, String travelMode, String waypoints) {
        return String.format(GOOGLE_MAP_URL_WITH_WAYPOINTS, origin, destination,travelMode, waypoints);
    }

    public static String getGoogleMapUrlWithWaypoints(Place startPlace, Place endPlace, List<Place> stopPlaces, String travelMode) {
        String origin = startPlace.getLat() + "," + startPlace.getLng();
        String destination = endPlace.getLat() + "," + endPlace.getLng();
        StringBuffer waypoints = new StringBuffer();
        for (Place place : stopPlaces) {
            waypoints.append(place.getLat()).append(",").append(place.getLng()).append("|");
        }
        waypoints.deleteCharAt(waypoints.length() - 1);
        return getGoogleMapUrlWithWaypoints(origin, destination, travelMode, waypoints.toString());
    }
    public static String getGoogleMapUrlWithWaypoints(Location myLocation, Place startPlace, Place endPlace, List<Place> stopPlaces, String travelMode) {
        String origin = myLocation.getLat() + "," + myLocation.getLng();
        String destination = endPlace.getLat() + "," + endPlace.getLng();
        StringBuffer waypoints = new StringBuffer();
        waypoints.append(startPlace.getLat()).append(",").append(startPlace.getLng()).append("|");
        for (Place place : stopPlaces) {
            waypoints.append(place.getLat()).append(",").append(place.getLng()).append("|");
        }
        waypoints.deleteCharAt(waypoints.length() - 1);
        return getGoogleMapUrlWithWaypoints(origin, destination, travelMode, waypoints.toString());
    }

}
