package com.example.mybestlocation.ui.home;

import static android.text.TextUtils.replace;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    // FusedLocationProviderClient mClient= LocationServices.getFusedLocationProviderClient(this.getApplicationContext() );

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        View root = binding.getRoot();


        binding.mapBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String latitude = binding.lon.getText().toString();
                String longitude = binding.lat.getText().toString();
                String description = binding.desc.getText().toString();
                System.out.println(longitude+latitude);
                String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Label which you want" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

binding.newBtn.setOnClickListener(view -> {
    Ajout a = new Ajout(getActivity());
    HashMap<String, String> map = new HashMap<>();
    map.put("longitude" ,  binding.lon.getText().toString());
    map.put("latitude" , binding.lat.getText().toString());
    map.put("description" ,  binding.desc.getText().toString());
    a.setMap(map);
    a.execute();
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
        HashMap<String, String> map  ;
        JSONParser parser ;

        public void setMap(HashMap map) {
            this.map = map;
        }

        Ajout(Context con){
            this.con = con ;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            parser = new JSONParser();
            AlertDialog.Builder dialog = new AlertDialog.Builder(con);
            dialog.setTitle(" alert");
            dialog.setMessage("waiting for data");
            alert=dialog.create();
            alert.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            System.out.println(map);
            String ip = "192.168.163.121";

            String url ="http://"+ip+"/tp_mobile/ajout_position.php";
            JSONObject response = parser.makeHttpRequest(url , "POST",map );
            try {
                int success = response.getInt("success");
                if(success ==0){
                    System.out.println(success);
                }else {
                    System.out.println(success);
                }
            }catch (Exception e){

                System.out.println(e.getMessage());

            }


            try {

                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            alert.dismiss();
            AlertDialog.Builder dialog = new AlertDialog.Builder(con);
            dialog.setTitle(" alert");
            dialog.setMessage("done");
            alert=dialog.create();
            alert.show();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            alert.dismiss();
        }
    }


}