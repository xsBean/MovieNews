package d.manh.movienow.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.List;

import d.manh.moviewnow.R;

public class RecyclerViewTrailerAdapter extends RecyclerView.Adapter<RecyclerViewTrailerAdapter.TrailerViewHolder> {
    final private ListItemClickListener trailersOnClickListener;
    private Context context;
    private List<Trailer> listTrailers;

    public RecyclerViewTrailerAdapter(ListItemClickListener listItemClickListener, List<Trailer> trailers){
        this.listTrailers = trailers;
        this.trailersOnClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.trailer_card,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        String url = StoreContract.URL_YOUTUBE +listTrailers.get(position).getTrailerKey() + StoreContract.URL_YOUTUBE_PATH;
        Picasso.with(context).load(url).into(holder.ivTrailersThumb);
    }

    @Override
    public int getItemCount() {
        return listTrailers.size();
    }


    protected class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivTrailersThumb;

        private TrailerViewHolder(View itemView) {
            super(itemView);
            ivTrailersThumb = itemView.findViewById(R.id.iv_trailer_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            trailersOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

