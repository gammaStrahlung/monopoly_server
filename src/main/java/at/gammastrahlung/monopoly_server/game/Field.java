package at.gammastrahlung.monopoly_server.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuperBuilder

public class Field {
    private int fieldID;
    private String name;

}
