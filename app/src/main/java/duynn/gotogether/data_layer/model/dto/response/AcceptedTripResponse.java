package duynn.gotogether.data_layer.model.dto.response;

import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptedTripResponse {
    public static final Long serialVersionUID = 1L;

    private Trip trip;
    private ClientTrip clientTrip;
    private String status;
    private String message;
}
