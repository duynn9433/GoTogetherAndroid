package duynn.gotogether.data_layer.model.dto.response.Authen;

import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResp implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private Integer status;
    private String jwttoken;
    private Client client;

    private String error;
}