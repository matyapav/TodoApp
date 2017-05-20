package cz.matyapav.todoapp.todo.util.validators;

import java.util.Calendar;
import java.util.Date;

import cz.matyapav.todoapp.todo.screen.create.CreateTodoViewHolder;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoValidator {

    CreateTodoViewHolder vh;

    public TodoValidator(CreateTodoViewHolder vh) {
        this.vh = vh;
    }

    public boolean validateTitle(String todoName){
        if(todoName.isEmpty()){
            vh.todoTitle.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.todoTitle.setError(null);
        }
        return false;
    }

    public boolean validateStartDateEmpty(String dateStartStr){
        if(dateStartStr.isEmpty()){
            vh.createTodoDateStart.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoDateStart.setError(null);
        }
        return false;
    }

    public boolean validateEndDateEmpty(String dateEndStr){
        if(dateEndStr.isEmpty()){
            vh.createTodoDateEnd.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoDateEnd.setError(null);
        }
        return false;
    }

    public boolean validateStartTimeEmpty(String startTime){
        if(startTime.isEmpty()){
            vh.createTodoStartTime.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoStartTime.setError(null);
        }
        return false;
    }

    public boolean validateEndTimeEmpty(String endTime){
        if(endTime.isEmpty()){
            vh.createTodoEndTime.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoEndTime.setError(null);
        }
        return false;
    }

    public boolean validateEndTimeBeforeStartTime(Date dateStart, Date dateEnd){
        if(dateStart != null && dateEnd != null) {
            if(dateStart.compareTo(dateEnd) >= 0){
                vh.createTodoStartTime.setError(vh.getContext().getResources().getString(Constants.START_AFTER_END));
                vh.createTodoEndTime.setError(vh.getContext().getResources().getString(Constants.START_AFTER_END));
                vh.createTodoDateStart.setError(vh.getContext().getResources().getString(Constants.START_AFTER_END));
                vh.createTodoDateEnd.setError(vh.getContext().getResources().getString(Constants.START_AFTER_END));
                return true;
            }else{
                vh.createTodoStartTime.setError(null);
            }
        }
        return false;
    }
}
