package com.github.sebastianengel.cityguide.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Fragment listing the resulting places.
 *
 * @author Sebastian Engel
 */
public class PlacesListFragment extends Fragment {

    @Inject PlacesService placesService;

    @InjectView(R.id.list) RecyclerView recyclerView;

    private PlacesListAdapter listAapter;
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
        setupRecyclerView();
        loadPlaces();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal behavior
    ///////////////////////////////////////////////////////////////////////////

    private void setupRecyclerView() {
        listAapter = new PlacesListAdapter();
        recyclerView.setAdapter(listAapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    private void loadPlaces() {
        subscriptions.add(
            placesService.loadPlaces(PlacesType.BAR)
                .subscribe(
                        new Action1<List<Place>>() {
                            @Override
                            public void call(List<Place> places) {
                                Timber.d("Success");
                                listAapter.setPlaces(places);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Timber.d("Failure");
                                Timber.w(throwable.getMessage());
                            }
                        }
                ));
    }

}
