package duynn.gotogether.data_layer.model.dto.client_trip_dto;

import com.google.gson.annotations.SerializedName;
import duynn.gotogether.data_layer.model.model.ContactInfomation;
import duynn.gotogether.data_layer.model.model.Fullname;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 4L;

    private Long id;

    private String avatar;

    private Fullname fullname;

    @SerializedName("contact_information")
    private ContactInfomation contactInfomation;

}
