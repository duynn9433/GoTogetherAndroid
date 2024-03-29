package duynn.gotogether.data_layer.model.dto.execute_trip;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.model.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse implements Serializable {
    public static final long serialVersionUID = 1L;

    private String message;
    private String status;
    private ClientLocationDTO location;

}
