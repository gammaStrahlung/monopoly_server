package at.gammastrahlung.monopoly_server.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Field {
    private int fieldId;
    private String name;
    private FieldType type;
}
