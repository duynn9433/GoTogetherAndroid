package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO implements Serializable {
    private static final long serialVersionUID = 9L;

    private Long id;

    private PositionDTO startPosition;

    @SerializedName("start_place")
    private PlaceDTO startPlace;

    private PositionDTO endPosition;
    @SerializedName("end_place")
    private PlaceDTO endPlace;

    @SerializedName("stop_places")
    private List<PlaceDTO> listStopPlace;

    @SerializedName("start_time")
    private Date startTime;

    @SerializedName("total_seat")
    private Integer totalSeat;

    @SerializedName("empty_seat")
    private Integer emptySeat;

    @SerializedName("price_per_km")
    private Double pricePerKm;

    @SerializedName("description")
    private String description;

    @SerializedName("distance_plus")
    private Double distancePlus;

    @SerializedName("transport")
    private TransportDTO transport;

    @SerializedName("is_finished")
    private boolean isFinished;

    @SerializedName("is_started")
    private boolean isStarted;

    @SerializedName("is_canceled")
    private boolean isCanceled;

    @SerializedName("driver")
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private ClientDTO driver;

}
