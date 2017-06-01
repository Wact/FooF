package localhost.foof.models;

/**
 * Created by ARTEM on 28.05.2017.
 */

/**
 * Класс Аккаунт
 */
public class Account {
    String mail;
    String password;
    String dataAboutAccount;
    String orders;
    String bonus;

    public Account(String mail, String password, String dataAboutAccount, String orders, String bonus) {
        this.mail = mail;
        this.password = password;
        this.dataAboutAccount = dataAboutAccount;
        this.orders = orders;
        this.bonus = bonus;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataAboutAccount() {
        return dataAboutAccount;
    }

    public void setDataAboutAccount(String dataAboutAccount) {
        this.dataAboutAccount = dataAboutAccount;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
