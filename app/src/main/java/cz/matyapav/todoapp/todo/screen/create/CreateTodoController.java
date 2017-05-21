package cz.matyapav.todoapp.todo.screen.create;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import cz.matyapav.todoapp.todo.model.Cathegory;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.adapters.CategoryAdapter;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;
import cz.matyapav.todoapp.todo.util.validators.TodoValidator;
import cz.matyapav.todoapp.util.Storage;
import cz.matyapav.todoapp.util.Utils;

/**
 * Controls CreateTodo activity
 */
public class CreateTodoController {

    Activity context;
    CreateTodoViewHolder vh;
    Todo editedTodo;

    public CreateTodoController(Activity context, CreateTodoViewHolder vh, Todo editedTodo) {
        this.context = context;
        this.vh = vh;
        this.editedTodo = editedTodo;
    }

    /**
     * Requests focus on tudu title
     */
    void autoFocusTodoTile(){
        vh.todoTitle.requestFocus();
    }

    /**
     * Initializes category spinner
     */
    void initCategorySpinner() {
        vh.cathegorySpinner.setAdapter(new CategoryAdapter(context, Storage.getDummyCategories()));
    }

    /**
     * Sets action on floating action button
     */
    void setFabAction() {
        vh.finishFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTodo();
            }
        });
    }

    /**
     * Creates tudu - gets data from view, validates them and save tudu in Storage
     * (or display errors if validation failed)
     */
    void createTodo(){
        //todoName
        Todo todo = new Todo();
        TodoValidator validator = new TodoValidator(vh);
        boolean errors;

        String todoName = vh.todoTitle.getText().toString();
        String dateStartStr = vh.createTodoDateStart.getText().toString();
        String dateEndStr = vh.createTodoDateEnd.getText().toString();
        String startTime = vh.createTodoStartTime.getText().toString();
        String endTime = vh.createTodoEndTime.getText().toString();
        TodoPriority priority = null;
        if(vh.lowPrioIcon.getVisibility() == View.VISIBLE){
            priority = TodoPriority.LOW;
        }else if(vh.medPrioIcon.getVisibility() == View.VISIBLE){
            priority = TodoPriority.MEDIUM;
        }else if(vh.highPrioIcon.getVisibility() == View.VISIBLE){
            priority = TodoPriority.HIGH;
        }
        Date dateStart =Utils.parseDate(dateStartStr);
        Date dateEnd = Utils.parseDate(dateEndStr);
        if(dateStart != null && dateEnd != null && !startTime.isEmpty() && !endTime.isEmpty()) {
            int startTimeHour = Utils.parseHourFromString(startTime);
            int startTimeMinutes = Utils.parseMinutesFromString(startTime);
            int endTimeHour = Utils.parseHourFromString(endTime);
            int endTImeMinutes = Utils.parseMinutesFromString(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateStart);
            calendar.set(Calendar.HOUR_OF_DAY, startTimeHour);
            calendar.set(Calendar.MINUTE, startTimeMinutes);
            dateStart = calendar.getTime();
            calendar.setTime(dateEnd);
            calendar.set(Calendar.HOUR_OF_DAY, endTimeHour);
            calendar.set(Calendar.MINUTE, endTImeMinutes);
            dateEnd = calendar.getTime();
        }

        errors = validator.validateTitle(todoName) |
                validator.validateStartDateEmpty(dateStartStr) |
                validator.validateStartTimeEmpty(startTime) |
                validator.validateEndDateEmpty(dateEndStr) |
                validator.validateEndTimeEmpty(endTime) |
                validator.validateEndTimeBeforeStartTime(dateStart, dateEnd);

        if(!errors) {
            todo.setTitle(todoName);
            todo.setPriority(priority);
            todo.setDateAndTimeStart(dateStart);
            todo.setDateAndTimeEnd(dateEnd);
            Cathegory cathegory = (Cathegory) vh.cathegorySpinner.getSelectedItem();
            todo.setCathegory(cathegory);
            todo.setNotification(vh.notification.isChecked());
            todo.setDescription(vh.description.getText().toString());
            saveTodo(todo);
        }
    }

    /**
     * Saved tudu into Storage
     * @param todo
     */
    void saveTodo(Todo todo){
        boolean successfullySaved = false;
        if(editedTodo == null) {
            successfullySaved = Storage.addNewTodo(todo, context);
        }else{
            successfullySaved = Storage.editTodo(editedTodo.getId(), todo, context);
        }
        if(successfullySaved) {
            Storage.updateTodosInSharedPreferences(context);
        }
        context.setResult(Activity.RESULT_OK);
        context.finish();
    }

    /**
     * Sets onclick listeners on priority buttons
     */
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

    /**
     * Initializes onclick listeners to date and time pickers
     */
    void setListenersToDateAndTimePickers() {
        vh.createTodoDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });
        vh.createTodoDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
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

    /**
     * Sets background colors to priority buttons according to their priority
     */
    void setBackgroundColorsToPriorityBtns() {
        vh.lowPrioWrapper.setBackgroundColor(Utils.getColor(context, TodoPriority.LOW.getColorId()));
        vh.medPrioWrapper.setBackgroundColor(Utils.getColor(context, TodoPriority.MEDIUM.getColorId()));
        vh.highPrioWrapper.setBackgroundColor(Utils.getColor(context, TodoPriority.HIGH.getColorId()));
    }

    /**
     * Shows time picker dialog
     * @param isStartTime
     */
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
        timePickerDialog.setCancelable(false);
        timePickerDialog.show();
    }

    /**
     * Shows date picker dialog
     * @param isStartDate
     */
    private void showDatePickerDialog(final boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if(isStartDate) {
                    vh.createTodoDateStart.setText(Utils.dateFormatter.format(newDate.getTime()));
                }else{
                    vh.createTodoDateEnd.setText(Utils.dateFormatter.format(newDate.getTime()));
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    /**
     * Preffils tudu into view
     * @param todo
     */
    public void fillTodoIntoView(Todo todo) {
        vh.todoTitle.setText(todo.getTitle());
        selectPriority(todo.getPriority());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todo.getDateAndTimeStart());
        vh.createTodoDateStart.setText(Utils.dateFormatter.format(calendar.getTime()));
        vh.createTodoStartTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        //Todo zvazit jestli chceme disablovat
        vh.createTodoDateStart.setEnabled(false);
        vh.createTodoDateEnd.setEnabled(false);
        calendar.setTime(todo.getDateAndTimeEnd());
        vh.createTodoDateEnd.setText(Utils.dateFormatter.format(calendar.getTime()));
        vh.createTodoEndTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        if(todo.getCathegory() != null) {
            vh.cathegorySpinner.setSelection(Utils.getValueSpinnerPosition(vh.cathegorySpinner, todo.getCathegory().getCathegoryName()));
        }
        vh.notification.setChecked(todo.isNotification());
        vh.description.setText(todo.getDescription());
    }

    /**
     * Selects priority
     * @param priority
     */
    private void selectPriority(TodoPriority priority) {
        if (priority.equals(TodoPriority.LOW)) {
            vh.lowPrioIcon.setVisibility(View.VISIBLE);
            vh.medPrioIcon.setVisibility(View.INVISIBLE);
            vh.highPrioIcon.setVisibility(View.INVISIBLE);
        } else if (priority.equals(TodoPriority.MEDIUM)) {
            vh.lowPrioIcon.setVisibility(View.INVISIBLE);
            vh.medPrioIcon.setVisibility(View.VISIBLE);
            vh.highPrioIcon.setVisibility(View.INVISIBLE);
        } else if (priority.equals(TodoPriority.HIGH)){
            vh.lowPrioIcon.setVisibility(View.INVISIBLE);
            vh.medPrioIcon.setVisibility(View.INVISIBLE);
            vh.highPrioIcon.setVisibility(View.VISIBLE);
        }else{
            throw new IllegalArgumentException("Priority does not exists");
        }
    }


    /**
     * Prefills start and end date
     * @param currentDate
     */
    public void prefillStartAndEndDate(String currentDate) {
        vh.createTodoDateStart.setText(currentDate);
        vh.createTodoDateEnd.setText(currentDate);
    }
}
