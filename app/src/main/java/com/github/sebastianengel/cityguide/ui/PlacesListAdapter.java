package com.github.sebastianengel.cityguide.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Adapter for the RecyclerView in {@link com.github.sebastianengel.cityguide.ui.PlacesListFragment}.
 *
 * @author Sebastian Engel
 */
public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private final List<Place> places;
    private Drawable barDrawable;
    private Drawable cafeDrawable;
    private Drawable bistroDrawable;

    public PlacesListAdapter() {
        this.places = new ArrayList<>();
    }

    public void setPlaces(List<Place> places) {
        this.places.clear();
        notifyItemRangeRemoved(0, this.places.size());

        this.places.addAll(places);
        notifyItemRangeInserted(0, places.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        View listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_places, viewGroup, false);

        // Load the drawables only once here, not on each binding.
        this.barDrawable = context.getResources().getDrawable(R.drawable.ic_bar);
        this.cafeDrawable = context.getResources().getDrawable(R.drawable.ic_cafe);
        this.bistroDrawable = context.getResources().getDrawable(R.drawable.ic_bistro);

        return new ViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Place place = places.get(position);

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
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.icon) ImageView icon;
        @InjectView(R.id.name) TextView name;
        @InjectView(R.id.rating_bar) RatingBar ratingBar;

        public ViewHolder(View listItemView) {
            super(listItemView);
            ButterKnife.inject(this, listItemView);
        }
    }

}
