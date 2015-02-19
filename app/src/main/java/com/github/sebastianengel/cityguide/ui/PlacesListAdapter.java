package com.github.sebastianengel.cityguide.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.sebastianengel.cityguide.R;
import com.github.sebastianengel.cityguide.data.model.Place;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Adapter for the {@link com.github.sebastianengel.cityguide.ui.PlacesListFragment}.
 *
 * @author Sebastian Engel
 */
public class PlacesListAdapter extends BaseAdapter {

    private final Context context;
    private final List<Place> places;
    private final Drawable barDrawable;
    private final Drawable cafeDrawable;
    private final Drawable bistroDrawable;

    public PlacesListAdapter(Context context) {
        this.context = context;
        this.places = new ArrayList<>();

        // Load the drawables only once here, not on each getView() call.
        this.barDrawable = context.getResources().getDrawable(R.drawable.ic_bar);
        this.cafeDrawable = context.getResources().getDrawable(R.drawable.ic_cafe);
        this.bistroDrawable = context.getResources().getDrawable(R.drawable.ic_bistro);
    }

    public void setPlaces(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_places, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Place place = getItem(position);

        viewHolder.name.setText(place.name);
        viewHolder.ratingBar.setRating(place.rating);

        switch (place.type) {
            case BAR:
                viewHolder.icon.setImageDrawable(barDrawable);
                break;
            case CAFE:
                viewHolder.icon.setImageDrawable(cafeDrawable);
                break;
            case RESTAURANT:
                viewHolder.icon.setImageDrawable(bistroDrawable);
                break;
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return places.get(position).hashCode();
    }

    static class ViewHolder {
        @InjectView(R.id.icon) ImageView icon;
        @InjectView(R.id.name) TextView name;
        @InjectView(R.id.rating_bar) RatingBar ratingBar;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
