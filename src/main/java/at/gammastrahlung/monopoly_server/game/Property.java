package at.gammastrahlung.monopoly_server.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Property extends Field{
    private int price;
    private Player owner;
    private PropertyColor color;

    public Property(int fieldId, String name, FieldType type, int price, Player owner, PropertyColor color) {
        super(fieldId, name, type);
        this.price = price;
        this.owner = owner;
        this.color = color;
    }


    public static final Property MEDITERRANEAN_AVENUE = new Property(1, "Mediterranean Avenue", FieldType.PROPERTY, 60, null, PropertyColor.BROWN);
    public static final Property BALTIC_AVENUE = new Property(3, "Baltic Avenue", FieldType.PROPERTY, 60, null, PropertyColor.BROWN);

    public static final Property ORIENTAL_AVENUE = new Property(6, "Oriental Avenue", FieldType.PROPERTY, 100, null, PropertyColor.LIGHT_BLUE);
    public static final Property VERMONT_AVENUE = new Property(8, "Vermont Avenue", FieldType.PROPERTY, 100, null, PropertyColor.LIGHT_BLUE);
    public static final Property CONNECTICUT_AVENUE = new Property(9, "Oriental Avenue", FieldType.PROPERTY, 120, null, PropertyColor.LIGHT_BLUE);

    public static final Property ST_CHARLES_PLACE = new Property(11, "St Charles Place", FieldType.PROPERTY, 140, null, PropertyColor.PINK);
    public static final Property STATES_AVENUE = new Property(13, "States Avenue", FieldType.PROPERTY, 140, null, PropertyColor.PINK);
    public static final Property VIRGINIA_AVENUE = new Property(14, "Virginia Avenue", FieldType.PROPERTY, 160, null, PropertyColor.PINK);

    public static final Property ST_JAMES_PLACE = new Property(16, "St. James Place", FieldType.PROPERTY, 180, null, PropertyColor.ORANGE);
    public static final Property TENNESSEE_AVENUE = new Property(18, "Tennessee Avenue", FieldType.PROPERTY, 180, null, PropertyColor.ORANGE);
    public static final Property NEW_YORK_AVENUE = new Property(19, "New York Avenue", FieldType.PROPERTY, 200, null, PropertyColor.ORANGE);

    public static final Property KENTUCKY_AVENUE = new Property(21, "Kentucky Avenue", FieldType.PROPERTY, 220, null, PropertyColor.RED);
    public static final Property INDIANA_AVENUE = new Property(23, "Indiana Avenue", FieldType.PROPERTY, 220, null, PropertyColor.RED);
    public static final Property ILLINOIS_AVENUE = new Property(24, "Illinois Avenue", FieldType.PROPERTY, 240, null, PropertyColor.RED);

    public static final Property ATLANTIC_AVENUE = new Property(26, "Atlantic Avenue", FieldType.PROPERTY, 260, null, PropertyColor.YELLOW);
    public static final Property VENTNOR_AVENUE = new Property(27, "Ventnor Avenue", FieldType.PROPERTY, 260, null, PropertyColor.YELLOW);
    public static final Property MARVIN_GARDENS = new Property(29, "Marvin Gardens", FieldType.PROPERTY, 280, null, PropertyColor.YELLOW);

    public static final Property PACIFIC_AVENUE = new Property(31, "Pacific Avenue", FieldType.PROPERTY, 300, null, PropertyColor.GREEN);
    public static final Property NORTH_CAROLINA_AVENUE = new Property(32, "North Carolina Avenue", FieldType.PROPERTY, 300, null, PropertyColor.GREEN);
    public static final Property PENNSYLVANIA_AVENUE = new Property(34, "Pennsylvania Avenue", FieldType.PROPERTY, 320, null, PropertyColor.GREEN);

    public static final Property PARK_PLACE = new Property(37, "Park Place", FieldType.PROPERTY, 350, null, PropertyColor.DARK_BLUE);
    public static final Property BOARDWALK = new Property(39, "Board Walk", FieldType.PROPERTY, 400, null, PropertyColor.DARK_BLUE);




}
