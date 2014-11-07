package com.example.jeff.move4klant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class LikesActivity extends Activity {
    public CheckBox[] checkBoxList;

    private ArrayList<Category> list;

    public static String[] categoryList = {
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<Category>();

        final ListView listView = (ListView)findViewById(R.id.list_Category);

        // fill category list
        for (int i = 0; i < categoryList.length-1; i++) {
            list.add(new Category(i, categoryList[i]));
        }

        listView.setAdapter(new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_multiple_choice, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category c = (Category)listView.getAdapter().getItem(i);
                Toast.makeText(getApplicationContext(), c.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.save:
                String message = "";
                String checked;
                for (int i = 0; i < categoryList.length; i++)
                {
                    if (checkBoxList[i].isChecked()){
                        checked = "true";
                    }
                    else{
                        checked = "false";
                    }
                    message = message + categoryList[i] + ": " + checked + "\n";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                break;
            case R.id.cancel:
                onBackPressed();
                break;
        }
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
}
