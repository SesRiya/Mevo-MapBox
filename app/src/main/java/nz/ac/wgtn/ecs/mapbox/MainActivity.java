package nz.ac.wgtn.ecs.mapbox;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;


import nz.ac.wgtn.ecs.mapbox.models.Features;
import nz.ac.wgtn.ecs.mapbox.models.MevoMap;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private MapView mapView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.mevo.co.nz/public/vehicles/wellington";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            List<Feature> symbolLayerIconFeatureList = new ArrayList<>();

                            MevoMap mevoMap = new Gson().fromJson(response, MevoMap.class);
                            List<Features> features = mevoMap.getData().getFeatures();
                            for (Features feature : features) {
                                List<String> coordinates = feature.getGeometry().getCoordinates();
                                Double lng = Double.valueOf(coordinates.get(0));
                                Double lat = Double.valueOf(coordinates.get(1));

                                Feature singleFeature = Feature.fromGeometry(Point.fromLngLat(lng, lat));
                                symbolLayerIconFeatureList.add(singleFeature);

                                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                                        // Add the SymbolLayer icon image to the map style
                                        .withImage(ICON_ID, BitmapFactory.decodeResource(
                                                MainActivity.this.getResources(), R.drawable.pin_mvo))

                                        // Adding a GeoJson source for the SymbolLayer icons.
                                        .withSource(new GeoJsonSource(SOURCE_ID,
                                                FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

                                        // Adding the actual SymbolLayer to the map style.
                                        .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                                .withProperties(
                                                        iconImage(ICON_ID),
                                                        iconAllowOverlap(true),
                                                        iconIgnorePlacement(true)
                                                )
                                        ), new Style.OnStyleLoaded() {

                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {
                                        // Map is set up and the style has loaded.
                                    }
                                });
                                }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
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
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}