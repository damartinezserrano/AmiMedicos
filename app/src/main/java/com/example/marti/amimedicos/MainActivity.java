package com.example.marti.amimedicos;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.marti.amimedicos.estructura.Ubicacion;
import com.example.marti.amimedicos.estructura.ValidarTriageBody;
import com.example.marti.amimedicos.interfaces.notification.NotificationM;
import com.example.marti.amimedicos.settings.Constant;
import com.example.marti.amimedicos.ui.LogInUI;
import com.example.marti.amimedicos.ui.ServiciosAsignadosUI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NotificationM {


    LocationManager lm;
    LocationListener ll;

    double longitude = 0, latitude = 0;

    GsonBuilder gsonBuilder;
    Gson gson;

    RequestQueue requestQueue;
    Ubicacion ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainAppThemeWithNoBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1);
        addDynamicFragment();

        if (getIntent().getIntExtra("notif", 0) == 1) {
            Fragment fg = ServiciosAsignadosUI.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).commit();

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location","Changed :"+location.getLatitude());
                if (Constant.servicioIniciado) {
                    Log.i("cod_serv",Constant.cod_detalle_serv);
                    ubicarMedico(location, Constant.HTTP_DOMAIN_DVD + Constant.END_POINT_POSITION + Constant.SLASH + Constant.cod_detalle_serv);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestSingleUpdate(lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER, ll, null);
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000, 0, ll);
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10000, 0, ll);
    }

    private void addDynamicFragment() {
        // TODO Auto-generated method stub
        // creating instance of the HelloWorldFragment.
        Fragment fg = LogInUI.newInstance();
        //Fragment fg = LogInFragment.newInstance();
        // adding fragment to relative layout by using layout id
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).commit();
    }

    @Override
    public void localNotification() {
        createNotificationChannel();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("notif", 1);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "id0");
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icono_corazon_verde)
                .setTicker("Nuevo Servicio")
                .setContentTitle("Nuevo Servicio Asignado")
                .setContentText("Tienes un nuevo servicio asignado.")
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setPriority(NotificationCompat.PRIORITY_HIGH | NotificationCompat.PRIORITY_MAX);


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());

    }

    @Override
    public void popupInternalNotification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.notification_layout, null);
        builder.setView(dialogView);
        builder.setPositiveButton("Ver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final AlertDialog alert = builder.create();
        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alert.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Servicio";
            String description = "Servicio Asignado";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("id0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void ubicarMedico(Location location, String url) {

        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (latitude != 0 && longitude != 0) { //verifica si el dispositivo esta registrado y procede a obtener su ubicacion

                Log.i("Localizacion", "localizado");

                String lat = String.valueOf(latitude);
                String lng = String.valueOf(longitude);

                putUbicacionMedico(url, lat, lng); //Constant.HTTP_DOMAIN_DVD+Constant.END_POINT_POSITION+Constant.SLASH+Constant.cod_detalle_serv

            }

        }
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }

        return requestQueue;
    }

    public void putUbicacionMedico(String URL, String ltn, String lgn) {


        requestQueue = getRequestQueue();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL, putUbicacionBodyJSON(ltn, lgn), //hacemos la peticion post
                response -> {

                    Log.i("MainAct", "Se ha ubicado el medico con exito");


                }, error -> {
            Log.i("MainAct", "Error al enviar ubicacion");
            // parseLogInError(error);
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError { //autorizamos basic
                Map<String, String> headers = new HashMap<>();
                String auth = getResources().getString(R.string.auth);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }

        };

        requestQueue.add(request);
    }

    public JSONObject putUbicacionBodyJSON(String ltn, String lgn) { //construimos el json
        //primero json device
        String loginBody = "";
        JSONObject jsonObject = null;

        ubicacion = new Ubicacion();
        ubicacion.setLat(ltn); //
        ubicacion.setLng(lgn); //


        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        loginBody = gson.toJson(ubicacion);
        Log.i("ubicRbody", loginBody);

        try {
            jsonObject = new JSONObject(loginBody);
            Log.i("jsonObject", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void pedirUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestSingleUpdate(lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER, ll, null);
    }
}
