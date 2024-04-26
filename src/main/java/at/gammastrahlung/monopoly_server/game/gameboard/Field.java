package at.gammastrahlung.monopoly_server.game.gameboard;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder

public class Field {
    @Expose
    private int fieldId;
    @Expose
    private String name;
    @Expose
    private FieldType type;
}
