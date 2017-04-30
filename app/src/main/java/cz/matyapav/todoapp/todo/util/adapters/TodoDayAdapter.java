package cz.matyapav.todoapp.todo.util.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemAdapterMangerImpl;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.daimajia.swipe.util.Attributes;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.screen.list.TodoDayViewHolder;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Utils;


/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayAdapter extends RecyclerSwipeAdapter<TodoDayAdapter.DataObjectHolder> {

    private List<Todo> todos;
    private Activity context;

    public TodoDayAdapter(Activity context, List<Todo> todos) {
        this.context = context;
        this.todos = todos;
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

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(todo.getDateAndTimeStart());
        String start = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        calendar.setTime(todo.getDateAndTimeEnd());
        String end = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        viewHolder.todoTime.setText(start + " - " + end);
        viewHolder.todoTitle.setText(todo.getTitle());
        viewHolder.todoIcon.setImageResource(todo.getCathegory().getImgResourceId());
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
                context.startActivity(i);
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
                                notifyItemChanged(pos, todo);
                            }
                        });
                undoCompletedSnackbar.show();
                viewHolder.swipeLayout.close();
                notifyItemChanged(pos, todo);
            }
        });
        viewHolder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todos.remove(todo);
                Snackbar undoDeletionSnackbar = Snackbar
                        .make(viewHolder.itemView, todo.getTitle()+" deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                todos.add(pos, todo);
                                notifyItemInserted(pos);
                            }
                        });

                undoDeletionSnackbar.show();
                viewHolder.swipeLayout.close();
                notifyItemRemoved(pos);
            }
        });
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
