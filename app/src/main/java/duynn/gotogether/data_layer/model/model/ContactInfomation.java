package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfomation implements Serializable {
    private static final long serialVersionUID = 2L;

    private Long id;

    @SerializedName("phone_number")
    private String phoneNumber;

    private String email;

}
