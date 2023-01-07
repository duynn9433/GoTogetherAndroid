package duynn.gotogether.data_layer.model.dto.response;

import duynn.gotogether.data_layer.model.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListCommentResponse {
    private static final long serialVersionUID = 1L;
    private List<Comment> commentDTOS;
    private String status;
    private String message;
}
