package localhost.foof.models;

/**
 * Класс создания Item'ов для отображения в боковом меню
 */
public class ItemModel {

    public int icon;
    public String name;

    public ItemModel(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }
}