package cz.matyapav.todoapp.todo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Cathegory;

/**
 * CategoryAdapter - used to create Spinner with category items
 */
public class CategoryAdapter extends ArrayAdapter<Cathegory> {

    private List<Cathegory> cathegories;

    public CategoryAdapter(Context context, List<Cathegory> cathegories) {
        super(context, 0, cathegories);
        this.cathegories = cathegories;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent){
        final Cathegory cathegory = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_item, parent, false);
        }

        TextView categoryName = (TextView) convertView.findViewById(R.id.category_spinner_name);
        ImageView categoryIcon = (ImageView) convertView.findViewById(R.id.category_spinner_icon);

        categoryName.setText(cathegory.getCathegoryName());
        categoryIcon.setImageResource(cathegory.getImgResourceId());

        return convertView;
    }
    @Override
    public int getCount() {
        return cathegories.size();
    }

    @Override
    public Cathegory getItem(int position) {
        return cathegories.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

}
