package com.github.sebastianengel.cityguide.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.sebastianengel.cityguide.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * View that let's the user switch between the three places types.
 *
 * @author Sebastian Engel
 */
public class PlaceTypeSlider extends FrameLayout {

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int titleIndex);
    }

    @InjectViews({R.id.title_view_1, R.id.title_view_2, R.id.title_view_3}) List<TextView> titleViews;
    @InjectView(R.id.thumb) View thumb;

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
        handleSelection(titleIndex);
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

        setBackground(getContext().getResources().getDrawable(R.drawable.seekbar_background));

        // Explicitly set padding to 0 here as setting the background drawable would automatically
        // result in a padding set.
        setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // Adjust the thumb's size to match the size of the TextViews.
        ViewGroup.LayoutParams layoutParams = thumb.getLayoutParams();
        layoutParams.width = titleViews.get(0).getWidth();
        layoutParams.height = getHeight();
    }

    @OnClick({R.id.title_view_1, R.id.title_view_2, R.id.title_view_3})
    public void onTitleClicked(TextView titleView) {
        handleSelection(titleViews.indexOf(titleView));
    }

    private void handleSelection(int titleIndex) {
        TextView selectedTextView = titleViews.get(titleIndex);

        // Mark the clicked clicked TextView as selected.
        for (TextView titleView : titleViews) {
            if (titleView == selectedTextView) {
                titleView.setSelected(true);
            } else {
                titleView.setSelected(false);
            }
        }

        // Now animate the thumb to be directly behind the selected TextView.
        thumb.animate().translationX(selectedTextView.getX()).setDuration(100);

        // Inform the listeners.
        if (onSelectionChangedListener != null) {
            onSelectionChangedListener.onSelectionChanged(titleIndex);
        }
    }

}
