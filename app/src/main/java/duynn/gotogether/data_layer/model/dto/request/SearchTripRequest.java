package duynn.gotogether.data_layer.model.dto.request;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchTripRequest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Place startPlace;
    private Place endPlace;
    private Calendar startTime;
    private Integer numOfSeat;
}
