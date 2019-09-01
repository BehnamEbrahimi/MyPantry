package com.example.mypantry;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydneyPosition = new LatLng(-33.813067, 151.0240488);
        mMap.addMarker(new MarkerOptions().position(sydneyPosition).title("Sydney"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-33.818511,151.0190769))
                .title("IGA").icon(BitmapDescriptorFactory.fromResource(R.mipmap.iga)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-33.7955275,151.0450065))
                .title("IGA").icon(BitmapDescriptorFactory.fromResource(R.mipmap.iga)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-33.8086152,151.0066241))
                .title("IGA").icon(BitmapDescriptorFactory.fromResource(R.mipmap.coles)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-33.8244326,151.0790598))
                .title("IGA").icon(BitmapDescriptorFactory.fromResource(R.mipmap.coles)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-33.8146323,151.0544318))
                .title("IGA").icon(BitmapDescriptorFactory.fromResource(R.mipmap.wool)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-33.8126757,151.042099))
                .title("IGA").icon(BitmapDescriptorFactory.fromResource(R.mipmap.aldi)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydneyPosition, 12.0f));


    }
}
