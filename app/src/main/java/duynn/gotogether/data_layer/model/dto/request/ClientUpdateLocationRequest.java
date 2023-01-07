package duynn.gotogether.data_layer.model.dto.request;

import duynn.gotogether.data_layer.model.dto.execute_trip.ClientLocationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateLocationRequest implements Serializable {
    public static final long serialVersionUID = 1L;
    private ClientLocationDTO location;
    //for driver request
    private List<Long> passengerIDs;
    //for client request
    private Long driverId;
}
