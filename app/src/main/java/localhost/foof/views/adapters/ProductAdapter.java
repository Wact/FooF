package localhost.foof.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import localhost.foof.views.fragments.NewOrderFragment;
import localhost.foof.R;
import localhost.foof.models.Product;

/**
 * Класс Адаптер Продуктов, определяющий отображение товара
 */
public class ProductAdapter extends ArrayAdapter<Product>{
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Product> products;
    private final String pattern = "##0.00";
    private final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat decimalFormat = new DecimalFormat(pattern, otherSymbols);

    public ProductAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource, products);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.products = products;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        NewOrderFragment.products = products;
        if(convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }


        final Product product = products.get(position);
        viewHolder.nameView.setText(product.getName());
        viewHolder.priceView.setText(product.getPrice());
        viewHolder.countView.setText(String.valueOf(product.getCount()));
        String imageUrl = "http://i.imgur.com/";
        String fileName = product.getFileName();
        Picasso.with(getContext())
                .load(imageUrl+fileName)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(viewHolder.imageView);

        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newCount = products.get(position).getCount()+1;
                product.setCount(newCount);
                viewHolder.countView.setText(Integer.toString(newCount));
                if(newCount>1)
                    viewHolder.priceView.setText("" + decimalFormat.format(Float.parseFloat(product.getPrice()) * newCount));
            }
        });

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.getCount()>0) {
                    int newCount = products.get(position).getCount()-1;
                    product.setCount(newCount);
                    viewHolder.countView.setText(Integer.toString(newCount));
                    if(newCount>0)
                        viewHolder.priceView.setText("" + decimalFormat.format(Float.parseFloat(product.getPrice()) * newCount));
                }
            }
        });

        return convertView;
    }

    /**
     * Класс формы для отображения товара
     */
    private class ViewHolder {
        final Button addButton,removeButton;
        final TextView nameView, priceView, countView;
        final ImageView imageView;

        ViewHolder(View view) {
            addButton = (Button) view.findViewById(R.id.addButton);
            removeButton = (Button) view.findViewById(R.id.removeButton);
            nameView = (TextView) view.findViewById(R.id.nameView);
            priceView = (TextView) view.findViewById(R.id.priceView);
            countView = (TextView) view.findViewById(R.id.countView);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}