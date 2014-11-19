package com.example.jeff.move4klant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import library.Category;
import library.DatabaseHandler;


public class LikesActivity extends Activity {
    private ArrayAdapter<Category> aa;


    private ArrayList<Category> checkedList = new ArrayList<Category>();
    private List<Category> savedList;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        final ListView listView = (ListView) findViewById(R.id.list_Category);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        savedList = DatabaseHandler.getInstance(getApplicationContext()).getLikedCategories();

        // Checked list
        final ArrayList<Category> list = (ArrayList) DatabaseHandler.getInstance(getApplicationContext()).getAllCategories();
        // dummy data

        aa = new ArrayAdapter<Category>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, list);
        listView.setAdapter(aa);

        // Check if there are already some prefs
        if (savedList.size() > 0) {
            for (int i = 0; i < savedList.size(); i++){
                int id = savedList.get(i).getID();
                checkedList.add(savedList.get(i));
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

                if(listView.isItemChecked(i)) {
                    list.setItemChecked(i, true);
                    checkedList.add(c);
                    aa.notifyDataSetChanged();
                }
                else{
                    int index = 0;
                    list.setItemChecked(i, false);
                    int clickedID = c.getID();
                        for (int ii = 0; ii < checkedList.size(); ii++){
                            Category cc = checkedList.get(ii);
                            if (clickedID == cc.getID()){
                                index = ii;
                            }
                        }
                    checkedList.remove(checkedList.get(index));
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
            case R.id.saveLikes:
                saveLikes();
                Intent i = new Intent(getApplicationContext(), ManageAccount.class);
                startActivity(i);
                finish();
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

    public void saveLikes() {
       int id =DatabaseHandler.getInstance(getApplicationContext()).getUser().getUserID();
       DatabaseHandler.getInstance(getApplicationContext()).saveLikedCategories(id, checkedList);
    }

}
