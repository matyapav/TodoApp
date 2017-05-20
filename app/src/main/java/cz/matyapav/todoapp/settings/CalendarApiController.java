package cz.matyapav.todoapp.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static cz.matyapav.todoapp.util.Constants.REQUEST_ACCOUNT_PICKER;
import static cz.matyapav.todoapp.util.Constants.REQUEST_PERMISSION_GET_ACCOUNTS;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */

public class CalendarApiController {

    GoogleAccountCredential mCredential;
    boolean isGetRequest;
    Date startDate;
    Date endDate;

    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    private Activity context;
    private SettingsFragment fragment;

    public CalendarApiController(Activity context, SettingsFragment fragment) {
        this.context = context;
        mCredential = GoogleAccountCredential.usingOAuth2(context.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        this.fragment = fragment;
    }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    public void connectToAPI() {
        if (!GooglePlayServicesConnector.isGooglePlayServicesAvailable(context)) {
            GooglePlayServicesConnector.acquireGooglePlayServices(context);
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!Utils.isDeviceOnline(context)) {
            Toast.makeText(context, R.string.connection_unavailable, Toast.LENGTH_LONG).show();
        } else {
            if (isGetRequest) {
                new MakeRequestTaskGet(context, fragment, mCredential, startDate, endDate).execute();
            } else {
                //TODO EXPORT
                List<TodoDay> todoDays = new ArrayList<>();
                new MakeRequestTaskSet(context, fragment, mCredential, todoDays).execute();
            }
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) {
                fragment.startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    context,
                    context.getString(R.string.google_account_access),
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
            connectToAPI();
        }
    }


    public void setIsGetRequest(boolean isGetRequest){
        this.isGetRequest = isGetRequest;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}








