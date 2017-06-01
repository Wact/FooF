package localhost.foof.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import localhost.foof.R;

/**
 * Created by ARTEM on 09.04.2017.
 */

/**
 * Класс Фрагмент Контакты, в котором отображается информация о разработчике
 */
public class ContactsFragment extends Fragment {
    public ContactsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        return view;
    }
}
