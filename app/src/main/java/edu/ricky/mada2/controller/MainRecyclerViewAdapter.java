package edu.ricky.mada2.controller;

/**
 * Created by Ricky Wu on 2015/9/7.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import edu.ricky.mada2.R;
import edu.ricky.mada2.model.BitmapLruCache;
import edu.ricky.mada2.model.DbModel;
import edu.ricky.mada2.model.Movie;
import edu.ricky.mada2.model.MovieModel;

public class MainRecyclerViewAdapter extends RecyclerView
        .Adapter<MainRecyclerViewAdapter
        .DataObjectHolder> {
    // Models
    private MovieModel mModel;
    private BitmapLruCache bitmapLruCache;
    private DbModel db;
    // Reference
    private Context context;
    private List<Movie> mDataset;
    private MyClickListener myClickListener;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView poster;
        TextView title;
        TextView year;
        TextView genre;
        TextView plot;
        TextView rating;

        public DataObjectHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.movie_thumbnail_imageview);
            title = (TextView) itemView.findViewById(R.id.movie_title_textview);
            genre = (TextView) itemView.findViewById(R.id.movie_genre_textview);
            year = (TextView) itemView.findViewById(R.id.movie_year_textview);
            plot = (TextView) itemView.findViewById(R.id.movie_plot_textview);
            rating = (TextView) itemView.findViewById(R.id.movie_rating_textview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(mDataset, getAdapterPosition(), v);
        }
    }



    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MainRecyclerViewAdapter(Context context) {
        this.mModel = MovieModel.getSingleton(context);
        this.db = DbModel.getSingleton(context);
        this.bitmapLruCache = BitmapLruCache.getSingleton();
        this.context = context;
        reloadDataset(0);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.title.setText(mDataset.get(position).getTitle());
        holder.genre.setText(mDataset.get(position).getGenre());
        holder.year.setText(mDataset.get(position).getYear());
        //holder.plot.setText(mDataset.get(position).getPlot());
        holder.rating.setText(Double.toString(mDataset.get(position).getMyRating()));
        if (holder.poster != null) {
            bitmapLruCache.loadBitmap(mDataset.get(position).getIconUrl(), holder.poster);
        }
    }

    public void reloadDataset(int list) {
        switch(list) {
            case 0:
                mDataset = mModel.getAllMovies();
                break;
            case 1:
                mDataset = mModel.getSearchMovies();
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(List<Movie> dataset,int position, View v);
    }

    private void save() {
        //TODO: Save Movies in the Map into Sqlite
        //db.saveAllMovies(mModel.getMovieMap());
        return;
    }

    public void close() {
        save();
        mModel.close();
        db.close();
    }
}
