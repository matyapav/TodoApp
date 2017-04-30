package cz.matyapav.todoapp.todo.screen.list;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import cz.matyapav.todoapp.todo.model.Todo;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    ListView list;
    private GestureDetector gestureDetector;
    private Context context;
    private ArrayAdapter<Todo> adapter;

    public OnSwipeTouchListener(Context ctx, ListView list, ArrayAdapter<Todo> adapter) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        context = ctx;
        this.list = list;
        this.adapter = adapter;
    }

    public OnSwipeTouchListener() {
        super();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return false;
    }

    public void onSwipeRight(int pos) {
        Toast.makeText(context, "rigth"+pos, Toast.LENGTH_SHORT).show();
    }

    public void onSwipeLeft(int pos) {
        Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        private int getPostion(MotionEvent e1) {
            return list.pointToPosition((int) e1.getX(), (int) e1.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY)
                    && Math.abs(distanceX) > SWIPE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight(getPostion(e1));
                else
                    onSwipeLeft(getPostion(e1));
                return true;
            }
            return false;
        }

    }
}
