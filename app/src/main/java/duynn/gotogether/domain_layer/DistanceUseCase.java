package duynn.gotogether.domain_layer;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.List;

public class DistanceUseCase {
    public static double calculateDistanceBetweenTwoPoint(double lat1, double lon1, double lat2, double lon2) {
        //https://en.wikipedia.org/wiki/Haversine_formula
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    public static double calculateDistance(List<LatLng> locationList){
        if(locationList == null || locationList.size() < 2) return 0;
        double distance = 0;
        for(int i = 0; i < locationList.size() - 1; i++){
            LatLng location1 = locationList.get(i);
            LatLng location2 = locationList.get(i + 1);
            distance+= (SphericalUtil.computeDistanceBetween(location1, location2)/1000); //km
//            distance += calculateDistanceBetweenTwoPoint(location1.latitude, location1.longitude, location2.latitude, location2.longitude);
        }

        return distance;
    }

    public static double calculateDistanceKm(List<LatLng> locationList){
        if(locationList.size() < 2) return 0;
        double distance = 0;
        distance = SphericalUtil.computeDistanceBetween(locationList.get(0), locationList.get(locationList.size() - 1));
        return distance / 1000;
    }

    public static String formatToString2digitEndPoint(double distance){
        return new DecimalFormat("#.##").format(distance);
    }
}
