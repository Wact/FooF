package localhost.foof.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import localhost.foof.R;

/**
 * Класс Активность Заказ показывает подробную информацию о заказе
 */
public class OrderActivity extends AppCompatActivity{
    LinearLayout viewId;
    LinearLayout viewFirstName;
    LinearLayout viewSecondName;
    LinearLayout viewAddress;
    LinearLayout viewProducts;
    LinearLayout viewPrice;
    LinearLayout viewStatus;
    LinearLayout viewComment;

    TextView tvId;
    TextView tvFirstName;
    TextView tvSecondName;
    TextView tvAddress;
    TextView tvProducts;
    TextView tvPrice;
    TextView tvStatus;
    TextView tvComment;

    String id;
    String firstName;
    String secondName;
    String address;
    String products;
    String price;
    String status;
    String comment;

    JSONArray productsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        viewId = (LinearLayout) findViewById(R.id.idView);
        viewFirstName = (LinearLayout) findViewById(R.id.firstNameView);
        viewSecondName = (LinearLayout) findViewById(R.id.secondNameView);
        viewAddress = (LinearLayout) findViewById(R.id.addressView);
        viewProducts = (LinearLayout) findViewById(R.id.productsView);
        viewPrice = (LinearLayout) findViewById(R.id.priceView);
        viewStatus = (LinearLayout) findViewById(R.id.statusView);
        viewComment = (LinearLayout) findViewById(R.id.commentView);

        tvId = (TextView) findViewById(R.id.idText);
        tvFirstName = (TextView) findViewById(R.id.firstNameText);
        tvSecondName = (TextView) findViewById(R.id.secondNameText);
        tvAddress = (TextView) findViewById(R.id.addressText);
        tvProducts = (TextView) findViewById(R.id.productsText);
        tvPrice = (TextView) findViewById(R.id.priceText);
        tvStatus = (TextView) findViewById(R.id.statusText);
        tvComment = (TextView) findViewById(R.id.commentText);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        firstName = extras.getString("firstName");
        secondName = extras.getString("secondName");
        address = extras.getString("address");
        products = extras.getString("products");
        price = extras.getString("price");
        status = extras.getString("status");
        comment = extras.getString("comment");

        try {
            productsArray = new JSONArray(products);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        products = "";

        for(int i = 0; i < productsArray.length(); i++) {
            try {
                products+=productsArray.get(i).toString()+"\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        switch(status) {
            case "0":
                status=getString(R.string.statusExecuted);
                break;
            case "1":
                status=getString(R.string.statusActive);
                break;
        }

        switch(comment) {
            case "NULL":
                comment = "";
                break;
            case "OK":
                comment = getString(R.string.comment_success);
        }

        tvId.setText("#: "+id);
        tvFirstName.setText(getString(R.string.firstName)+firstName);
        tvSecondName.setText(getString(R.string.secondName)+secondName);
        tvAddress.setText(getString(R.string.address)+address);
        tvProducts.setText(getString(R.string.productList)+products);
        tvPrice.setText(getString(R.string.cost)+price);
        tvStatus.setText(status);
        tvComment.setText(getString(R.string.comment)+comment);
    }
}
