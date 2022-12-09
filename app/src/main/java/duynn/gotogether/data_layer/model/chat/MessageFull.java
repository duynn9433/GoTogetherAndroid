package duynn.gotogether.data_layer.model.chat;

import androidx.room.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageFull implements Serializable {
    private static final long serialVersionUID = 1L;
    @Embedded
    private Message message;
    @Relation(
            parentColumn = "sender_id",
            entityColumn = "id"
    )
    private Client sender;
    @Relation(
            parentColumn = "receiver_id",
            entityColumn = "id"
    )
    private Client receiver;
}
