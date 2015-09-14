package edu.ricky.mada2.controller;

/**
 * Created by Ricky Wu on 2015/9/7.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.List;

import edu.ricky.mada2.R;
import edu.ricky.mada2.model.DbModel;
import edu.ricky.mada2.model.Event;
import edu.ricky.mada2.model.EventModel;

public class EventRecyclerViewAdapter extends RecyclerView
        .Adapter<EventRecyclerViewAdapter
        .DataObjectHolder> {
    // Models
    private EventModel eModel;
    private DbModel db;
    // Reference
    private Context context;
    private List<Event> mDataset;
    private MyClickListener myClickListener;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        //ImageView poster;
        TextView name;
        TextView date;
        TextView venue;
        TextView loc;

        public DataObjectHolder(View itemView) {
            super(itemView);
            //poster = (ImageView) itemView.findViewById(R.id.movie_thumbnail_imageview);
            name = (TextView) itemView.findViewById(R.id.event_name_textview);
            date = (TextView) itemView.findViewById(R.id.event_date_textview);
            venue = (TextView) itemView.findViewById(R.id.event_venue_textview);
            loc = (TextView) itemView.findViewById(R.id.event_loc_textview);

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

    public EventRecyclerViewAdapter(Context context) {
        this.eModel = EventModel.getSingleton();
        this.db = DbModel.getSingleton(context);
        this.context = context;
        load();
        reloadDataset();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.name.setText(mDataset.get(position).getName());
        holder.date.setText(mDataset.get(position).getEventDate().toString());
        holder.venue.setText(mDataset.get(position).getVenue());
        holder.loc.setText(mDataset.get(position).getLocation().toString());
        /*holder.rating.setText(Double.toString(mDataset.get(position).getMyRating()));
        Picasso.with(context)
                .load(mDataset.get(position).getIconUrl())
                .into(holder.poster
                );*/

    }

    public void addItem(Event dataObj, int index) {
        eModel.addEvent(dataObj);
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        eModel.removeEvent(mDataset.get(index));
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void reloadDataset() {
        mDataset = eModel.getAllEvent();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(List<Event> dataset, int position, View v);
    }

    private void save() {
        /*//TODO: Save Movies in the Map into Sqlite
        db.saveAllMovies(mModel.getMovieMap());
        return;*/
    }

    private void load() {
        /*//TODO: Load movies from Sqlite to Map
        Map<String, String> map = db.getAllMovies();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                JSONObject jo = new JSONObject(entry.getValue());
                Movie m = new Movie(jo);
                eModel.addEvent(m);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;*/
    }

    public void close() {
        save();
        eModel.close();
        db.close();
    }
}
