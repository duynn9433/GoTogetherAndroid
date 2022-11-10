package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    private static final long serialVersionUID = 11L;

    private Long id;

    private String content;

    private Integer rating;

    @SerializedName("author")
    private Client author;

    @SerializedName("receiver")
    private Client receiver;

    private Trip trip;

}
