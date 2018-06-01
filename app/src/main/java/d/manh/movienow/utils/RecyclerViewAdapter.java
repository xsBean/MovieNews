package d.manh.movienow.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import d.manh.movienow.data.StoreContract;
import d.manh.movienow.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import d.manh.movienow.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private Context context;
    private List<Movie> listMovie;
    final private ListItemClickListener myOnClickListener;

    public RecyclerViewAdapter(ListItemClickListener listItemClickListener, List<Movie> listMovie) {
        this.myOnClickListener = listItemClickListener;
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.movie_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String url = StoreContract.URL_IMAGE + StoreContract.URL_PATH_POSTER_SIZE +listMovie.get(position).getPosterPath();
        Picasso.with(context).load(url).into(holder.ivImageMovie);
        holder.tvMovieTitle.setText(listMovie.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if(listMovie == null) return 0;
        else return listMovie.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        ImageView ivImageMovie;
        TextView tvMovieTitle;

       private MyViewHolder(View itemView) {
           super(itemView);
           ivImageMovie = itemView.findViewById(R.id.iv_movie_image);
           tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
           itemView.setOnClickListener(this);
       }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            myOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
