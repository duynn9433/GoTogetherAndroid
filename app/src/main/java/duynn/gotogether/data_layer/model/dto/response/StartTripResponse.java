package duynn.gotogether.data_layer.model.dto.response;

import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartTripResponse {
    public static final Long serialVersionUID = 1L;

    private Trip trip;
    private List<ClientTrip> clientTrips;
    private String status;
    private String message;
}
