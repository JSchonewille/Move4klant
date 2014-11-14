package com.example.jeff.move4klant;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.Toast;

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

        Preference offersPref = (Preference) findPreference("prefOffers");
        offersPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

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
        PrefUtils.getFromPrefs(SettingsActivity.this, getString(R.string.PREFS_AUTO_LOGIN_KEY), "false");

        Toast.makeText(getApplicationContext(),"Account ontkoppeld", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

}
