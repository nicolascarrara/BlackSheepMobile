package fr.nikoala.blacksheep;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView image;
    private TextView note,date,synopsys,titre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_movie_detail);
        image = findViewById(R.id.poster);
        titre = findViewById(R.id.titre);
        date = findViewById(R.id.date);
        note = findViewById(R.id.note);
        synopsys = findViewById(R.id.synopsys);

        MyData details = (MyData) getIntent().getExtras().getSerializable("MOVIE_DETAILS");

        if (details != null){
            Glide.with(this).load(details.getImage()).into(image);
            titre.setText(details.getTitre());
            date.setText(details.getDate_sortie());
            note.setText(Float.toString(details.getNote()));
            synopsys.setText(details.getSynopsys());
        }
    }
}
