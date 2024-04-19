package at.gammastrahlung.monopoly_server.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class Utility extends Field{
    private Player owner;
    private int toPay;
}
