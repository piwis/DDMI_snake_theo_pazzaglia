package com.structit.snake.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.structit.snake.R;
import com.structit.snake.Repo.RepoScoreboard;

import java.util.List;

public class ScoreboardAdapter  extends ArrayAdapter<RepoScoreboard> {

    private final Context context;
    private final List<RepoScoreboard> scores;

    public ScoreboardAdapter(Context context, int simple_list_item_1, List<RepoScoreboard> scores) {
        super(context, R.layout.row_scoreboard_view, scores);
        this.context = context;
        this.scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_scoreboard_view, parent, false);

        TextView nameUser = (TextView) rowView.findViewById(R.id.name);
        TextView scoreUser = (TextView) rowView.findViewById(R.id.score);


        nameUser.setText(scores.get(position).getName());
        scoreUser.setText(scores.get(position).getScore());

        return rowView;
        //String s = values[position];
    }
}
