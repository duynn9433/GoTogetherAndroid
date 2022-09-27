package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

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

    private String role;

    private String avatar;

    private String description;

    private AccountDTO account;

    private FullnameDTO fullname;

    @SerializedName("contact_information")
    private ContactInfomationDTO contactInfomation;

    private AddressDTO address;

    private boolean isActive;

}
