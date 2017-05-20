package cz.matyapav.todoapp.settings;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.ViewHolder;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */

class SettingsViewHolder extends ViewHolder{

    RelativeLayout languageSetting;
    RelativeLayout importDataSetting;
    TextView language;

    public SettingsViewHolder(View context) {
        super(context);
    }

    @Override
    public void findViews() {
        languageSetting = (RelativeLayout) getContext().findViewById(R.id.settings_language_wrapper);
        importDataSetting = (RelativeLayout) getContext().findViewById(R.id.settings_import_data);
        language = (TextView) getContext().findViewById(R.id.settings_language);
    }
}
