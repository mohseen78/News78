package com.example.abdulmohseen.news78.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdulmohseen.news78.network.ApiInterface;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.example.abdulmohseen.news78.News78Application;
import com.example.abdulmohseen.news78.R;
import com.example.abdulmohseen.news78.adapter.DataAdapter;
import com.example.abdulmohseen.news78.model.ArticleStructure;
import com.example.abdulmohseen.news78.model.Constants;
import com.example.abdulmohseen.news78.model.NewsResponse;
import com.example.abdulmohseen.news78.network.ApiClient;
import com.example.abdulmohseen.news78.network.interceptors.OfflineResponseCacheInterceptor;
import com.example.abdulmohseen.news78.network.interceptors.ResponseCacheInterceptor;
import com.example.abdulmohseen.news78.util.UtilityMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String[] SOURCE_ARRAY = {"google-news-in", "bbc-news", "the-hindu", "the-times-of-india", "mashable",
            "mtv-news", "buzzfeed", "bbc-sport", "espn-cric-info", "engadget", "the-next-web", "the-verge",
            "techcrunch",
            "techradar", "ign", "polygon"};

    private String SOURCE;

    private ArrayList<ArticleStructure> articleStructure = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Parcelable listState;
    private TextView mTitle;
    private Typeface montserrat_regular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        createToolbar();
        createRecyclerView();

        SOURCE = SOURCE_ARRAY[0];
        mTitle.setText(R.string.toolbar_default_text);
        onLoadingSwipeRefreshLayout();

        createDrawer(savedInstanceState, toolbar);
    }

    private void createToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTitle = findViewById(R.id.toolbar_title);
    }



    private void createRecyclerView() {
        recyclerView = findViewById(R.id.card_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }



    private void createDrawer(Bundle savedInstanceState, final Toolbar toolbar) {

        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(0).withName("GENERAL")
                .withSelectable(false);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Google News India")
                .withIcon(R.drawable.ic_googlenews);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("BBC News")
                .withIcon(R.drawable.ic_bbcnews);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("The Hindu")
                .withIcon(R.drawable.ic_thehindu);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("The Times of India")
                .withIcon(R.drawable.ic_timesofindia);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("Mashable")
                .withIcon(R.drawable.ic_mashablenews);


        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(6).withName("ENTERTAINMENT");
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("MTV New")
                .withIcon(R.drawable.ic_mtvnews);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("Buzzfeed")
                .withIcon(R.drawable.ic_buzzfeednews);


        SecondaryDrawerItem item9 = new SecondaryDrawerItem().withIdentifier(9).withName("SPORTS");
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(10).withName("BBC Sports")
                .withIcon(R.drawable.ic_bbcsports);
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(11).withName("Espn CricketInfo")
                .withIcon(R.drawable.ic_espncricinfo);


        SectionDrawerItem item12 = new SectionDrawerItem().withIdentifier(12).withName("TECHNOLOGY");
        PrimaryDrawerItem item13 = new PrimaryDrawerItem().withIdentifier(13).withName("Engadget")
                .withIcon(R.drawable.ic_engadget).withTypeface(montserrat_regular);
        PrimaryDrawerItem item14 = new PrimaryDrawerItem().withIdentifier(14).withName("The Next Web")
                .withIcon(R.drawable.ic_thenextweb).withTypeface(montserrat_regular);
        PrimaryDrawerItem item15 = new PrimaryDrawerItem().withIdentifier(15).withName("The Verge")
                .withIcon(R.drawable.ic_theverge).withTypeface(montserrat_regular);
        PrimaryDrawerItem item16 = new PrimaryDrawerItem().withIdentifier(16).withName("TechCrunch")
                .withIcon(R.drawable.ic_techcrunch).withTypeface(montserrat_regular);
        PrimaryDrawerItem item17 = new PrimaryDrawerItem().withIdentifier(17).withName("TechRadar")
                .withIcon(R.drawable.ic_techradar).withTypeface(montserrat_regular);


        SectionDrawerItem item18 = new SectionDrawerItem().withIdentifier(18).withName("GAMING");
        PrimaryDrawerItem item19 = new PrimaryDrawerItem().withIdentifier(19).withName("IGN")
                .withIcon(R.drawable.ic_ignnews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item20 = new PrimaryDrawerItem().withIdentifier(20).withName("Polygon")
                .withIcon(R.drawable.ic_polygonnews).withTypeface(montserrat_regular);



        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.nav_header)
                .addProfiles(
                        new ProfileDrawerItem().withName("News 78").withIcon(getResources().getDrawable(R.drawable.ic_buzzfeednews))
                )
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .addDrawerItems(item0, item1, item2, item3, item4, item5, item6, item7, item8, item9,
                        item10, item11, item12, item13, item14, item15, item16, item17, item18, item19,
                        item20)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int selected = (int) (long) drawerItem.getIdentifier();
                        switch (selected) {
                            case 1:
                                SOURCE = SOURCE_ARRAY[0];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 2:
                                SOURCE = SOURCE_ARRAY[1];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 3:
                                SOURCE = SOURCE_ARRAY[2];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 4:
                                SOURCE = SOURCE_ARRAY[3];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;

                            case 5:
                                SOURCE = SOURCE_ARRAY[4];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 7:
                                SOURCE = SOURCE_ARRAY[5];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 8:
                                SOURCE = SOURCE_ARRAY[6];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 10:
                                SOURCE = SOURCE_ARRAY[7];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 11:
                                SOURCE = SOURCE_ARRAY[8];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 13:
                                SOURCE = SOURCE_ARRAY[9];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 14:
                                SOURCE = SOURCE_ARRAY[10];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 15:
                                SOURCE = SOURCE_ARRAY[11];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 16:
                                SOURCE = SOURCE_ARRAY[12];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 17:
                                SOURCE = SOURCE_ARRAY[13];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 19:
                                SOURCE = SOURCE_ARRAY[14];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 20:
                                SOURCE = SOURCE_ARRAY[15];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;


                            default:
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }


    private void loadJSON() {
        swipeRefreshLayout.setRefreshing(true);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new ResponseCacheInterceptor());
        httpClient.addInterceptor(new OfflineResponseCacheInterceptor());
        httpClient.cache(new Cache(new File(News78Application.getNews78ApplicationInstance().getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        ApiInterface request = ApiClient.getClient(httpClient).create(ApiInterface.class);

        Call<NewsResponse> call = request.getHeadlines(SOURCE, 25, Constants.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articleStructure.isEmpty()) {
                        articleStructure.clear();
                    }

                    articleStructure = response.body().getArticles();

                    adapter = new DataAdapter(MainActivity.this, articleStructure);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }


            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadJSON();
    }

    /*
     ** TODO: APP INDEXING(App is not indexable by Google Search; consider adding at least one Activity with an ACTION-VIEW) .
     ** TODO: ADDING ATTRIBUTE android:fullBackupContent
     **/

    private void onLoadingSwipeRefreshLayout() {
        if (!UtilityMethods.isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "Could not load latest News. Please turn on the Internet.", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }
                }
        );
    }


    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setMessage("Do you want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        //add the values which need to be saved from the drawer to the bundle
        bundle = result.saveInstanceState(bundle);
        //add the values which need to be saved from the accountHeader to the bundle
        bundle = accountHeader.saveInstanceState(bundle);

        super.onSaveInstanceState(bundle);
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(Constants.RECYCLER_STATE_KEY, listState);
        bundle.putString(Constants.SOURCE, SOURCE);
        bundle.putString(Constants.TITLE_STATE_KEY, mTitle.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            SOURCE = savedInstanceState.getString(Constants.SOURCE);
            createToolbar();
            mTitle.setText(savedInstanceState.getString(Constants.TITLE_STATE_KEY));
            listState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE_KEY);
            createDrawer(savedInstanceState, toolbar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
