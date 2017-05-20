package cz.matyapav.todoapp.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.matyapav.todoapp.R;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class SettingsFragment extends Fragment {

    SettingsController controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.action_settings));
        setHasOptionsMenu(false);

        controller = new SettingsController(getActivity(), new SettingsViewHolder(view));
        controller.initLanguageSelection();

        return view;
    }

}
