package duynn.gotogether.data_layer.model.dto.execute_trip;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateLocationRequest implements Serializable {
    public static final long serialVersionUID = 1L;

    private Location location;
    private Long tripId;
    private Long clientId;
}
