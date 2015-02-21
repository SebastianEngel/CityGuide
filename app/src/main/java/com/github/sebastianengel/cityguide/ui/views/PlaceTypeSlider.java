package com.github.sebastianengel.cityguide.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
import timber.log.Timber;

/**
 * Draggable slider view that let's the user switch between the three places types.
 *
 * <p>
 *     A place type will be selected when the user taps on it or when the user is
 *     dragging the thumb and releases it. In that case, the type that's title is
 *     closest to the thumb, will be selected (the thumb snaps to it).
 * </p>
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
    private int touchPointerId;
    private float dragStartX;
    private float lastTouchX;
    private float touchX;
    private float targetX;
    private float distance;
    private TextView currentSelection;

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

    /**
     * Handles clicks on a title.
     */
    @OnClick({R.id.title_view_1, R.id.title_view_2, R.id.title_view_3})
    public void onTitleClicked(TextView titleView) {
        handleSelection(titleViews.indexOf(titleView));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int touchPointerIndex;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchPointerIndex = event.getActionIndex();
                touchPointerId = event.getPointerId(touchPointerIndex);
                dragStartX = event.getX(touchPointerIndex);
                lastTouchX = dragStartX;

                // Make the thumb slightly transparent while dragging so that the white text color
                // of an overlaid title view is still readable.
                thumb.setAlpha(0.9f);

                break;

            case MotionEvent.ACTION_MOVE:
                touchPointerIndex = event.findPointerIndex(touchPointerId);
                touchX = event.getX(touchPointerIndex);

                distance = touchX - lastTouchX;

                // Make sure the thumb doesn't leave the bounds of the parent container.
                targetX = thumb.getX() + distance;
                if (targetX < 0) {
                    targetX = 0;
                } else if (targetX + thumb.getWidth() > getWidth()) {
                    targetX = getWidth() - thumb.getWidth();
                }

                // Move the thumb to the new position.
                thumb.setX(targetX);

                lastTouchX = touchX;

                break;
            case MotionEvent.ACTION_UP:
                thumb.setAlpha(1f);

                // See with which title TextView the thumb intersects most. Snap to that.
                snapThumbToNearest();

                break;
            case MotionEvent.ACTION_CANCEL:
                Timber.d("ACTION_CANCEL at %f, %f", event.getX(), event.getY());
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private void handleSelection(int titleIndex) {
        TextView selectedTitleView = titleViews.get(titleIndex);

        // Now animate the thumb to be directly behind the selected TextView.
        snapToTitleView(selectedTitleView);

        // When the thumb just snaps back, to its position, don't do anything
        if (selectedTitleView == currentSelection) {
            return;
        }

        currentSelection = selectedTitleView;

        // Mark the clicked clicked TextView as selected.
        for (TextView titleView : titleViews) {
            if (titleView == selectedTitleView) {
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

    private void snapThumbToNearest() {
        Rect thumbBounds = new Rect();
        getViewBounds(thumb, thumbBounds);

        Rect titleViewBounds = new Rect();
        Rect intersectRect = new Rect();

        TextView snapToView = null;
        int lastIntersectFactor = 0;
        int tmpIntersectFactor;

        for (TextView titleView : titleViews) {
            getViewBounds(titleView, titleViewBounds);

            if (intersectRect.setIntersect(thumbBounds, titleViewBounds)) {
                tmpIntersectFactor = intersectRect.width() * intersectRect.height();

                if (tmpIntersectFactor > lastIntersectFactor) {
                    snapToView = titleView;
                    lastIntersectFactor = tmpIntersectFactor;
                }
            }
        }

        if (snapToView != null) {
            handleSelection(titleViews.indexOf(snapToView));
        }
    }

    private void snapToTitleView(TextView titleView) {
        // Now animate the thumb to be directly behind the selected TextView.
        thumb.animate().translationX(titleView.getX()).setDuration(100);
    }

    private Rect getViewBounds(View view, Rect bounds) {
        bounds.left = (int) view.getX();
        bounds.top = (int) view.getY();
        bounds.right = bounds.left + view.getWidth();
        bounds.bottom = bounds.top + view.getHeight();
        return bounds;
    }
}
