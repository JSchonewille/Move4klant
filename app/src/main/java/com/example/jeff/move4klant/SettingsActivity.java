package com.example.jeff.move4klant;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.Toast;

import library.DatabaseHandler;
import library.PrefUtils;

/**
 * Created by Sander on 6-11-2014.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.settings);



        Preference disconnectPref = (Preference) findPreference("prefDisconnectAccount");
        disconnectPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
               disconnectAccount();
                return true;
            }
        });



        Preference manageAccountPref = (Preference) findPreference("prefManageAccount");
        manageAccountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent manAcc = new Intent(getApplicationContext(), ManageAccount.class);
                startActivity(manAcc);
                overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
                return true;
            }
        });
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

    public void disconnectAccount() {
        PrefUtils.saveToPrefs(SettingsActivity.this, getString(R.string.PREFS_LOGIN_USERNAME_KEY), "");
        PrefUtils.saveToPrefs(SettingsActivity.this, getString(R.string.PREFS_LOGIN_PASSWORD_KEY), "");
        PrefUtils.saveToPrefs(SettingsActivity.this, getString(R.string.PREFS_AUTO_LOGIN_KEY), "");
        DatabaseHandler.getInstance(getApplicationContext()).resetUser();
        DatabaseHandler.getInstance(getApplicationContext()).resetCategories();

        Toast.makeText(getApplicationContext(),"Account ontkoppeld", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
