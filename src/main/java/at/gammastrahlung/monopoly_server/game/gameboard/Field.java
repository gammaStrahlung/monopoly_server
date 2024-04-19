package at.gammastrahlung.monopoly_server.game.gameboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Field {
    private int fieldId;
    private String name;
    private FieldType type;
}
