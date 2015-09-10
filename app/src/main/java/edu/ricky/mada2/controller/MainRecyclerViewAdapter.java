package edu.ricky.mada2.controller;

/**
 * Created by Ricky Wu on 2015/9/7.
 */
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ricky.mada2.MainActivity;
import edu.ricky.mada2.R;
import edu.ricky.mada2.model.Movie;
import edu.ricky.mada2.model.MovieModel;

public class MainRecyclerViewAdapter extends RecyclerView
        .Adapter<MainRecyclerViewAdapter
        .DataObjectHolder> {
    private MovieModel mModel;
    // Reference Controller
    private MainActivity mActivity;
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
        RelativeLayout mContainer;

        public DataObjectHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.movie_thumbnail_imageview);
            title = (TextView) itemView.findViewById(R.id.movie_title_textview);
            genre = (TextView) itemView.findViewById(R.id.movie_genre_textview);
            year = (TextView) itemView.findViewById(R.id.movie_year_textview);
            plot = (TextView) itemView.findViewById(R.id.movie_plot_textview);
            rating = (TextView) itemView.findViewById(R.id.movie_rating_textview);
            //mContainer = (RelativeLayout) itemView.findViewById(R.id.card_view_container);

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

    public MainRecyclerViewAdapter(MainActivity activity) {
        mModel = MovieModel.getSingleton();
        mDataset = mModel.getAllMovies();
        mActivity = activity;
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
        //holder.year.setText(mDataset.get(position).getYear());
        holder.genre.setText(mDataset.get(position).getGenre());
        holder.year.setText(mDataset.get(position).getYear());
        holder.plot.setText(mDataset.get(position).getPlot());
        holder.rating.setText(Double.toString(mDataset.get(position).getMyRating()));
        Picasso.with(mActivity)
                .load(mDataset.get(position).getIconUrl())
                .into(holder.poster/*(), PicassoPalette.with(mDataset.get(position).getIconUrl(), holder.poster)
                                .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                                .intoBackground(holder.mContainer)
                                .intoTextColor(holder.title)*/
//
//                                .use(PicassoPalette.Profile.VIBRANT)
//                                .intoBackground(titleView, PicassoPalette.Swatch.RGB)
//                                .intoTextColor(titleView, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                );
        //Bitmap bitmap = ((BitmapDrawable)holder.poster.getDrawable()).getBitmap();
        //.transform(new RoundedTransformation(10, 0))
                //.fit()
                //.into(holder.poster);

        /*Palette.generateAsync(bitmap,
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrant =
                                palette.getVibrantSwatch();
                        if (vibrant != null) {
                            // If we have a vibrant color
                            // update the title TextView
                            holder.mContainer.setBackgroundColor(
                                    vibrant.getRgb());
//                            titleView.setTextColor(
//                                    vibrant.getTitleTextColor());
                        }
                    }
                });*/
    }

    public void addItem(Movie dataObj, int index) {
        mModel.addMovie(dataObj);
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mModel.removeMovie(mDataset.get(index));
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void reloadDataset() {
        mDataset = mModel.getAllMovies();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(List<Movie> dataset,int position, View v);
    }

    public void close() {
        Log.e("MainRecyler", "close");
        mModel.close();
    }
}
