package duynn.gotogether.config.di;

import lombok.Getter;
import org.modelmapper.ModelMapper;

@Getter
public class ModelMapperConfig {
    private static ModelMapper instance;

    public ModelMapperConfig() {
        instance = new ModelMapper();
        instance.getConfiguration()
                .setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.STRICT);
    }

    public static ModelMapper getInstance() {
        if(instance == null) {
            instance = new ModelMapper();
            instance.getConfiguration()
                    .setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.STRICT);
        }
        return instance;
    }
}
