package d.manh.movienow.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import d.manh.movienow.R;
import d.manh.movienow.models.Review;

public class RecyclerViewReviewAdapter extends RecyclerView.Adapter<RecyclerViewReviewAdapter.ReviewViewHolder>{

//    final private ListItemClickListener trailersOnClickListener;
    private ArrayList<Review> listReviews;


    public RecyclerViewReviewAdapter(ArrayList<Review> reviews){
        this.listReviews = reviews;
//        this.trailersOnClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ReviewViewHolder(layoutInflater.inflate(R.layout.review_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.tvReviewAuthor.setText(listReviews.get(position).getAuthor());
        holder.tvReviewContent.setText(listReviews.get(position).getContent());
    }
    @Override
    public int getItemCount() {
        if(listReviews == null) return 0;
        return listReviews.size();
    }

    protected class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView tvReviewAuthor;
        TextView tvReviewContent;
        private ReviewViewHolder(View itemView) {
            super(itemView);
            tvReviewAuthor = itemView.findViewById(R.id.tv_review_author);
            tvReviewContent = itemView.findViewById(R.id.tv_review_content);
        }

    }
}

