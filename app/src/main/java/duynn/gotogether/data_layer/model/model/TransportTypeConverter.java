package duynn.gotogether.data_layer.model.model;

import java.util.Objects;
import java.util.stream.Stream;

//https://www.baeldung.com/jpa-persisting-enums-in-jpa
//@Converter
public class TransportTypeConverter{
//    @Override
    public String convertToDatabaseColumn(TransportType transportType) {
        if (transportType == null) {
            return null;
        }
        return transportType.getCode();
    }

//    @Override
    public TransportType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(TransportType.values())
                .filter(c -> Objects.equals(c.getCode(), code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
