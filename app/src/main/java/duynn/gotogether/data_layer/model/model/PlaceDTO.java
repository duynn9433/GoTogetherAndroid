
package duynn.gotogether.data_layer.model.model;

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
public class PlaceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @SerializedName("place_id")
    private String placeID;
    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("geometry")
    private GeometryDTO geometry;
    @SerializedName("name")
    private String name;

}
