package duynn.gotogether.data_layer.model.model;

import com.google.firebase.encoders.annotations.Encodable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransportWithoutOwner implements Serializable {
    private static final long serialVersionUID = 7L;

    private Long id;

    private String name;

    @SerializedName("license_plate")
    private String licensePlate;

    private String description;

    private String image;

    @SerializedName("transport_type")
    private TransportType transportType;

    public TransportWithoutOwner(Transport transport) {
        this.id = transport.getId();
        this.name = transport.getName();
        this.licensePlate = transport.getLicensePlate();
        this.description = transport.getDescription();
        this.image = transport.getImage();
        this.transportType = transport.getTransportType();
    }
}
