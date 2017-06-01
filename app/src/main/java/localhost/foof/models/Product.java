package localhost.foof.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Класс Товар
 */
public class Product implements Parcelable{
    public String name;
    public String price;
    public String fileName;
    public int count;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param name - Наименование товара
     * @param price - Цена товара
     * @param fileName - Наименование картинки на сервере
     */
    public Product(String name, String price, String fileName) {
        this.name = name;
        this.price = price;
        this.count = 0;
        this.fileName = fileName;
    }

    private Product(Parcel in) {
        name = in.readString();
        price = in.readString();
        fileName = in.readString();
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(fileName);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}