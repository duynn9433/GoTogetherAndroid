package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User implements Serializable {
    private static final long serialVersionUID = 5L;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    private Double rate;
    @SerializedName("fcm_token")
    private String fcmToken;
    @SerializedName("is_in_trip")
    private boolean isInTrip;

    private List<TransportWithoutOwner> transports;

    public String getFullNameString() {
        return getFullname().getFirstName() + " "
                + getFullname().getMiddleName() + " "
                + getFullname().getLastName() ;
    }
}
