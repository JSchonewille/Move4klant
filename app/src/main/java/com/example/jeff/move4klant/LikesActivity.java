package com.example.jeff.move4klant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class LikesActivity extends Activity {
    public CheckBox[] checkBoxList;

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
        TableLayout TL = (TableLayout) findViewById(R.id.tableLayoutCategory);
        checkBoxList = new CheckBox[categoryList.length];

        for (int i = 0; i < categoryList.length; i++) {

            // table row
            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            // table cell
            TextView tv = new TextView(this);
            String category = categoryList[i];
            tv.setText(category);
            CheckBox ch = new CheckBox(this);
            checkBoxList[i] = ch;
            checkBoxList[i].setChecked(false);

            // add cell to row
            row.addView(tv);
            row.addView(checkBoxList[i]);
            TL.addView(row,i);
        }


    }

    public void onClickSave(View v)
    {
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
    }

    public void onClickCancel(View v){

        Intent intent = new Intent(this, ManageAccount.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_likes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
