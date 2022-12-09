
package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
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
