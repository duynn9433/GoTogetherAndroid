package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientTripDTO implements Serializable {
    private static final long serialVersionUID = 10L;

    private Long id;

    private PositionDTO pickUpPosition;

    @SerializedName("pickup_place")
    private PlaceDTO pickUpPlace;

    private PositionDTO dropOffPosition;

    @SerializedName("dropoff_place")
    private PlaceDTO dropOffplace;

    @SerializedName("pickup_time")
    private Date pickUpTime;

    @SerializedName("number_of_people")
    private Integer numOfPeople;

    @SerializedName("price_per_km")
    private Double pricePerKmForOnePeople;

    @SerializedName("is_accepted")
    private boolean isAccepted;

    @SerializedName("is_canceled")
    private boolean isCanceled;

    private ClientDTO client;

    private TripDTO trip;

}
