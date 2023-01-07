package duynn.gotogether.data_layer.model.dto.response;

import duynn.gotogether.data_layer.model.model.ClientTrip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListClientTripResponse {
    private static final long serialVersionUID = 1L;

    private String message;
    private String status;
    private List<ClientTrip> data;
}
