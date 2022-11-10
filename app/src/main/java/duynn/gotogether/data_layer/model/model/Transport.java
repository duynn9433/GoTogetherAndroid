package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Transport implements Serializable {
    private static final long serialVersionUID = 7L;

    private Long id;

    private String name;

    @SerializedName("license_plate")
    private String licensePlate;

    private String description;

    private String image;

    @SerializedName("transport_type")
    private TransportType transportType;

    @SerializedName("owner")
//    @JsonBackReference
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ClientDTO.class)
    private Client owner;

}
