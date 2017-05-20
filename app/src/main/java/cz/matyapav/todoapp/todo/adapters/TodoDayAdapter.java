package cz.matyapav.todoapp.todo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.Calendar;
import java.util.List;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Storage;
import cz.matyapav.todoapp.util.Utils;


/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayAdapter extends RecyclerSwipeAdapter<TodoDayAdapter.DataObjectHolder> {

    private List<Todo> todos;
    private Activity context;
    private AdapterObserver observer;
    private boolean showCompleted;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Constants.PREFS_SHOW_COMPLETED)) {
                showCompleted = sharedPreferences.getBoolean(key, true);
                notifyDataSetChanged();
            }
        }
    };
    private Fragment fragment;

    public TodoDayAdapter(Activity context, final List<Todo> todos, AdapterObserver observer, Fragment fragment) {
        this.context = context;
        this.todos = todos;
        this.observer = observer;
        this.showCompleted = true; //default on first run is true;
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(listener);
        this.fragment = fragment;
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.list_item_swipe_layout;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder viewHolder, int position) {
        final Todo todo = todos.get(position);

        //setup swipe layout
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.doneLayout);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.deleteLayout);
        viewHolder.swipeLayout.setClickToClose(true);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todo.getDateAndTimeStart());
        String start = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        calendar.setTime(todo.getDateAndTimeEnd());
        String end = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        viewHolder.todoTime.setText(start + " - " + end);
        viewHolder.todoTitle.setText(todo.getTitle());
        if(todo.getCathegory() != null) {
            viewHolder.todoIcon.setImageResource(todo.getCathegory().getImgResourceId());
        }
        final int color = todo.getPriority().getColorId();
        GradientDrawable iconBg = (GradientDrawable) viewHolder.todoIcon.getBackground().mutate();
        iconBg.setColor(Utils.getColor(context, color));

        if(todo.isCompleted()){
            viewHolder.todoIcon.setImageResource(R.drawable.ic_check_circle);
            iconBg.setColor(Utils.getColor(context, R.color.secondary_text));
        }

        //add listeners to item
        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateTodoActivity.class);
                i.putExtra(Constants.PREFILLED_TODO, todo);
                fragment.startActivityForResult(i, Constants.TODO_CREATE_EDIT_REQUEST_CODE);
            }
        });

        final int pos = position;
        viewHolder.doneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setCompleted(!todo.isCompleted());
                Snackbar undoCompletedSnackbar = Snackbar
                        .make(viewHolder.itemView, todo.getTitle()+" marked as "+
                                (todo.isCompleted()?"completed":"incompleted"), Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                todo.setCompleted(!todo.isCompleted());
                                Storage.updateTodosInSharedPreferences(context);
                                notifyItemChanged(pos, todo);
                                observer.onAdapterDataChanged();
                            }
                        });
                undoCompletedSnackbar.show();
                viewHolder.swipeLayout.close();
                Storage.updateTodosInSharedPreferences(context);
                notifyItemChanged(pos, todo);
                observer.onAdapterDataChanged();
            }
        });

        viewHolder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = todos.indexOf(todo);
                Snackbar undoDeletionSnackbar = Snackbar
                        .make(viewHolder.itemView, todo.getTitle()+" deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                todos.add(position, todo);
                                notifyItemInserted(position);
                                Storage.updateTodosInSharedPreferences(context);
                                observer.onAdapterDataChanged();
                            }
                        });

                undoDeletionSnackbar.show();
                viewHolder.swipeLayout.close();
                notifyItemRemoved(position);
                todos.remove(todo);
                Storage.updateTodosInSharedPreferences(context);
                observer.onAdapterDataChanged();
            }
        });
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
        if(todo.isCompleted() && !showCompleted){
            param.height = 0;
            param.width = 0;
            viewHolder.itemView.setVisibility(View.GONE);
        }else{
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            viewHolder.itemView.setVisibility(View.VISIBLE);
        }
        viewHolder.itemView.setLayoutParams(param);

    }

    public void changeDataSet(List<Todo> data){
        this.todos = data;
        notifyDataSetChanged();
        observer.onAdapterDataChanged();
    }

    public void changeDataSet(){
        notifyDataSetChanged();
        observer.onAdapterDataChanged();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        View doneLayout;
        View deleteLayout;
        TextView todoTime;
        TextView todoTitle;
        ImageView todoIcon;

        public DataObjectHolder(View itemView) {
            super(itemView);
            //set item information into view
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.list_item_swipe_layout);
            todoTime = (TextView) itemView.findViewById(R.id.todo_time);
            todoTitle = (TextView) itemView.findViewById(R.id.todo_title);
            todoIcon = (ImageView) itemView.findViewById(R.id.todo_cathegory_icon);
            doneLayout = itemView.findViewById(R.id.bottom_left_wrapper);
            deleteLayout = itemView.findViewById(R.id.bottom_right_wrapper);
        }
    }

}
