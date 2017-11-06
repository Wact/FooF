package localhost.foof.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.util.concurrent.ExecutionException;

import localhost.foof.views.fragments.AccountFragment;
import localhost.foof.views.fragments.ContactsFragment;
import localhost.foof.views.adapters.DrawerAdapter;
import localhost.foof.network.NetworkHandler;
import localhost.foof.views.fragments.NewOrderFragment;
import localhost.foof.views.fragments.OrdersFragment;
import localhost.foof.models.Product;
import localhost.foof.R;
import localhost.foof.models.ItemModel;

import static android.os.ParcelFileDescriptor.MODE_APPEND;
import static localhost.foof.views.fragments.NewOrderFragment.postProducts;
import static localhost.foof.views.fragments.NewOrderFragment.products;

/**
 * Класс Главной Активити отображает основной интерфейс приложения
 */
public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
    public File file = null;
    public static JSONObject account;

    private String[] mItemTitles;
    private DrawerLayout mDrawerLayout;
    public ListView mDrawerListView;
    private Toolbar mToolbar;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private int lastPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        NetworkHandler networkHandler = new NetworkHandler(getApplicationContext());

        try {
            readFile(getApplicationContext());
            networkHandler.execute("getAccount", account.get("mail").toString(), account.get("password").toString());
            account = networkHandler.get() == null ? new JSONObject() : new JSONObject(networkHandler.get());
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        mTitle = getTitle();
        mItemTitles = getResources().getStringArray(R.array.drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        String cap = getString(R.string.leftDrawerCap);
        if(!account.isNull("mail")) {
            try {
                JSONObject obj = new JSONObject(account.get("dataAboutAccount").toString());
                cap = obj.get("firstName").toString()+" "+obj.get("secondName").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ItemModel[] dItems = fillDataModel(cap);

        DrawerAdapter adapter = new DrawerAdapter(this, R.layout.item_row, dItems);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setOnItemClickListener(new ItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        assert mDrawerLayout != null;
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        try {
            int fragment = (int) getIntent().getExtras().get("Fragment");

            lastPosition = fragment;
            initFragment(fragment);
        } catch (NullPointerException e) {
            lastPosition = 1;
            initFragment(1);
        }
    }

    /**
     * Функция инициализации Item'ов в боковом меню
     * @param cap - Строка, содержащая просьбу об авторизации в системе.
     *            Если пользователь авторизован, будут показаны его Имя и Фамилия
     * @return - массив объектов ItemModel
     */
    private ItemModel[] fillDataModel(String cap) {
        return new ItemModel[]{
                new ItemModel(R.drawable.products, cap),
                new ItemModel(R.drawable.products, getString(R.string.products)),
                new ItemModel(android.R.drawable.ic_dialog_info, getString(R.string.confirmOrder)),
                new ItemModel(R.drawable.notepad, getString(R.string.myOrders)),
                new ItemModel(android.R.drawable.ic_menu_call, getString(R.string.contacts))
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        System.out.println("onDrawerSlide");
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        System.out.println("onDrawerOpened");
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        System.out.println("onDrawerClosed");
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        System.out.println("onDrawerChanged");
    }

    private class ItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            initFragment(position);
        }
    }

    /**
     * Функция взаимодействия с toolbar'ом
     * @param menuItem - Menu Item, который нажал пользователь
     * @return - возвращает true, если нажат Menu Item 'Корзина'
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id==R.id.cart) {
            for(int i = 0; i < products.size(); i++) {
                Product nProduct = products.get(i);
                if(nProduct.getCount()!=0) {
                    postProducts.add(nProduct);
                }
            }
            if(postProducts.size()!=0) {
                Intent intent = new Intent(getApplicationContext(), NewOrderConfirmActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.makeOrder), Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            return true;
        }
        return mDrawerToggle.onOptionsItemSelected(menuItem) || super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Функция инициализации заголовка приложения
     * @param title - заголовок
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Процедура инициализации toolbar'а
     */
    void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    /**
     * Процедура инициадизации бокового меню
     */
    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
    }

    public void initFragment(int position) {
        Fragment fragment = null;
        String tag = "";

        switch (position) {
            case 0:
                fragment = new AccountFragment();
                tag = "ACCOUNT";
                break;
            case 1:
                fragment = new NewOrderFragment();
                tag = "PRODUCTS";
                break;
            case 2:
                mDrawerLayout.closeDrawer(mDrawerListView);
                Intent intent = new Intent(getApplicationContext(), NewOrderConfirmActivity.class);
                startActivity(intent);
                for(int i = 0; i < products.size(); i++) {
                Product nProduct = products.get(i);
                if(nProduct.getCount()!=0) {
                    postProducts.add(nProduct);
                }
            }
                break;
            case 3:
                fragment = new OrdersFragment();
                tag="ORDERS";
                break;
            case 4:
                fragment = new ContactsFragment();
                tag = "CONTACTS";
                break;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment fr = getSupportFragmentManager().findFragmentByTag(tag);
        if(fragment==null) {
            return;
        }
        if(fr!=null)
            fragmentTransaction.show(fr);
        else {
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
        }
        lastPosition=position;
        fragmentTransaction.commit();
        mDrawerListView.setItemChecked(position, true);
        mDrawerListView.setSelection(position);
        setTitle(mItemTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    @Override
    protected void onResumeFragments() { initFragment(lastPosition); }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    public static void readFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput("content.txt");
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            account = new JSONObject(new String (bytes));
            fis.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /*public static void writeFile(JSONObject jsonObject, Context context) {
        context = mainContext;
        try {
            FileOutputStream fos = mainContext.openFileOutput("content.txt", MODE_APPEND);
            byte[] bytes = jsonObject.toString().getBytes();
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}