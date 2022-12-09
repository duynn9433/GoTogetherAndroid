package duynn.gotogether.data_layer.model.chat;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.*;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "client")
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    @PrimaryKey
    private Long id;
    private String name;
    private String phone;
}
