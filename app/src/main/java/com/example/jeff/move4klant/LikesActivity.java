package com.example.jeff.move4klant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import library.Category;
import library.DatabaseHandler;


public class LikesActivity extends Activity {
    private ArrayAdapter<Category> aa;
    private DatabaseHandler db;
    private ArrayList<Category> checkedList = new ArrayList<Category>();
    private ArrayList<Category> savedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHandler(getApplicationContext());
        final ListView listView = (ListView) findViewById(R.id.list_Category);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        savedList = db.getSavedCategories();

        final List<Category> list = new ArrayList<Category>();
        // dummy data
        Category a = new Category(1, "Spijkers");
        Category b = new Category(2, "Hout");
        list.add(a);
        list.add(b);
        aa = new ArrayAdapter<Category>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, list);
        listView.setAdapter(aa);

        // Check if there are already some prefs
        // loop through saved list
        if (savedList.size() > 0) {
            for (int i = 0; i < savedList.size(); i++){
                int id = savedList.get(i).getID();
                // add saved category again to the checked list
                checkedList.add(savedList.get(i));
                // loop through total list to see if the saved if is present, if so, check the checkbox
                for (int ii = 0; ii < list.size(); ii++){
                    if (list.get(ii).getID() == id){
                        listView.setItemChecked(ii, true);
                    }

                }
            }
        }

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
                    checkedList.remove(checkedList.get(i));
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
    public void onBackPressed(){
        finish();
        this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    public void saveLikes(){
        db = new DatabaseHandler(getApplicationContext());
        // clear database with all likes
        db.resetCategory();
        // fill db again with all the likes
        for (int i = 0 ; i < checkedList.size(); i++) {

            Category c = checkedList.get(i);
            String id = "" + c.getID();
            String value = c.getName();

           db.addCategory(id, value);
        }
    }

}
