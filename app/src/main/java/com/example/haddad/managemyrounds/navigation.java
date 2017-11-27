package com.example.haddad.managemyrounds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;

import com.example.haddad.managemyrounds.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationEventListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.commons.models.Position;

public class navigation extends AppCompatActivity {
   private MapboxNavigation navigation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Position origin = Position.fromLngLat(-77.03613, 38.90992);
        Position destination = Position.fromLngLat(-77.0365, 38.8977);
        Position destination2 = Position.fromLngLat(-77.0368, 38.8978);

        Mapbox.getInstance(this, getString(R.string.access_token));
         navigation = new MapboxNavigation(this, getString(R.string.access_token));

        LocationEngine locationEngine = LostLocationEngine.getLocationEngine(this);
        navigation.setLocationEngine(locationEngine);

        NavigationRoute.Builder builder = NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .destination(destination2);
        navigation.addNavigationEventListener(new NavigationEventListener() {
            @Override
            public void onRunning(boolean running) {

            }
        });

        builder.build();
    }


    protected void onDestroy() {
        super.onDestroy();
        // End the navigation session
        navigation.endNavigation();
        navigation.onDestroy();
    }


}
