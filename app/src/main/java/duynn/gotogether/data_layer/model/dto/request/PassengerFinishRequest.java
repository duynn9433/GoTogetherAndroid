package duynn.gotogether.data_layer.model.dto.request;

import duynn.gotogether.data_layer.model.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerFinishRequest implements Serializable {
    public static final long serialVersionUID = 10L;
    private Comment comment;
}
