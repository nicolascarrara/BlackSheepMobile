package fr.nikoala.blacksheep;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MovieDetailActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayerFragment playerFragment;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = "AIzaSyBeSM1ONcRxgZMI58x550DUBNXu7jSMtIg";
    private static String VIDEO_ID = "";
    private ImageView image;
    private TextView date,synopsys,titre;
    private static final String TAG = "Movidetail";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_movie_detail);
        final LinearLayout layoutyt=(LinearLayout)this.findViewById(R.id.layoutyoutube);
        layoutyt.setVisibility(LinearLayout.GONE);
        final FloatingActionButton seen = findViewById(R.id.buttonseen);
        final FloatingActionButton tosee =findViewById(R.id.buttontosee);
        final FilmManager m = new FilmManager(this); //
        m.open();
        playerFragment =(YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment);

        playerFragment.initialize(YouTubeKey, this);
        
        final MyData details = (MyData) getIntent().getExtras().getSerializable("MOVIE_DETAILS");
         //Récupération de la bande annonce
        AsyncTask<Integer,Void,Void> task=new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/"+integers[0]+"/videos?api_key=e1bf1e9eda0b0070cc6a8ff1796ca8ec&language=fr")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray array = root.getJSONArray("results");
                    if (array.length()!=0){
                        for (int i = 0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                                Log.d(TAG,object.getString("site"));
                                details.setTrailer(object.getString("key"));
                                    Log.d(TAG,array.getJSONObject(i).getString("key"));
                                  if (i==1){
                                        VIDEO_ID=array.getJSONObject(i).getString("key");
                                  }
                                    // }

                        }
                    }else{
                        VIDEO_ID="";
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
                if (details.getTrailer()==""){
                    layoutyt.setVisibility(LinearLayout.GONE);
                }else{
                    layoutyt.setVisibility(LinearLayout.VISIBLE);
                }
            }
        };
        task.execute(details.getId());
        image = findViewById(R.id.poster);
        titre = findViewById(R.id.titre);
        date = findViewById(R.id.date);
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        synopsys = findViewById(R.id.synopsys);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(getFragmentManager().findFragmentById(R.id.youtube_player_fragment));

        //Boutons Permettant l'ajout ou la suppresion des film dans les listes
         tosee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(details.getTosee()==1){
                    details.setTosee(0);
                    tosee.setImageResource(R.drawable.ic_seen);
                    Snackbar.make(view, "Retiré des films vus" , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    details.setTosee(1);
                    tosee.setImageResource(R.drawable.ic_notsee);
                    Snackbar.make(view, "Ajouté aux films vus" , Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                m.modFilm(details);
            }
        });

        seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(details.getSeen()==1){
                    details.setSeen(0);
                    seen.setImageResource(R.drawable.ic_notliked);
                    Snackbar.make(view, "Retiré des coups de coeur" , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    details.setSeen(1);
                    seen.setImageResource(R.drawable.ic_liked);
                    Snackbar.make(view, "Ajouté aux coups de coeur" , Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                m.modFilm(details);
            }
        });

        // Vérification de l'existence du film en base de données locale, s'il n'existe pas il est ajouté
        if(m.Checkfilm(details.getId())){
            Film f = m.getFilm(details.getId());
            details.setSeen(f.getSeen());
            details.setTosee(f.getTosee());
            if (f.getTosee()==1){
                tosee.setImageResource(R.drawable.ic_notsee);
            }else {
                tosee.setImageResource(R.drawable.ic_seen);
            }
            if (f.getSeen()==1){
                seen.setImageResource(R.drawable.ic_liked);
            }else{
               seen.setImageResource(R.drawable.ic_notliked);
            }
        }else{
            m.addFilm(details);
            tosee.setImageResource(R.drawable.ic_seen);
            seen.setImageResource(R.drawable.ic_notliked);
        }

        if (details != null){
            Glide.with(this).load("https://image.tmdb.org/t/p/w300"+details.getImage()).into(image);
            titre.setText(details.getTitre());
            date.setText(details.getDate_sortie());
            synopsys.setText(details.getSynopsys());
            rating.setRating((details.getNote())/2);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mPlayer = youTubePlayer;
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!b) {

            //player.cueVideo("9rLZYyMbJic");
            mPlayer.loadVideo(VIDEO_ID);
        }
        else
        {
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        mPlayer = null;
    }
}
