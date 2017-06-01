package localhost.foof.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Класс Заказ
 */
public class Order implements Parcelable{
    public String id;
    public String firstName;
    public String secondName;
    public String address;
    public String products;
    public String price;
    public String status;
    public String comment;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param id - Уникальный номер заказа
     * @param firstName - Имя пользователя
     * @param secondName - Фамилия пользователя
     * @param address - Адрес доставки
     * @param products - Список товаров
     * @param price - Общая стоимость
     * @param status - Статус заказа
     * @param comment - Комментарий менеджера
     */
    public Order(String id, String firstName, String secondName, String address, String products, String price, String status, String comment) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.products = products;
        this.price = price;
        this.status = status;
        this.comment = comment;
    }

    private Order(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        secondName = in.readString();
        address = in.readString();
        products = in.readString();
        price = in.readString();
        status = in.readString();
        comment = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getAddress() {
        return address;
    }

    public String getProducts() {
        return products;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(firstName);
        parcel.writeString(secondName);
        parcel.writeString(address);
        parcel.writeString(products);
        parcel.writeString(price);
        parcel.writeString(status);
        parcel.writeString(comment);
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
