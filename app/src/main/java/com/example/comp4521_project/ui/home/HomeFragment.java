package com.example.comp4521_project.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521_project.R;
import com.example.comp4521_project.databinding.FragmentHomeBinding;
import com.example.comp4521_project.databinding.FragmentMapsBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.comp4521_project.databinding.FragmentMapsBinding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import java.util.Map;

public class HomeFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    //private FragmentHomeBinding binding;
    private FragmentMapsBinding binding;
    //private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //private boolean permissionDenied = false;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    Button btnChangeType;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
        View myView = inflater.inflate(R.layout.fragment_home, container,false);
        btnChangeType = myView.findViewById(R.id.btnChangeType);
        btnChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                else
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney / Kyiv
        LatLng sydneyLatLng = new LatLng(-34, 151);
        LatLng KyivLatLng = new LatLng(50.431759, 30.517023);
        mMap.addMarker(new MarkerOptions().position(sydneyLatLng).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(KyivLatLng).title("Kyiv").snippet("Capital of Ukraine!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KyivLatLng, 5));

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                String strLatitude = String.format("%.2f", latLng.latitude);
                String strLongitude = String.format("%.2f", latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng)
                                .title("Post here")
                                .snippet("Location" + "(" + strLatitude + ", " + strLongitude + ")"))
                                .setDraggable(true);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String strLatitude = String.format("%.2f", marker.getPosition().latitude);
                String strLongitude = String.format("%.2f", marker.getPosition().longitude);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Add the buttons
                builder.setTitle("Confirm Location");
                builder.setMessage("Are you sure to make a post in: \n" +
                        "Location" + "(" + strLatitude + ", " + strLongitude + ") ?");

                builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Todo: start a post

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // Set other dialog properties

                // Create the AlertDialog
                builder.create().show();
                return false;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                marker.remove();
            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {

            }

        });
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
//            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, mManifest.permission.ACCESS_FINE_LOCATION, true);
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
        mMap.addMarker(new MarkerOptions()
                .title("Your current location")
                .position(new LatLng(location.getLatitude(), location.getLongitude())));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
        // Check condition
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            // When permission are granted
            // Call  method
            enableMyLocation();
        }
        else {
            // When permission are denied
            // Display toast
            Toast.makeText(getActivity(),"Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onClick(View v) {
//        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
//            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        }
//        else
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//    }

}

