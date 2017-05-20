package cz.matyapav.todoapp.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.audiofx.BassBoost;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.util.enums.SupportedLanguages;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.SimpleDividerItemDecoration;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */

class SettingsController {

    FragmentActivity context;
    SettingsViewHolder vh;

    public SettingsController(FragmentActivity context, SettingsViewHolder vh) {
        this.context = context;
        this.vh = vh;
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
}
