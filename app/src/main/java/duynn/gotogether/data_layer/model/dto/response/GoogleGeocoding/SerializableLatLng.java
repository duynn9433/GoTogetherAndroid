package duynn.gotogether.data_layer.model.dto.response.GoogleGeocoding;

import java.io.Serializable;

public class SerializableLatLng implements Serializable {
    public static final long serialVersionUID = 1L;

    public double latitude;
    public double longitude;

    public double getLat() { return latitude; }
    public void setLat(double value) { this.latitude = value; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double value) { this.longitude = value; }

    @Override
    public String toString() {
        return "SerializableLatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
