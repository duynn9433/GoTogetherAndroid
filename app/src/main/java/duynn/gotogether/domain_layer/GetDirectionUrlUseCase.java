package duynn.gotogether.domain_layer;

import com.google.android.gms.maps.model.LatLng;
import duynn.gotogether.BuildConfig;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.model.model.Place;

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
                new LatLng(origin.getLat(), origin.getLng()),
                new LatLng(dest.getLat(), dest.getLng()),
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
            LatLng origin,
            LatLng dest,
            List<LatLng> stopList,
            String directionMode){
        StringBuffer sb = new StringBuffer();
        /**endpoint*/
        sb.append("https://rsapi.goong.io/Direction?");
        /**parameter*/
        // ---Origin of route---
        sb.append("origin=").append(origin.latitude).append(",").append(origin.longitude);
        // ---Destination of route---
        sb.append("&destination=");
        //--stop--
        for (LatLng stop : stopList) {
            sb.append(stop.latitude).append(",").append(stop.longitude).append(";");
        }
        //---destination---
        sb.append(dest.latitude).append(",").append(dest.longitude);
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
        List<LatLng> stopLocationList = new ArrayList<>();
        for (Place stop : stopList) {
            stopLocationList.add(new LatLng(stop.getLat(), stop.getLng()));
        }
        return getMultiStopDirectionUrl(
                new LatLng(origin.getLat(), origin.getLng()),
                new LatLng(dest.getLat(), dest.getLng()),
                stopLocationList,
                directionMode);
    }

}
