
package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @SerializedName("place_id")
    private String placeID;
    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("name")
    private String name;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public Place(duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place place) {
        this.placeID = place.getPlaceID();
        this.formattedAddress = place.getFormattedAddress();
        this.name = place.getName();
        this.lat = place.getGeometry().getLocation().getLat();
        this.lng = place.getGeometry().getLocation().getLng();
    }
}
