package duynn.gotogether.data_layer.model.dto.client_trip_dto;

import com.google.gson.annotations.SerializedName;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
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
public class ClientDTO extends UserDTO implements Serializable {
    private static final long serialVersionUID = 5L;
    private Location location;
    @SerializedName("fcm_token")
    private String fcmToken;

    @SerializedName("is_in_trip")
    private boolean isInTrip;
}

