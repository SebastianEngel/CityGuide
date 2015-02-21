package com.github.sebastianengel.cityguide.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sebastianengel.cityguide.CityGuideApp;
import com.github.sebastianengel.cityguide.R;
import com.github.sebastianengel.cityguide.data.model.Place;
import com.github.sebastianengel.cityguide.data.model.PlacesType;
import com.github.sebastianengel.cityguide.domain.PlacesService;
import com.github.sebastianengel.cityguide.ui.views.PlaceTypeSlider;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Fragment listing the resulting places.
 *
 * @author Sebastian Engel
 */
public class PlacesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject PlacesService placesService;

    @InjectView(R.id.place_type_slider) PlaceTypeSlider placeTypeSlider;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @InjectView(R.id.list) RecyclerView recyclerView;
    @InjectView(R.id.empty_view) View emptyView;

    private PlacesListAdapter listAdapter;
    private PlacesType currentPlaceType;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    ///////////////////////////////////////////////////////////////////////////
    // Fragment instantiation
    ///////////////////////////////////////////////////////////////////////////

    public static PlacesListFragment newInstance() {
        return new PlacesListFragment();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Fragment lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        CityGuideApp.get(getActivity()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        setupPlacesSlider();
        setupSwipeContainer();
        setupRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // OnRefreshListener implementation
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onRefresh() {
        loadPlaces();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal behavior
    ///////////////////////////////////////////////////////////////////////////

    private void setupPlacesSlider() {
        placeTypeSlider.setOnSelectionChangedListener(new PlaceTypeSlider.OnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(int titleIndex) {
                switch (titleIndex) {
                    case 0:
                        currentPlaceType = PlacesType.BAR;
                        break;
                    case 1:
                        currentPlaceType = PlacesType.RESTAURANT;
                        break;
                    case 2:
                        currentPlaceType = PlacesType.CAFE;
                        break;
                    default:
                        currentPlaceType = PlacesType.BAR;
                        break;
                }
                loadPlaces();
            }
        });

        placeTypeSlider.setSelection(0);
    }

    private void setupSwipeContainer() {
        swipeContainer.setColorSchemeResources(R.color.primary);
        swipeContainer.setOnRefreshListener(this);
    }

    private void setupRecyclerView() {
        listAdapter = new PlacesListAdapter();
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    private void loadPlaces() {
        swipeContainer.setRefreshing(true);

        subscriptions.add(
            placesService.loadPlaces(currentPlaceType)
                .subscribe(
                    new Action1<List<Place>>() {
                        @Override
                        public void call(List<Place> places) {
                            listAdapter.setPlaces(places);
                            swipeContainer.setRefreshing(false);

                            if (listAdapter.getItemCount() < 1) {
                                showEmptyView(true);
                            } else {
                                showEmptyView(false);
                            }
                        }
                    },
                    new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            swipeContainer.setRefreshing(false);

                            showEmptyView(true);

                            SnackbarManager.show(
                                Snackbar.with(getActivity())
                                    .textColorResource(R.color.error_text)
                                    .text(R.string.error_loading_places));

                            Timber.w(throwable.getMessage());
                        }
                    }
                ));
    }

    private void showEmptyView(boolean show) {
        emptyView.setVisibility(show ? VISIBLE : GONE);
        recyclerView.setVisibility(show ? GONE : VISIBLE);
    }

}
