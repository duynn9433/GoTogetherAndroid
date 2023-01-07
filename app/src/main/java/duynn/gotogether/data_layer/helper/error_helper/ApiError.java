package duynn.gotogether.data_layer.helper.error_helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
    private int statusCode;
    private String message = "Lỗi không xác định";
    private String debugMessage;
}
