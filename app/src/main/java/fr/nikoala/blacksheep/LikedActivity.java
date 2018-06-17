package fr.nikoala.blacksheep;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LikedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CustomAdapter(this,data_list);

        recyclerView.setAdapter(adapter);
        final FilmManager m = new FilmManager(this); //
        m.open();
        Cursor c = m.getLiked();
        if (c.moveToFirst())
        {
            do {
                MyData data = new MyData(c.getInt(c.getColumnIndex(FilmManager.KEY_ID_FILM)),c.getString(c.getColumnIndex(FilmManager.KEY_TITRE_FILM)),c.getString(c.getColumnIndex(FilmManager.KEY_RESUME_FILM)),c.getFloat(c.getColumnIndex(FilmManager.KEY_NOTE_FILM)),c.getString(c.getColumnIndex(FilmManager.KEY_DATE_FILM)),c.getString(c.getColumnIndex(FilmManager.KEY_IMAGE_FILM)),c.getInt(c.getColumnIndex(FilmManager.KEY_TOSEE_FILM)),c.getInt(c.getColumnIndex(FilmManager.KEY_SEEN_FILM)),c.getString(c.getColumnIndex(FilmManager.KEY_TRAILER_FILM)));
                data_list.add(data);
                Log.d("test",
                        c.getInt(c.getColumnIndex(FilmManager.KEY_ID_FILM)) + "," +
                                c.getString(c.getColumnIndex(FilmManager.KEY_IMAGE_FILM))
                );
            }
            while (c.moveToNext());
        }
        c.close();
        adapter.notifyDataSetChanged();
    }

}
