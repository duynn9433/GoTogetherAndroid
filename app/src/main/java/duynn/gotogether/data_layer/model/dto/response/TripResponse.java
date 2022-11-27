package duynn.gotogether.data_layer.model.dto.response;

import duynn.gotogether.data_layer.model.model.Trip;
import lombok.Data;

@Data
public class TripResponse {
    public static final Long serialVersionUID = 1L;

    private Trip trip;
    private String status;
    private String message;
}
