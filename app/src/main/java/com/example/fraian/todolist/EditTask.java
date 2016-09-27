package com.example.fraian.todolist;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class EditTask extends AppCompatActivity {

    public static final String NAME = "name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        TextView name = (TextView) findViewById(R.id.edit_task_name);
        name.setText(getIntent().getStringExtra(NAME));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_setting, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void editEntry(View v) {
        Intent mainIt = new Intent();
        TextView name = (TextView) findViewById(R.id.edit_task_name);
        mainIt.putExtra(NAME, name.getText().toString());
        setResult(RESULT_OK, mainIt);
        this.finish();
    }
}

