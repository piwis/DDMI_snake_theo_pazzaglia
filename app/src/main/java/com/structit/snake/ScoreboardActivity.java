package com.structit.snake;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import com.structit.snake.Adapter.ScoreboardAdapter;
import com.structit.snake.Repo.RepoScoreboard;
import com.structit.snake.utils.SingletonScoreboard;

public class ScoreboardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mListView;
    ArrayAdapter<RepoScoreboard> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("title");
        }

        mListView = (ListView) findViewById(R.id.scoreboardView);

        mListView.setOnItemClickListener(this);


        adapter = new ScoreboardAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, SingletonScoreboard.getInstance().getArray());
        mListView.setAdapter(adapter);

    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
