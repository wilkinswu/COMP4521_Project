package com.example.comp4521_project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521_project.R;
import com.example.comp4521_project.databinding.FragmentHomeBinding;
import com.example.comp4521_project.databinding.FragmentMapsBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.comp4521_project.databinding.FragmentMapsBinding;

import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    //private FragmentHomeBinding binding;
    private FragmentMapsBinding binding;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
        return inflater.inflate(R.layout.fragment_home, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng Kyiv = new LatLng(50.431759, 30.517023);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(Kyiv).title("Kyiv"));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}