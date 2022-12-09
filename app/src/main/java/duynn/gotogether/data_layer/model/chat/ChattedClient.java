package duynn.gotogether.data_layer.model.chat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(tableName = "chatted_client")
public class ChattedClient implements Serializable {
    private static final long serialVersionUID = 1L;
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "client")
    private Long clientId;
    @ColumnInfo(name = "owner_id")
    private Long ownerId;
}
