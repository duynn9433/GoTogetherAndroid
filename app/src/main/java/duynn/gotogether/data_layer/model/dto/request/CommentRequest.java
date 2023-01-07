package duynn.gotogether.data_layer.model.dto.request;


import duynn.gotogether.data_layer.model.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private static final long serialVersionUID = 1L;
    private Comment commentDTO;
}
