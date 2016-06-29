package com.abs.samih.fcm_ex01;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnGo,btnSave,btnCancel;
    private EditText etToSearch;
    private LatLng loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnGo = (Button) findViewById(R.id.btnGo);
        btnSave = (Button) findViewById(R.id.btnSave);
        etToSearch = (EditText) findViewById(R.id.etToSearch);

        btnGo.setOnClickListener(clickListener);
        btnSave.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnGo:
                    search(etToSearch.getText().toString());
                    break;
                case R.id.btnSave:
                    if (loc!=null) {
                        Intent intent = new Intent();

                        intent.putExtra("lat",loc.latitude);
                        intent.putExtra("lng",loc.longitude);
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                    break;
                case R.id.btnCancel:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
            }

        }
    };

    private void search(final String s) {
        AsyncTask<Void,Integer,List<Address>> asyncTask=new AsyncTask<Void, Integer, List<Address>>() {
            List<Address> locations;
            Geocoder geocoder;

            @Override
            protected void onPreExecute() {
                locations =  null;
                geocoder = new Geocoder(MapsActivity2.this, Locale.getDefault());
                super.onPreExecute();
            }

            @Override
            protected List<Address> doInBackground(Void... params) {

                try {
                    locations = geocoder.getFromLocationName(s,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return locations;
            }
            //5
            @Override
            protected void onPostExecute(List<Address> addresses) {
                super.onPostExecute(addresses);

                for (Address a:addresses)
                {
                    //6
                    loc = new LatLng(a.getLatitude(),a.getLongitude());
                    MarkerOptions m = new MarkerOptions().position(loc).title(s);
                    mMap.addMarker(m);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16));
                }
            }
        } ;
        //7
        asyncTask.execute();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
