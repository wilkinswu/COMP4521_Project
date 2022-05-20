package com.example.comp4521_project.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.Gravity;
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

import com.example.comp4521_project.MainActivity;
import com.example.comp4521_project.R;
import com.example.comp4521_project.databinding.FragmentHomeBinding;

import com.example.comp4521_project.ui.dashboard.DashboardFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Map;

public class HomeFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    //private FragmentHomeBinding binding;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private DashboardFragment dashboardFragment;
    private MainActivity mainActivity;
    Button btnChangeType, btnInstruction;

    public HomeFragment(MainActivity m, DashboardFragment f){
        dashboardFragment = f;
        mainActivity = m;
    }

    public GoogleMap getmMap() {return mMap;}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container,false);
        btnChangeType = myView.findViewById(R.id.btnChangeType);
        btnInstruction = myView.findViewById(R.id.btnInstruction);
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


        btnInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstructionDialog();
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
        showInstructionDialog();
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

                String result = "";
                final String[] items = {"Help","Notification","Warning"};
                AlertDialog.Builder listDialog = new AlertDialog.Builder(getActivity());
                listDialog.setTitle("Please choose your marker type");
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "You have chose " + items[which], Toast.LENGTH_SHORT).show();
                        MarkerOptions markerOptions = new MarkerOptions()
                                .title("Post here")
                                .position(latLng)
                                .snippet("Location" + "(" + strLatitude + ", " + strLongitude + ")")
                                .draggable(true);
                        switch (items[which]) {
                            case "Help":
                                markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_help_24));
                                mMap.addMarker(markerOptions).setTag("Help");
                                break;
                            case "Notification":
                                markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_notifications_24));
                                mMap.addMarker(markerOptions).setTag("Notification");
                                break;
                            case "Warning":
                                markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_warning_24));
                                mMap.addMarker(markerOptions).setTag("Warning");
                                break;
                            default:
                                break;
                        }

                    }
                });
                listDialog.show();
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
                        SharedPreferences share = getActivity().getSharedPreferences("myshare", Context.MODE_PRIVATE);
                        String name = share.getString("username", null);
                        LatLng loc = marker.getPosition();
                        String type = (String) marker.getTag();
                        mainActivity.clickDashboard();
                        dashboardFragment.clickOnInputField(name, loc, type, "");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

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

        try {
            dashboardFragment.refreshPage();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            Toast.makeText(getActivity(),"Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void showInstructionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView title = new TextView(getActivity());
        title.setText("Instruction");
        title.setTextColor(Color.BLACK);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);
        //builder.setTitle("Instruction");
        builder.setMessage("1. Long press to place or delete a marker.\n" +
                "2. You can also click yourself to place a marker.\n" +
                "3. Click on a marker to make a post.");
        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.create().show();
      
    }


    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}

