package fr.nikoala.blacksheep;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
        final Button seen = findViewById(R.id.buttonseen);
        final Button tosee =findViewById(R.id.buttontosee);
        final FilmManager m = new FilmManager(this); //
        m.open();

        image = findViewById(R.id.poster);
        titre = findViewById(R.id.titre);
        date = findViewById(R.id.date);
        note = findViewById(R.id.note);
        synopsys = findViewById(R.id.synopsys);
        tosee.setText("Je veux le voir !");
        seen.setText("Ajouter aux coups de coeur ");


        final MyData details = (MyData) getIntent().getExtras().getSerializable("MOVIE_DETAILS");
        tosee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(details.getTosee()==1){
                    details.setTosee(0);
                    tosee.setText("Je veux le voir !");

                }else{
                    details.setTosee(1);
                    tosee.setText("Je l'ai déjà vu !");
                }
                m.modFilm(details);
            }
        });
        seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(details.getSeen()==1){
                    details.setSeen(0);
                    seen.setText("Ajouter aux coups de coeur ");

                }else{
                    details.setSeen(1);
                    seen.setText("Dans vos coups de coeur !");
                }
                m.modFilm(details);
            }
        });
        if(m.Checkfilm(details.getId())){
            Film f = m.getFilm(details.getId());
            if (f.getTosee()==1){
                tosee.setText("Je veux le voir !");
            }
            if (f.getSeen()==1){
                seen.setText("Dans vos coups de coeur !");
            }
        }else{
            m.addFilm(details);
        }
        if (details != null){
            Glide.with(this).load(details.getImage()).into(image);
            titre.setText(details.getTitre());
            date.setText(details.getDate_sortie());
            note.setText(Float.toString(details.getNote()));
            synopsys.setText(details.getSynopsys());
        }
    }
}
