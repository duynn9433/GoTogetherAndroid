package duynn.gotogether.data_layer.model.model;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripStopPlace implements Serializable {
    private static final long serialVersionUID = 4L;

    private Long id;

    private Integer position;

    private Place place;

    private Trip trip;
}
