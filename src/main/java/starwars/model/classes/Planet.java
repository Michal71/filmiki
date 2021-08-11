package starwars.model.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class Planet {
    private String planetId;
    private String name;
    private String url;
}
