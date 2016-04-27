package uk.ac.prco.plymouth.thecentrecircle.classes;

/**
 * Created by charliewaite on 27/04/2016.
 */
public class AboutItem {
    private String itemName;
    private String itemValue;
    private int itemIcon;

    public AboutItem(String itemName, String itemValue, int itemIcon) {
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public int getItemIcon() {
        return itemIcon;
    }
}
