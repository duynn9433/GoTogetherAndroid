package duynn.gotogether.data_layer.model.dto.execute_trip;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientLocationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public LatLng toLatLng() {
        return new LatLng(lat, lng);
    }
}
