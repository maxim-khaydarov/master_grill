package mkh.ua.grillbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ASUS on 22.01.2017.
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.text);
            holder.colum = (TextView) row.findViewById(R.id.textView19);

            //holder.colum = (TextView) row.findViewById(R.id.colum);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        ImageItem item = (ImageItem) data.get(position);
        holder.title.setText(item.getTitle());
        holder.colum.setText(item.getColum());
        row.setBackgroundColor(Color.parseColor(item.getImage()));

        //holder.colum.setText(item.getColum());
        return row;
    }

    static class ViewHolder {
        TextView title;
        ImageView image;
        TextView colum;
    }


}