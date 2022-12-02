package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientTrip implements Serializable {
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

    @SerializedName("distance")
    private Double distance;

    @SerializedName("is_accepted")
    private boolean isAccepted;

    @SerializedName("is_canceled")
    private boolean isCanceled;

    private Client client;

    private Trip trip;

}
