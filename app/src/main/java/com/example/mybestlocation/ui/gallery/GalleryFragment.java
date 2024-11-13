package com.example.mybestlocation.ui.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mybestlocation.JSONParser;
import com.example.mybestlocation.Position;
import com.example.mybestlocation.R;
import com.example.mybestlocation.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    ArrayList<Position> data = new ArrayList<Position>();
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Download d  = new Download(getActivity());
                d.execute();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class Download extends AsyncTask {

        Context con;
        AlertDialog alert ;
        Download(Context con){
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
            String ip = "192.168.163.121";
            String url ="http://"+ip+"/tp_mobile/get_all_user.php";
            JSONParser parser = new JSONParser();
            JSONObject response = parser.makeRequest(url);
            try {
                int success = response.getInt("success");
                if(success ==0){
                    String msg = response.getString("message");
                }else {
                    JSONArray jr = response.getJSONArray("Positions");
                    data.clear();
                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject line = jr.getJSONObject(i);
                        int id = line.getInt("id");
                        String longitude = line.getString("longitude");
                        String latitude = line.getString("latitude");
                        String description = line.getString("description");
                        Position p = new Position(id,longitude,latitude,description);
                        data.add(p);
                    }

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
            ArrayAdapter<Position> ad = new ArrayAdapter<Position>(con, android.R.layout.simple_list_item_1,data);
            binding.lvGallery.setAdapter(ad);

            alert.dismiss();
        }
    }


}