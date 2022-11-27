package duynn.gotogether.domain_layer;

import com.google.android.gms.maps.model.LatLng;
import duynn.gotogether.BuildConfig;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;

import java.util.ArrayList;
import java.util.List;

public class GetDirectionUrlUseCase {
    public static String getStandartDirectionUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "vehicle=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Building the url to the web service
        String url = "https://rsapi.goong.io/Direction?" + parameters + "&api_key=" + BuildConfig.GOONG_API_KEY;
        return url;
    }
    public static String getStandartDirectionUrl(Location origin, Location dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.getLat() + "," + origin.getLng();
        // Destination of route
        String str_dest = "destination=" + dest.getLat() + "," + dest.getLng();
        // Mode
        String mode = "vehicle=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Building the url to the web service
        String url = "https://rsapi.goong.io/Direction?" + parameters + "&api_key=" + BuildConfig.GOONG_API_KEY;
        return url;
    }
    public static String getStandartDirectionUrl(Place origin, Place dest, String directionMode) {
        String res = getStandartDirectionUrl(
                origin.getGeometry().getLocation(),
                dest.getGeometry().getLocation(),
                directionMode);
        return res;
    }
    public static String getMultiStopDirectionUrl(
            Location origin,
            Location dest,
            List<Location> stopList,
            String directionMode){
        StringBuffer sb = new StringBuffer();
        /**endpoint*/
        sb.append("https://rsapi.goong.io/Direction?");
        /**parameter*/
        // ---Origin of route---
        sb.append("origin=").append(origin.getLat()).append(",").append(origin.getLng());
        // ---Destination of route---
        sb.append("&destination=");
        //--stop--
        for (Location stop : stopList) {
            sb.append(stop.getLat()).append(",").append(stop.getLng()).append(";");
        }
        //---destination---
        sb.append(dest.getLat()).append(",").append(dest.getLng());
        // ---Mode---
        sb.append("&vehicle=").append(directionMode);
        //---key---
        sb.append("&api_key=" + BuildConfig.GOONG_API_KEY);
        return sb.toString();
    }
    public static String getMultiStopDirectionUrl(
            Place origin,
            Place dest,
            List<Place> stopList,
            String directionMode){
        List<Location> stopLocationList = new ArrayList<>();
        for (Place stop : stopList) {
            stopLocationList.add(stop.getGeometry().getLocation());
        }
        return getMultiStopDirectionUrl(
                origin.getGeometry().getLocation(),
                dest.getGeometry().getLocation(),
                stopLocationList,
                directionMode);
    }

}
