package duynn.gotogether.data_layer.model.dto.client_trip_dto;

import com.google.gson.annotations.SerializedName;
import duynn.gotogether.data_layer.model.model.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientTripDTO implements Serializable {
    private static final long serialVersionUID = 10L;
    private Long id;
    @SerializedName("pickup_place")
    private Place pickUpPlace;

    @SerializedName("dropoff_place")
    private Place dropOffPlace;

    @SerializedName("pickup_time")
    private Calendar pickUpTime;

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

    @SerializedName("distance")
    private Double distance;

    @SerializedName("is_driver_commentted")
    private Boolean isDriverCommentted;

    @SerializedName("is_passenger_commentted")
    private Boolean isPassengerCommentted;

}
