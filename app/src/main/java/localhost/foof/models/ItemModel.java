package localhost.foof.models;

/**
 * Created by ARTEM on 08.04.2017.
 */

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