package duynn.gotogether.data_layer.model.dto.response.Authen;

import duynn.gotogether.data_layer.model.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRes {
    private String status;
    private String message;
}
