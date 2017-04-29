package cz.matyapav.todoapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public abstract class ViewHolder {

    View context;

    public ViewHolder(View context) {
        this.context = context;
        findViews();
    }

    public abstract void findViews();

    public View getContext() {
        return context;
    }


}
