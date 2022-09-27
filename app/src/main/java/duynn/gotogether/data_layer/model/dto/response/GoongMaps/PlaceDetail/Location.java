package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public double getLat() { return lat; }
    public void setLat(double value) { this.lat = value; }

    public double getLng() { return lng; }
    public void setLng(double value) { this.lng = value; }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}

