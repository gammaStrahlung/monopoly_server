package at.gammastrahlung.monopoly_server.game;

import lombok.*;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Property extends Field{
    private int price;
    private Player owner;
    private PropertyColor color;
}
