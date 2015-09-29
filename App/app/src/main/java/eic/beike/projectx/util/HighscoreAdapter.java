package eic.beike.projectx.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import eic.beike.projectx.R;

/**
 * Created by Adam on 2015-09-29.
 */
public class HighscoreAdapter extends ArrayAdapter<ScoreEntry> {

    ScoreEntry[] values;
    Context context;

    public HighscoreAdapter(Context context, ScoreEntry[] values) {
        super(context, R.layout.highscore_row, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.highscore_row, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        TextView scoreView = (TextView) rowView.findViewById(R.id.score);

        ScoreEntry entry = values[position];

        nameView.setText(entry.getName());
        scoreView.setText(entry.getScore());

        return rowView;
    }

}
