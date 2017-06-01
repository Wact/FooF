/*
 * Copyright (c) 2017.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package localhost.foof.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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
 * Created by ARTEM on 08.04.2017.
 */

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
        }

        return listItem;
    }
}