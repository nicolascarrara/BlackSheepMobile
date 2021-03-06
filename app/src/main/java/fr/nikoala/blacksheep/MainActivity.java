package fr.nikoala.blacksheep;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.SearchManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView;
    private boolean search=false;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;
    private int page=1;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SearchView recherche = findViewById(R.id.searchView);
        recherche.setFocusable(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        load_discover(page);
        gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CustomAdapter(this,data_list);
        recyclerView.setAdapter(adapter);
        recherche.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                load_search(query);
                if (TextUtils.isEmpty(query)){
                    page=1;
                    search=false;
                    data_list.clear();
                    load_discover(page);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recherche.getWindowToken(), 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    load_search(newText);
                    } else {
                    page=1;
                    search=false;
                    data_list.clear();
                    load_discover(page);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recherche.getWindowToken(), 0);
                    return false;
                    }
                return false; }
        });

        recherche.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                page=1;
                search=false;
                data_list.clear();
                load_discover(page);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(recherche.getWindowToken(), 0);
                return false;
            }
        });
               
               //ajout d'un listener pour controler le scrolling et récuperer de nouveau films
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1){
                    if (!search) {
                        page = page + 1;
                        load_discover(page);
                    }
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

               //récupération des films a découvrir
    private void load_discover(int id) {

        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/discover/movie?api_key=e1bf1e9eda0b0070cc6a8ff1796ca8ec&language=fr-fr&sort_by=popularity.desc&include_adult=false&include_video=false&page="+integers[0])
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray array = root.getJSONArray("results");

                    for (int i = 0; i<array.length(); i++){

                        JSONObject object = array.getJSONObject(i);
                        MyData data = new MyData(object.getInt("id"),object.getString("title"),object.getString("overview"),object.getLong("vote_average"),object.getString("release_date"),object.getString("backdrop_path"),0,0,"");
                        if (TextUtils.isEmpty(data.getSynopsys())){
                            data.setSynopsys("Pas de description disponible");
                        }
                        data_list.add(data);
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("End of content");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        task.execute(id);
    }
    
    // envoi de la requete de recherche d'un film
    private void load_search(final String recherche){


        AsyncTask<String,Void,Void> task = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/search/movie?api_key=e1bf1e9eda0b0070cc6a8ff1796ca8ec&language=fr-fr&query="+strings[0])
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray array = root.getJSONArray("results");
                    if (array.length()!=0){
                        data_list.clear();
                        search=true;
                        for (int i = 0; i<array.length(); i++){

                            JSONObject object = array.getJSONObject(i);
                            MyData data = new MyData(object.getInt("id"),object.getString("title"),object.getString("overview"),object.getLong("vote_average"),object.getString("release_date"),object.getString("backdrop_path"),0,0,"");

                            data_list.add(data);
                        }
                    }else{
                        search=false;
                        page=1;
                      //  Toast.makeText(getApplicationContext(), "Aucun résultat disponible" ,Toast.LENGTH_SHORT).show();
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("End of content");
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        task.execute(recherche);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_liked) {
            Intent intent;
            intent = new Intent(getApplicationContext() , LikedActivity.class);
            getApplicationContext().startActivity(intent);
        } else if (id == R.id.nav_tosee) {
            Intent intent;
            intent = new Intent(getApplicationContext() , ToSeeActivity.class);
            getApplicationContext().startActivity(intent);
        } else if (id == R.id.nav_discover) {
            Intent intent;
            intent = new Intent(getApplicationContext() , MainActivity.class);
            getApplicationContext().startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
