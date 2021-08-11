package starwars.model.classes;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class Character {
    private String characterId;
    private String name;
    private String homeworld;
    private ArrayList<String> films;
}