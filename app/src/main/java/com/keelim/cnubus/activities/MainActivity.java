package com.keelim.cnubus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.keelim.cnubus.R;

public class MainActivity extends AppCompatActivity {
    public SharedPreferences preferepnces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferepnces = getSharedPreferences("Pref", MODE_PRIVATE);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_setting, R.id.navigation_aroot, R.id.navigation_broot, R.id.navigation_croot)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //update manager
        // Creates instance of the manager.
        checkUpdate(getApplicationContext());
        mOnPopupClick();


    }

    public void checkUpdate(Context context) {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) { //업데이트 상태인지를 확인하고 업데이트

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            MainActivity.this,
                            300);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d("Support in-app-update", "UPDATE_NOT_AVAILABLE");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.drawer_aroot:
                Intent intent_aroot = new Intent(getApplicationContext(), ARootActivity.class);
                startActivity(intent_aroot);
                break;
            case R.id.drawer_broot:
                Intent intent_broot = new Intent(getApplicationContext(), BRootActivity.class);
                startActivity(intent_broot);
                break;
            case R.id.drawer_croot:
                Intent intent_croot = new Intent(getApplicationContext(), CRootActivity.class);
                startActivity(intent_croot);
                break;
            case R.id.drawer_night:
                Intent intent_night = new Intent(getApplicationContext(), NightRootActivity.class);
                startActivity(intent_night);
                break;

            case R.id.drawer_developer:
                Intent intent_developer = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent_developer);
                break;
        }


        return super.onOptionsItemSelected(item);

    }


    public void mOnPopupClick() {
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
            }
        }
    }


}
