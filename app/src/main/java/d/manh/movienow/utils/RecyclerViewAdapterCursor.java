package d.manh.movienow.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import d.manh.movienow.R;
import d.manh.movienow.data.StoreContract;

public class RecyclerViewAdapterCursor extends RecyclerView.Adapter<RecyclerViewAdapterCursor.MyViewHolderCursor> {

    private Context context;
    final private ListItemClickListener myOnClickListener;
    private Cursor cursor;

    public RecyclerViewAdapterCursor(Context context, Cursor cursor, ListItemClickListener myOnClickListener) {
        this.context = context;
        this.cursor = cursor;
        this.myOnClickListener = myOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolderCursor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.movie_card,parent,false);
        return new MyViewHolderCursor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCursor holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String title = cursor.getString(cursor.getColumnIndex(StoreContract.COLUMN_MOVIE_TITLES));
        holder.tvMovieTitle.setText(title);

        String posterPath = cursor.getString(cursor.getColumnIndex(StoreContract.COLUMN_MOVIE_POSTER_PATH));
        Uri posterUri = Uri.parse(posterPath);
        holder.ivImageMovie.setImageURI(posterUri);

    }
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    // Swap the current cursor with new one and trigger the UI refresh
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (cursor != null) cursor.close();
            cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class MyViewHolderCursor extends RecyclerView.ViewHolder  implements View.OnClickListener {
        ImageView ivImageMovie;
        TextView tvMovieTitle;

        private MyViewHolderCursor(View itemView) {
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
