package com.keelim.cnubus.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keelim.cnubus.R;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    // GoogleMapActivity
    private GoogleMap mMap;
    private ArrayList<LatLng> locationList;
    private int location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationListInit();
        intentControl();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null)
            Toast.makeText(this, "오류가 발생을 하였습니다.", Toast.LENGTH_SHORT).show();
        else
            mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) { //구글 맵은 처음 사용을 하는 거니까
        mMap = googleMap;

        for (int index = 0; index < 15; index++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(locationList.get(index))
                    .title(markerHandling(index));
            mMap.addMarker(markerOptions);
        }
        CameraUpdate cameraUpdate;
        if (location == -1) {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationList.get(0), 17);
        } else {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationList.get(location), 17);
        }
        mMap.animateCamera(cameraUpdate);
    }

    // private method
    private String markerHandling(int i) {
        String[] mapArray = getResources().getStringArray(R.array.stations);
        return mapArray[i];
    }

    private void intentControl() {
        Intent intent = getIntent();
        String stringLocation = intent.getStringExtra("location");
        if (stringLocation != null) {
            location = Integer.parseInt(stringLocation);
        } else
            location = -1;
    }

    private void locationListInit() {
        locationList = new ArrayList<>();
        locationList.add(new LatLng(36.363876, 127.345119)); //정삼화
        locationList.add(new LatLng(36.367262, 127.342408)); //한누리관 뒤
        locationList.add(new LatLng(36.368622, 127.341531)); // 서문
        locationList.add(new LatLng(36.374241, 127.343924)); // 음대
        locationList.add(new LatLng(36.376406, 127.344168)); // 공동 동물
        locationList.add(new LatLng(36.372513, 127.343118)); //체육관 입구
        locationList.add(new LatLng(36.370587, 127.343520)); // 예술대학앞
        locationList.add(new LatLng(36.369522, 127.346725)); // 도서관앞
        locationList.add(new LatLng(36.369119, 127.351884)); //농업생명과학대학
        locationList.add(new LatLng(36.367465, 127.352190)); //동문
        locationList.add(new LatLng(36.372480, 127.346155)); //생활관
        locationList.add(new LatLng(36.369780, 127.346901)); //도서관앞
        locationList.add(new LatLng(36.367404, 127.345517)); //공과대학앞
        locationList.add(new LatLng(36.365505, 127.345159)); //산학협력관
        locationList.add(new LatLng(36.367564, 127.345800)); //경상대학
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}