package cz.matyapav.todoapp;

import android.view.View;

/**
 * View holder abstract class
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
