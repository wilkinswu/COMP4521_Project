//this file is not used! map is in home fragment!

package com.example.comp4521_project;

import android.graphics.Color;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import com.google.android.gms.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.comp4521_project.databinding.FragmentMapsBinding;
//import com.google.maps.android.data.geojson.*;
//import com.google.maps.android.data.geojson.GeoJsonLayer;
//import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng Kyiv = new LatLng(50.431759, 30.517023);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(Kyiv).title("Marker in Kyiv"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Kyiv, 3F));

//        try {
//            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.es_geojson, getApplicationContext());
//
//            GeoJsonPolygonStyle style = layer.getDefaultPolygonStyle();
//            style.setFillColor(Color.MAGENTA);
//            style.setStrokeColor(Color.MAGENTA);
//            style.setStrokeWidth(1F);
//
//            layer.addLayerToMap();
//
//        } catch (IOException ex) {
//            Log.e("IOException", ex.getLocalizedMessage());
//        } catch (JSONException ex) {
//            Log.e("JSONException", ex.getLocalizedMessage());
//        }
    }
}