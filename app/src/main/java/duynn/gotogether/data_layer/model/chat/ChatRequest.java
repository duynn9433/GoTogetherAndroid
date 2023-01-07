package duynn.gotogether.data_layer.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Client sender;
    private Client receiver;
    private Message message;
}
