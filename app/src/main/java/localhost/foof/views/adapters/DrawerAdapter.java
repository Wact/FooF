package localhost.foof.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import localhost.foof.R;
import localhost.foof.models.ItemModel;

/**
 * Класс Адаптер, определяющий отображение Item'ов в боковом меню
 */
public class DrawerAdapter extends ArrayAdapter<ItemModel> {

    Context mContext;
    int layoutId;
    ItemModel data[] = null;

    public DrawerAdapter(Context mContext, int layoutId, ItemModel[] data) {
        super(mContext, layoutId, data);
        this.layoutId = layoutId;
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View listItem, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutId, parent, false);

        RelativeLayout left_relative = (RelativeLayout) listItem.findViewById(R.id.left_relative);
        ImageView imageIcon = (ImageView) listItem.findViewById(R.id.imageIcon);
        TextView mName = (TextView) listItem.findViewById(R.id.mName);

        ItemModel model = data[position];

        imageIcon.setImageResource(model.icon);
        mName.setText(model.name);

        if(position==0) {
            imageIcon.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
            left_relative.setBackgroundResource(R.color.colorCap);
            mName.setHeight(185);
            mName.setTextSize(24);
            mName.setGravity(Gravity.CENTER_VERTICAL);
        }

        return listItem;
    }
}