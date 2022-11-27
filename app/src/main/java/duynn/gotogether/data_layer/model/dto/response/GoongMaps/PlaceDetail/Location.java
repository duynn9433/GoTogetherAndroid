package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}

