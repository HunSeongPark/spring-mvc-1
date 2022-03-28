package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Hunseong on 2022/03/29
 */
@Getter
@Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
