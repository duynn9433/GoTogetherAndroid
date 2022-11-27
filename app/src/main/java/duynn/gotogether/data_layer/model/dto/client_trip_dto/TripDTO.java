package duynn.gotogether.data_layer.model.dto.client_trip_dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO implements Serializable {
    private static final long serialVersionUID = 9L;
    private Long id;

    @SerializedName("fcm_topic")
    private String fcmTopic;
}
