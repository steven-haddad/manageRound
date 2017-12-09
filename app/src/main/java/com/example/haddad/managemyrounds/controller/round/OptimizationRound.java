package com.example.haddad.managemyrounds.controller.round;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.singleton.AppSingleton;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.optimizedtrips.v1.MapboxOptimizedTrips;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.services.Constants.PRECISION_6;
import static java.lang.Boolean.TRUE;

import com.mapbox.mapboxsdk.annotations.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Use Mapbox Android Services to request and compare normal directions with time optimized directions.
 */
public class OptimizationRound extends AppCompatActivity implements LocationEngineListener, PermissionsListener {

    private MapView mapView;
    private DirectionsRoute optimizedRoute;
    private MapboxOptimizedTrips optimizedClient;
    private Polyline optimizedPolyline;
    private List<Position> stops;
    private Position origin;
    private static final String FIRST = "first";
    private static final String ANY = "any";
    private static final String TEAL_COLOR = "#23D2BE";
    private static final int POLYLINE_WIDTH = 5;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;
    private Location originLocation;
    private Button button;
    private static final String TAG = "DisplayFacesCoordinates";
    JSONArray latitude;
    JSONArray longitude;
    int arrSize;
    String selectedPeriod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_test);

        // Initialize list of Position objects and add the origin Position to the list
        getPostingPeriod();
        getFacesCoordinates();
        initializeListOfStops();

        // Setup the MapView
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                // Add origin and destination to the map
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(origin.getLatitude(), origin.getLongitude()))
                        .title("Nouveau marker"));

                    addDestinationMarker();
                    addPointToStopsList();
                    getOptimizedRoute(stops);

                enableLocationPlugin();


                map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        // Optimization API is limited to 12 coordinate sets

                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.mapboxBlue);

                    }
                });
                button = findViewById(R.id.startButton);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // Pass in your Amazon Polly pool id for speech synthesis using Amazon Polly
                        // Set to null to use the default Android speech synthesizer
                        String awsPoolId = null;
                        boolean simulateRoute = true;
                        NavigationLauncher.startNavigation(OptimizationRound.this, optimizedRoute, null, false);
                        DirectionsRoute  op=optimizedRoute;
                        Log.i("te","d"+op);
                    }
                });
            }
        });
    }



    private void addDestinationMarker()  {

        for(int i=0;i<arrSize;i++) {

            try {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude.getDouble(i),longitude.getDouble(i)))
                        .title("ajout d'un marker"+i));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void addPointToStopsList()  {

        for(int i=0;i<arrSize;i++) {

            try {
                stops.add(Position.fromCoordinates(longitude.getDouble(i), latitude.getDouble(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeListOfStops() {
        // Set up stop list
        stops = new ArrayList<>();
        // Set first stop
        origin = Position.fromCoordinates(2.3773043, 48.845496700000005);
        stops.add(origin);
    }

    private DirectionsRoute getOptimizedRoute(List<Position> coordinates) {
        optimizedClient = new MapboxOptimizedTrips.Builder()
                .setSource(FIRST)
                .setDestination(ANY)
                .setSteps(TRUE)
                .setLanguage("French")
                .setCoordinates(coordinates)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setAccessToken(Mapbox.getAccessToken())

                .build();

        optimizedClient.enqueueCall(new Callback<OptimizedTripsResponse>() {
            @Override
            public void onResponse(Call<OptimizedTripsResponse> call, Response<OptimizedTripsResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("DirectionsActivity", "no succes");
                    Toast.makeText(OptimizationRound.this, 't', Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (response.body().getTrips().isEmpty()) {
                        Log.d("DirectionsActivity", "impossible de detrminer l'itineraire" + " size = "
                                + response.body().getTrips().size());
                        Toast.makeText(OptimizationRound.this, "route ok",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Get most optimized route from API response
                optimizedRoute = response.body().getTrips().get(0);
                drawOptimizedRoute(optimizedRoute);
                 optimizedRoute.getLegs();
                double distance = optimizedRoute.getDistance();
                Log.i("d","r"+distance);
        }

            @Override
            public void onFailure(Call<OptimizedTripsResponse> call, Throwable throwable) {
                Log.d("DirectionsActivity", "Error: " + throwable.getMessage());
            }
        });
        return null;
    }

    private void drawOptimizedRoute(DirectionsRoute route) {
        // Remove old polyline
        if (optimizedPolyline != null) {
            map.removePolyline(optimizedPolyline);
        }
        // Draw points on MapView
        LatLng[] pointsToDraw = convertLineStringToLatLng(route);
        optimizedPolyline = map.addPolyline(new PolylineOptions()
                .add(pointsToDraw)
                .color(Color.parseColor(TEAL_COLOR))
                .width(POLYLINE_WIDTH));
    }

    private LatLng[] convertLineStringToLatLng(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), PRECISION_6);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }
        return points;
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void initializeLocationEngine() {
        locationEngine = new LostLocationEngine(OptimizationRound.this);
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            setCameraPosition(location);
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void getFacesCoordinates() {

        String cancel_req_tag = "FACE";
        String URL_API_REST_DISPLAY_FACE = "http://managemyround.livehost.fr/JSON/getFacesCoordinates.php?selectedPeriod=" +selectedPeriod;
        Log.e("Url", URL_API_REST_DISPLAY_FACE);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_API_REST_DISPLAY_FACE, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject o = new JSONObject(response);

                    latitude = o.getJSONArray("latitude");
                    longitude= o.getJSONArray("longitude");
                    arrSize=latitude.length();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("selectedPeriod", selectedPeriod);
                return params;

            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void getPostingPeriod() {

        SharedPreferences myPrefs = this.getSharedPreferences("selectedPeriod", MODE_PRIVATE);
        selectedPeriod = myPrefs.getString("selectedPeriod", null);

        String test=selectedPeriod;

    }



}



