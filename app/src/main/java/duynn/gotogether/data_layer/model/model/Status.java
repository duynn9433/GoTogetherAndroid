package duynn.gotogether.data_layer.model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status implements Serializable {
    public static final long serialVersionUID = 1L;
    private String status;
    private String message;
}
