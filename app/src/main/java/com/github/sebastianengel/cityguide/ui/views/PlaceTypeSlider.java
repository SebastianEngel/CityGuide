package com.github.sebastianengel.cityguide.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.sebastianengel.cityguide.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * View that let's the user switch between the three places types.
 *
 * @author Sebastian Engel
 */
public class PlaceTypeSlider extends FrameLayout {

    @InjectViews({R.id.title_view_1, R.id.title_view_2, R.id.title_view_3}) List<TextView> titleViews;

    private OnSelectionChangedListener onSelectionChangedListener;

    public PlaceTypeSlider(Context context) {
        super(context);
        init();
    }

    public PlaceTypeSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaceTypeSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlaceTypeSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public API
    ///////////////////////////////////////////////////////////////////////////

    public void setSelection(int titleIndex) {
        for (TextView titleView : titleViews) {
            if (titleView == titleViews.get(titleIndex)) {
                titleView.setSelected(true);
            } else {
                titleView.setSelected(false);
            }
        }

        // Inform the listeners.
        if (onSelectionChangedListener != null) {
            onSelectionChangedListener.onSelectionChanged(titleIndex);
        }
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.onSelectionChangedListener = listener;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal behavior
    ///////////////////////////////////////////////////////////////////////////

    private void init() {
        inflate(getContext(), R.layout.view_place_type_slider, this);
        ButterKnife.inject(this);

        Drawable backgroundDrawable = getContext().getResources().getDrawable(R.drawable.seekbar_background);
        setBackground(backgroundDrawable);

        // Explicitly set padding to 0 here as setting the background drawable would automatically
        // result in a padding set.
        setPadding(0, 0, 0, 0);
    }

    private void handleSelection(int titleIndex) {
        for (TextView titleView : titleViews) {
            if (titleView == titleViews.get(titleIndex)) {
                titleView.setSelected(true);
            } else {
                titleView.setSelected(false);
            }
        }

        // Inform the listeners.
        if (onSelectionChangedListener != null) {
            onSelectionChangedListener.onSelectionChanged(titleIndex);
        }
    }

    @OnClick({R.id.title_view_1, R.id.title_view_2, R.id.title_view_3})
    public void onTitleClicked(TextView titleView) {
        handleSelection(titleViews.indexOf(titleView));
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int titleIndex);
    }

}
