
package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Place implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("place_id")
    private String placeID;
    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("name")
    private String name;

    public String getPlaceID() { return placeID; }
    public void setPlaceID(String value) { this.placeID = value; }

    public String getFormattedAddress() { return formattedAddress; }
    public void setFormattedAddress(String value) { this.formattedAddress = value; }

    public Geometry getGeometry() { return geometry; }
    public void setGeometry(Geometry value) { this.geometry = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    @Override
    public String toString() {
        return "Result{" +
                "placeID='" + placeID + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", geometry=" + geometry +
                ", name='" + name + '\'' +
                '}';
    }
}
