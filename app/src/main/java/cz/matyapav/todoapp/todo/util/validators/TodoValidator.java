package cz.matyapav.todoapp.todo.util.validators;

import java.util.Date;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoViewHolder;
import cz.matyapav.todoapp.util.Constants;

/**
 * Validator for Tudu
 */
public class TodoValidator {

    CreateTodoViewHolder vh;

    public TodoValidator(CreateTodoViewHolder vh) {
        this.vh = vh;
    }

    /**
     * Checks whether title of tudu is empty or not
     * @param todoName
     * @return true if it is empty, false otherwise
     */
    public boolean validateTitle(String todoName){
        if(todoName.isEmpty()){
            vh.todoTitle.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.todoTitle.setError(null);
        }
        return false;
    }

    /**
     * Checks whether start date of tudu is empty
     * @param dateStartStr
     * @return true if it is empty, false otherwise
     */
    public boolean validateStartDateEmpty(String dateStartStr){
        if(dateStartStr.isEmpty()){
            vh.createTodoDateStart.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoDateStart.setError(null);
        }
        return false;
    }

    /**
     * Checks whether end date of tudu is empty
     * @param dateEndStr
     * @return true if it is empty, false otherwise
     */
    public boolean validateEndDateEmpty(String dateEndStr){
        if(dateEndStr.isEmpty()){
            vh.createTodoDateEnd.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoDateEnd.setError(null);
        }
        return false;
    }

    /**
     * Checks whether start time of tudu is empty
     * @param startTime
     * @return true if it is empty, false otherwise
     */
    public boolean validateStartTimeEmpty(String startTime){
        if(startTime.isEmpty()){
            vh.createTodoStartTime.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoStartTime.setError(null);
        }
        return false;
    }

    /**
     * Checks whether end time of tudu is empty
     * @param endTime
     * @return true if it is empty, false otherwise
     */
    public boolean validateEndTimeEmpty(String endTime){
        if(endTime.isEmpty()){
            vh.createTodoEndTime.setError(vh.getContext().getResources().getString(Constants.VALIDATION_REQUIRED));
            return true;
        }else{
            vh.createTodoEndTime.setError(null);
        }
        return false;
    }

    /**
     * Checks whether start date and time is before end date and time
     * @param dateStart
     * @param dateEnd
     * @return true if start date and time is after end date and time, false otherwise
     */
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
