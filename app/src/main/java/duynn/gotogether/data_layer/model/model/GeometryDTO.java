package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeometryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @SerializedName("location")
    private LocationDTO location;

    @Override
    public String toString() {
        return "Geometry{" +
                "id=" + id +
                ", location=" + location +
                '}';
    }
}
