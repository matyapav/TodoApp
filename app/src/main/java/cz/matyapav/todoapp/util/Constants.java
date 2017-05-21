package cz.matyapav.todoapp.util;

import cz.matyapav.todoapp.R;

/**
 * Constants used in application
 */
public class Constants {

    //Bundle and intent keys
    public static final String PREFILLED_TODO = "prefilled_todo";
    public static final String CURRENT_DATE = "currentDate";

    //Shared preferences keys
    public static final String PREFERENCE_FILE_KEY = "preference_file";
    public static final String PREFS_SHOW_COMPLETED = "prefs_show_completed";
    public static final String TODO_ID = "unique_todo_id";
    public static final String TODOS = "todos";

    //Validation keys
    public static final int VALIDATION_REQUIRED = R.string.validation_empty;
    public static final int START_AFTER_END = R.string.start_after_end;

    //Fragment tags
    public static final String SETTING_FRAGMENT_TAG = "settings_fragment";
    public static final String CURRENT_FRAGMENT = "current_fragment";

    //Request codes - used when starting activities for result
    public static final int TODO_CREATE_EDIT_REQUEST_CODE = 25;
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
}
