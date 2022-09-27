package duynn.gotogether.data_layer.model.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends UserDTO implements Serializable {
    private static final long serialVersionUID = 5L;
    private PositionDTO position;
    private LocationDTO location;

    private Double rate;

    @SerializedName("is_in_trip")
    private boolean isInTrip;

//    @JsonManagedReference
    private List<TransportDTO> transports;

    @Override
    public String toString() {
        String s = super.toString() + "ClientDTO{" +
                "position=" + position +
                ", location=" + location +
                ", rate=" + rate +
                ", isInTrip=" + isInTrip +
                ", transports=" + transports +
                '}';
        return s;
    }
}
