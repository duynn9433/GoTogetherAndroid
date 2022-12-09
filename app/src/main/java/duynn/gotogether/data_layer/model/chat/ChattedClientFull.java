package duynn.gotogether.data_layer.model.chat;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattedClientFull implements Serializable {
    private static final long serialVersionUID = 1L;

    @Embedded
    private ChattedClient chattedClient;
    @Relation(
            parentColumn = "client",
            entityColumn = "id"
    )
    private Client client;
}
