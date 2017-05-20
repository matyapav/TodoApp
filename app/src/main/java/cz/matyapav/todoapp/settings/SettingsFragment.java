package cz.matyapav.todoapp.settings;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import cz.matyapav.todoapp.R;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static cz.matyapav.todoapp.util.Constants.REQUEST_ACCOUNT_PICKER;
import static cz.matyapav.todoapp.util.Constants.REQUEST_AUTHORIZATION;
import static cz.matyapav.todoapp.util.Constants.REQUEST_GOOGLE_PLAY_SERVICES;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class SettingsFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    SettingsController controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.action_settings));
        setHasOptionsMenu(false);

        controller = new SettingsController(getActivity(), new SettingsViewHolder(view), this);
        controller.initLanguageSelection();
        controller.initImportDataSetting();
        // Initialize credentials and service object.

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getActivity(), R.string.install_play, Toast.LENGTH_LONG);
                } else {
                    controller.connectToApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        controller.setAccountNameToCredentials(accountName);
                        controller.connectToApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    controller.connectToApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //do nothing
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //do nothing
    }
}
