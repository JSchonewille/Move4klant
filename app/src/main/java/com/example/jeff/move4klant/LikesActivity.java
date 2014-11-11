package com.example.jeff.move4klant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import library.Category;
import library.DatabaseHandler;


public class LikesActivity extends Activity {
    private ArrayAdapter<Category> aa;
    private DatabaseHandler db;


    private ArrayList<Category> checkedList = new ArrayList<Category>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        final ListView listView = (ListView) findViewById(R.id.list_Category);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Total list
        ArrayList<Category> savedList = db.getSavedCategories();

        for (int i = 0; i < savedList.size(); i++){
            Category c = savedList.get(i);
            String id = String.valueOf(c.getID());
            String name = c.getName();
            Log.v("ID: ", id);
            Log.v("Name: ", name);
        }
        // Checked list
        final List<Category> list = new ArrayList<Category>();
        // dummy data
        Category a = new Category(1, "Spijkers");
        Category b = new Category(2, "Hout");
        list.add(a);
        list.add(b);
        aa = new ArrayAdapter<Category>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, list);
        listView.setAdapter(aa);

        // Check if there are already some prefs

        if (savedList.size() > 0) {
            for (int i = 0; i < savedList.size(); i++){
                int id = savedList.get(i).getID();
                for (int ii = 0; ii < list.size(); ii++){
                    if (list.get(ii).getID() == id){
                        listView.setItemChecked(ii, true);
//                        View view = listView.getAdapter().getView(ii, listView, listView);
//                        CheckedTextView check = ((CheckedTextView)view);
//                        check.setChecked(true);
                    }

                }
            }
            aa.notifyDataSetChanged();
        }

//        CategoryRequest.getAllCategories(new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray jsonArray) {
//                List<Category> list = Category.fromJSON(jsonArray);
//                listView.setAdapter(new ArrayAdapter<Category>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, list));
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView list = (ListView) adapterView;
                Category c = (Category)listView.getAdapter().getItem(i);
                CheckedTextView check = ((CheckedTextView)view);

                if(listView.isItemChecked(i)) {
                    list.setItemChecked(i, true);
                    checkedList.add(c);
                    aa.notifyDataSetChanged();
                }
                else{
                    list.setItemChecked(i, false);
                    checkedList.remove(c);
                    aa.notifyDataSetChanged();
                }
            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_likes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    public void onClick(View v){
        db = new DatabaseHandler(getApplicationContext());
        switch(v.getId()){
            case R.id.save:
                if(checkedList.size() == 0)
                {
                    db.resetCategory();
                }
                for (int i = 0 ; i < checkedList.size(); i++) {

                    Category c = checkedList.get(i);
                    String id = "" + c.getID();
                    String value = c.getName();

                   db.addCategory(id, value);
                }
                Toast.makeText(getApplicationContext(), "Saved succesful", Toast.LENGTH_SHORT).show();
                Intent inn = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(inn);
                break;
            case R.id.cancel:
                Intent in = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(in);
                finish();
                break;
        }
    }
}
