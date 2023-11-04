package com.example.mybestlocation.ui.home;

import static android.text.TextUtils.replace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.mybestlocation.JSONParser;
import com.example.mybestlocation.Position;
import com.example.mybestlocation.R;
import com.example.mybestlocation.databinding.FragmentHomeBinding;
import com.example.mybestlocation.ui.gallery.GalleryFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Button new_btn, map_btn;
    private TextView lon, lat , desc ;

    private FusedLocationProviderClient fusedLocationClient;

    // FusedLocationProviderClient mClient= LocationServices.getFusedLocationProviderClient(this.getApplicationContext() );

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        new_btn = root.findViewById(R.id.new_btn);
        map_btn = root.findViewById(R.id.map_btn);
        lon = root.findViewById(R.id.lon);
        lat = root.findViewById(R.id.lat);
        desc = root.findViewById(R.id.desc);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        String  latitude = Double. toString(
                                        location.getLatitude());
                                        String  longitude = Double. toString(location.getLongitude());

                                        lon.setText(longitude);
                                        lat.setText(latitude);
                                    } else {
                                        // La localisation est null, cela peut arriver dans certaines situations rares
                                        lon.setText("Dernière localisation indisponible");
                                        lat.setText("Dernière localisation indisponible");
                                    }
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

                    lon.setText("Dernière localisation ");
                    lat.setText("Dernière localisation ");
                }

            }
        });

new_btn.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View view) {
        Ajout a = new Ajout(getActivity());
        a.execute();
    }
});




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    class Ajout extends AsyncTask {

        Context con;
        AlertDialog alert ;
        Ajout(Context con){
            this.con = con ;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlertDialog.Builder dialog = new AlertDialog.Builder(con);
            dialog.setTitle(" alert");
            dialog.setMessage("waiting for data");
            alert=dialog.create();
            alert.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String ip = "192.168.1.19";
            String url ="http://"+ip+"/tp_mobile/ajout_position.php?longitude="+lon.getText()+"&latitude="+lat.getText()+"&description="+desc.getText()+"";
            JSONParser parser = new JSONParser();
            JSONObject response = parser.makeRequest(url);
            try {
                int success = response.getInt("success");
                if(success ==0){
                    String msg = response.getString("message");
                }else {
                }
            }catch (Exception e){

                System.out.println(e.getMessage());

            }


            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            
            alert.dismiss();
        }
    }


}