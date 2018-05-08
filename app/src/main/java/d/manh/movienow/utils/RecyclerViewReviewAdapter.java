package d.manh.movienow.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import d.manh.moviewnow.R;

public class RecyclerViewReviewAdapter extends RecyclerView.Adapter<RecyclerViewReviewAdapter.ReviewViewHolder>{


    final private ListItemClickListener trailersOnClickListener;
    private List<Review> listReviews;

    public RecyclerViewReviewAdapter(ListItemClickListener listItemClickListener, List<Review> reviews){
        this.listReviews = reviews;
        this.trailersOnClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ReviewViewHolder(layoutInflater.inflate(R.layout.trailer_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.tvReviewAuthor.setText(listReviews.get(position).getReviewAuthor());
        holder.tvReviewContent.setText(listReviews.get(position).getReviewContent());
    }
    @Override
    public int getItemCount() {
        return 0;
    }
    protected class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvReviewAuthor;
        TextView tvReviewContent;
        private ReviewViewHolder(View itemView) {
            super(itemView);
            tvReviewAuthor = itemView.findViewById(R.id.tv_review_author);
            tvReviewContent = itemView.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            trailersOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

