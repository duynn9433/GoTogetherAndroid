package duynn.gotogether.data_layer.model.chat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import duynn.gotogether.data_layer.model.model.Client;
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
@Entity(tableName = "message")
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name="content")
    private String content;
    @ColumnInfo(name="sender_id")
    private Long senderId;
    @ColumnInfo(name="receiver_id")
    private Long receiverId;
    @ColumnInfo(name="created_at")
    private Long createdAt;
    @ColumnInfo(name="updated_at")
    private Long updatedAt;
}
