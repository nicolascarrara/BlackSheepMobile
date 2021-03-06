package fr.nikoala.blacksheep;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nicolascarrara on 13/05/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private Context context;
    private List<MyData> my_data;

    public CustomAdapter(Context context, List<MyData> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.synopsys.setText(my_data.get(position).getTitre());
        Glide.with(context).load("https://image.tmdb.org/t/p/w300"+my_data.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView synopsys;
        public ImageView image;

        public ViewHolder(View itemView){
            super(itemView);
            synopsys = (TextView) itemView.findViewById(R.id.synopsys);
            image = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    intent = new Intent(context , MovieDetailActivity.class);
                    intent.putExtra("MOVIE_DETAILS", (Serializable) my_data.get(getAdapterPosition()));
                    context.startActivity(intent);
                  //  Toast.makeText(context, my_data.get(getAdapterPosition()).getSynopsys() ,Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
