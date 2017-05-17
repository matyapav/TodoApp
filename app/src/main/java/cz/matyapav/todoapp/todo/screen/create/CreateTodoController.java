package cz.matyapav.todoapp.todo.screen.create;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Cathegory;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.util.adapters.CategoryAdapter;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;
import cz.matyapav.todoapp.util.Storage;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class CreateTodoController {

    Activity context;
    CreateTodoViewHolder vh;

    public CreateTodoController(Activity context, CreateTodoViewHolder vh) {
        this.context = context;
        this.vh = vh;
    }

    void initCategorySpinner() {
        vh.cathegorySpinner.setAdapter(new CategoryAdapter(context, Storage.getDummyCategories()));
    }

    void setFabAction() {
        vh.finishFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO zpracuj data a uloz
                context.onBackPressed();
            }
        });
    }

    void setListenersToPriorityButtons() {
        vh.lowPrioWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.lowPrioIcon.setVisibility(View.VISIBLE);
                vh.medPrioIcon.setVisibility(View.INVISIBLE);
                vh.highPrioIcon.setVisibility(View.INVISIBLE);
            }
        });
        vh.medPrioWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.lowPrioIcon.setVisibility(View.INVISIBLE);
                vh.medPrioIcon.setVisibility(View.VISIBLE);
                vh.highPrioIcon.setVisibility(View.INVISIBLE);
            }
        });
        vh.highPrioWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.lowPrioIcon.setVisibility(View.INVISIBLE);
                vh.medPrioIcon.setVisibility(View.INVISIBLE);
                vh.highPrioIcon.setVisibility(View.VISIBLE);
            }
        });
    }

    void setListenersToDateAndTimePickers() {
        vh.createTodoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        vh.createTodoStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });
        vh.createTodoEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });
    }

    void setBackgroundColorsToPriorityBtns() {
        vh.lowPrioWrapper.setBackgroundColor(Utils.getColor(context, TodoPriority.LOW.getColorId()));
        vh.medPrioWrapper.setBackgroundColor(Utils.getColor(context, TodoPriority.MEDIUM.getColorId()));
        vh.highPrioWrapper.setBackgroundColor(Utils.getColor(context, TodoPriority.HIGH.getColorId()));
    }

    private void showTimePickerDialog(final boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (isStartTime) {
                    vh.createTodoStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                } else {
                    vh.createTodoEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                vh.createTodoDate.setText(Utils.dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void fillTodoIntoView(Todo todo) {
        vh.todoTitle.setText(todo.getTitle());
        selectPriority(todo.getPriority());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todo.getDateAndTimeStart());
        vh.createTodoDate.setText(Utils.dateFormatter.format(calendar.getTime()));
        vh.createTodoStartTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        calendar.setTime(todo.getDateAndTimeEnd());
        vh.createTodoEndTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        vh.cathegorySpinner.setSelection(Utils.getValueSpinnerPosition(vh.cathegorySpinner, todo.getCathegory().getCathegoryName()));
        vh.notification.setChecked(todo.isNotification());
        vh.description.setText(todo.getDescription());
    }

    private void selectPriority(TodoPriority priority) {
        if (priority.equals(TodoPriority.LOW)) {
            vh.lowPrioIcon.setVisibility(View.VISIBLE);
        } else if (priority.equals(TodoPriority.MEDIUM)) {
            vh.medPrioIcon.setVisibility(View.VISIBLE);
        } else if (priority.equals(TodoPriority.HIGH)){
            vh.highPrioIcon.setVisibility(View.VISIBLE);
        }else{
            throw new IllegalArgumentException("Priority does not exists");
        }
    }


}
