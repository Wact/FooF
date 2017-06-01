package localhost.foof.views.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import localhost.foof.R;
import localhost.foof.models.Product;
import localhost.foof.network.RetrofitHelper;
/**
 * Класс Фрагмент Новый Заказ отображает список товаров при помощи ListView
 */
public class NewOrderFragment extends Fragment {
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Product> postProducts = new ArrayList<>();
    ListView productList;

    private ProgressDialog dialog;

    public NewOrderFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neworder, container, false);
        setRetainInstance(true);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        productList = (ListView) view.findViewById(R.id.productList);
        RetrofitHelper retrofitHelper = new RetrofitHelper(getContext());
        retrofitHelper.getProducts(dialog, productList);

        return view;
    }
}
