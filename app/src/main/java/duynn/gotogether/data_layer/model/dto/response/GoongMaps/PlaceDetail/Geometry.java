package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("location")
    private Location location;

    public Location getLocation() { return location; }
    public void setLocation(Location value) { this.location = value; }

    @Override
    public String toString() {
        return "Geometry{" +
                "location=" + location +
                '}';
    }
}
