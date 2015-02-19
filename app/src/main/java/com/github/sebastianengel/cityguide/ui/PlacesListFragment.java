package com.github.sebastianengel.cityguide.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    @InjectView(R.id.list_view) ListView listView;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private PlacesListAdapter listAdapter;

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

        listAdapter = new PlacesListAdapter(getActivity());
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        subscriptions.add(
            placesService.loadPlaces(PlacesType.BAR)
                .subscribe(
                    new Action1<List<Place>>() {
                        @Override
                        public void call(List<Place> places) {
                            Timber.d("Success");
                            listAdapter.setPlaces(places);
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

    @Override
    public void onStop() {
        super.onStop();

        if (subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }

}
