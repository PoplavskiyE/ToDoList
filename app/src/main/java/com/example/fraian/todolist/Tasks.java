package com.example.fraian.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.example.fraian.todolist.EditTask.NAME;

public class Tasks extends AppCompatActivity implements OnItemLongClickListener,
        OnItemClickListener {
    private String selectedName;
    private DBAdapter mySQLiteAdapter;
    public static final String OPTIONS = "Options";
    public static final String EDIT = "Edit";
    public static final String DELETE = "Delete";
    public static final String RETURN = "Return";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        refresh();
        ListView listView = (ListView) findViewById(R.id.tasksList);
        listView.setOnItemLongClickListener(this);
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_setting, menu);
        return true;
    }

    public void addTask(View v) {
        Intent tasksInt = new Intent(this, AddTask.class);
        startActivityForResult(tasksInt, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                mySQLiteAdapter = new DBAdapter(this);
                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.insert(data.getStringExtra(NAME));
                mySQLiteAdapter.close();
                refresh();
            }
            if (requestCode == 2) {
                mySQLiteAdapter = new DBAdapter(this);
                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.updateInfo(data.getStringExtra(NAME),
                        selectedName);
                mySQLiteAdapter.close();
                refresh();
            }
        }
    }

    public void refresh() {
        mySQLiteAdapter = new DBAdapter(this);
        mySQLiteAdapter.openToRead();
        ListView listView = (ListView) findViewById(R.id.tasksList);
        mySQLiteAdapter.refreshContacts(listView);
        mySQLiteAdapter.close();
    }

    public boolean onItemLongClick(AdapterView<?> arg0, View v, int arg2,
                                   long arg3) {
        RelativeLayout l = (RelativeLayout) v;
        TextView name = (TextView) l.getChildAt(0);

        selectedName = String.valueOf(name.getText());

        mySQLiteAdapter = new DBAdapter(this);
        mySQLiteAdapter.openToRead();

        return false;
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(OPTIONS);
        menu.add(0, v.getId(), 0, EDIT);
        menu.add(0, v.getId(), 0, DELETE);
        menu.add(0, v.getId(), 0, RETURN);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == EDIT) {
            Intent contactsIt = new Intent(this, EditTask.class);
            contactsIt.putExtra(NAME, selectedName);

            startActivityForResult(contactsIt, 2);
        } else if (item.getTitle() == DELETE) {
            mySQLiteAdapter = new DBAdapter(this);
            mySQLiteAdapter.openToWrite();
            mySQLiteAdapter.deleteContact(selectedName);
            mySQLiteAdapter.close();
            refresh();
        } else if (item.getTitle() == RETURN) {
            return false;
        } else {
            return false;
        }
        return true;
    }
}
