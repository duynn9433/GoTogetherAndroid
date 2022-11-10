package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Trip implements Serializable {
    private static final long serialVersionUID = 9L;

    private Long id;

    @SerializedName("start_place")
    private Place startPlace;

    @SerializedName("end_place")
    private Place endPlace;

    @SerializedName("stop_places")
    private List<Place> listStopPlace;

    @SerializedName("start_time")
    
    private Calendar startTime;

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
    private TransportWithoutOwner transport;

    @SerializedName("is_finished")
    private boolean isFinished;

    @SerializedName("is_started")
    private boolean isStarted;

    @SerializedName("is_canceled")
    private boolean isCanceled;

    @SerializedName("driver")
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Client driver;

    public Trip() {
        this.startPlace = new Place();
        this.endPlace = new Place();
        this.listStopPlace = new ArrayList<>();
        this.startTime = Calendar.getInstance();
        this.totalSeat = 0;
        this.emptySeat = 0;
        this.pricePerKm = 0.0;
        this.description = "";
        this.distancePlus = 0.0;
        this.transport = null;
        this.isFinished = false;
        this.isStarted = false;
        this.isCanceled = false;
        this.driver = null;
    }
}
