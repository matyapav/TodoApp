package cz.matyapav.todoapp.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.util.enums.SupportedLanguages;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */

class SettingsController {

    FragmentActivity context;
    SettingsViewHolder vh;
    CalendarApiController apiController;


    public SettingsController(FragmentActivity context, SettingsViewHolder vh, SettingsFragment fragment) {
        this.context = context;
        this.vh = vh;
        this.apiController = new CalendarApiController(context, fragment);
    }

    public void initLanguageSelection(){
        vh.language.setText(Locale.getDefault().getDisplayLanguage());
        vh.languageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getResources().getString(R.string.select_language));
                builder.setItems(Utils.getSupportedLanguagesAsArray(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectLanguage(which);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    public void initImportDataSetting(){

        vh.importDataSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] options = {
                        context.getString(R.string.till_end_month),
                        context.getString(R.string.till_end_year),
                        context.getString(R.string.all)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.select_options);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        switch (which){
                            case 0:
                                apiController.setStartDate(calendar.getTime());
                                calendar.add(Calendar.MONTH, 1);
                                apiController.setEndDate(calendar.getTime());
                                break;
                            case 1:
                                apiController.setStartDate(calendar.getTime());
                                calendar.set(Calendar.MONTH, 12);
                                apiController.setEndDate(calendar.getTime());
                                break;
                            case 2:
                                apiController.setStartDate(null);
                                apiController.setEndDate(null);
                            default:
                                break;
                        }
                        apiController.setIsGetRequest(true);
                        apiController.connectToAPI();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();


            }
        });
    }

    private void selectLanguage(int index) {
        vh.language.setText(SupportedLanguages.values()[index].getLangName());
        String lang = SupportedLanguages.values()[index].getLangAbbreviation();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getBaseContext().getResources().updateConfiguration(config, context.getBaseContext().getResources().getDisplayMetrics());
        //reload activity in order to changed language take effect
        Intent i = context.getIntent();
        context.finish();
        i.putExtra(Constants.CURRENT_FRAGMENT, Constants.SETTING_FRAGMENT_TAG);
        context.startActivity(i);
    }


    public void connectToApi(){
        apiController.connectToAPI();
    }

    public void setAccountNameToCredentials(String accountName){
        apiController.mCredential.setSelectedAccountName(accountName);
    }


}
