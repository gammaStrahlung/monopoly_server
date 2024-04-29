package at.gammastrahlung.monopoly_server.game.gameboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TaxField extends Field{
    private int toPay;
}
