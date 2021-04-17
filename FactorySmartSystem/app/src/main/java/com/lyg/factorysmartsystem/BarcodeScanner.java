package com.lyg.factorysmartsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lyg.factorysmartsystem.Adapter.AdapterDataBarcode;
import com.lyg.factorysmartsystem.ConfigFile.ServerApi;
import com.lyg.factorysmartsystem.ConfigFile.authdata;
import com.lyg.factorysmartsystem.Model.ModelDataAdm;
import com.lyg.factorysmartsystem.Model.ModelDataBarcode;
import com.lyg.factorysmartsystem.Model.ModelInOut;
import com.lyg.factorysmartsystem.Model.ModelLine;
import com.lyg.factorysmartsystem.Model.ModelStation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BarcodeScanner extends AppCompatActivity {
    Spinner dataAdm, dataLine, dataStation, dataInOut;
    ImageView scanBarcode, Exit;
    RequestQueue requestQueue;

    authdata authdataa;

    List<ModelDataAdm> DataAdmList;
    List<ModelLine> LineList;
    List<ModelStation> StationList;
    List<ModelInOut> InOutList;

    ArrayAdapter<ModelDataAdm> DataAdmAdapter;
    ArrayAdapter<ModelLine> LineAdapter;
    ArrayAdapter<ModelStation> StationAdapter;
    ArrayAdapter<ModelInOut> InOutAdapter;

    RecyclerView recyclerView;
    AdapterDataBarcode adapter;
    List<ModelDataBarcode> item;

    String admData, lineData, stationData, inoutData;

    int StatusCheck = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        getSupportActionBar().hide();
        init();
        loadData();



        //        Parsing data

        /*Intent intent = getIntent();
        admDataEx = intent.getStringExtra("Extent_admData");
        lineDataEx = intent.getStringExtra("Extent_lineData");
        stationDataEx = intent.getStringExtra("Extent_stationData");
        inoutDataEx = intent.getStringExtra("Extent_inoutData");*/



        DataAdmList = new ArrayList<>();
        DataAdmList.add(new ModelDataAdm("0", ""));

        /*if (admDataEx.toString().trim().equals("")) {
            DataAdmList.add(new ModelDataAdm("0", ""));
        } else {
            DataAdmList.add(new ModelDataAdm("0", admDataEx));
        }*/

        loadAdm();
        dataAdm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ModelDataAdm dataSelected = (ModelDataAdm) adapterView.getSelectedItem();
//                Toast.makeText(BarcodeScanner.this, dataSelected.getSequenceNo() + " " + dataSelected.getAdm_Name(), Toast.LENGTH_SHORT).show();
                admData = dataSelected.getAdm_Name();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LineList = new ArrayList<>();

        LineList.add(new ModelLine("0", ""));
        loadLine();
        dataLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ModelLine dataSelected = (ModelLine) adapterView.getSelectedItem();
                lineData = dataSelected.getLine_No();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        StationList = new ArrayList<>();
        StationList.add(new ModelStation("0", ""));
        loadStation();
        dataStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ModelStation dataSelected = (ModelStation) adapterView.getSelectedItem();
                stationData = dataSelected.getStation_Name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        InOutList = new ArrayList<>();
        InOutList.add(new ModelInOut("0", ""));
        loadInOut();
        dataInOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ModelInOut dataSelected = (ModelInOut) adapterView.getSelectedItem();
                inoutData = dataSelected.getIO_Name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataAdm.getSelectedItem().toString().trim().equals("")) {
                    Toast.makeText(BarcodeScanner.this, "Select Admin !", Toast.LENGTH_SHORT).show();
                }
                else if (dataLine.getSelectedItem().toString().trim().equals("")){
                    Toast.makeText(BarcodeScanner.this, "Select Line !", Toast.LENGTH_SHORT).show();
                }
                else if (dataStation.getSelectedItem().toString().trim().equals("")) {
                    Toast.makeText(BarcodeScanner.this, "Select Station !", Toast.LENGTH_SHORT).show();
                }
                else if (dataInOut.getSelectedItem().toString().trim().equals("")) {
                    Toast.makeText(BarcodeScanner.this, "Select In / Out !", Toast.LENGTH_SHORT).show();
                } else {


                    Intent input = new Intent(BarcodeScanner.this, ZxingScanner.class);
                    input.putExtra("intent_admData", admData);
                    input.putExtra("intent_lineData", lineData);
                    input.putExtra("intent_stationData", stationData);
                    input.putExtra("intent_inoutData", inoutData);
                    startActivity(input);

                    //finish();

                }
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this);
                builder.setCancelable(false);
                //set pesan yang akan ditampilkan
                builder.setMessage("Exit from Barcode Scanner ?");
                //set positive tombol jika menjawab ya
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //jika kalian menekan tombol ya, maka otomatis akan keluar dari activity saat ini
                        Intent intentmenu = new Intent(BarcodeScanner.this, MainMenu.class);
                        startActivity(intentmenu);
                        finish();

                    }
                });
                //set negative tombol jika menjawab tidak
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //jika menekan tombol tidak, maka kalian akan tetap berada di activity saat ini
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }


        });
    }

    public void onDestroy() {

        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        loadData();
    }

    public void init(){
        requestQueue = Volley.newRequestQueue(this);
        authdataa = new authdata(this);
        recyclerView = findViewById(R.id.recyclerDataInput);
        dataAdm = findViewById(R.id.adm_name);
        dataLine = findViewById(R.id.line_no);
        dataStation = findViewById(R.id.station);
        dataInOut = findViewById(R.id.in_out);
        scanBarcode = findViewById(R.id.ivScanBarcode);
        Exit = findViewById(R.id.ivBackOnBarcode);
    }

    public void loadAdm(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerApi.URL_DATAADM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject itemAdm = data.getJSONObject(i);

                        DataAdmList.add(new ModelDataAdm(itemAdm.getString("SequenceNo"), itemAdm.getString("Adm_Name")));
                    }
//                    String compareValue = authdataa.getNamaadm().toString();
                    DataAdmAdapter = new ArrayAdapter(BarcodeScanner.this, R.layout.simple_spinner_item, DataAdmList);
                    DataAdmAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    dataAdm.setAdapter(DataAdmAdapter);
//                    if (compareValue != null) {
//                        int spinnerPosition = DataAdmAdapter.getPosition(compareValue);
//                        dataAdm.setSelection(spinnerPosition);
//                    }
                } catch (JSONException e) {
                    Toast.makeText(BarcodeScanner.this, "Codingan Error: Load Adm !", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BarcodeScanner.this, "No Data !", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void loadLine(){
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, ServerApi.URL_DATALINE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject itemLine = data.getJSONObject(i);

                        LineList.add(new ModelLine(itemLine.getString("SequenceNo"), itemLine.getString("Line_No")));
                    }
                    LineAdapter = new ArrayAdapter(BarcodeScanner.this, R.layout.simple_spinner_item, LineList);
                    LineAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    dataLine.setAdapter(LineAdapter);
                } catch (JSONException e) {
                    Toast.makeText(BarcodeScanner.this, "Codingan Error: Load Line !", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BarcodeScanner.this, "No Data !", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest2);
    }

    public void loadStation(){
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, ServerApi.URL_DATASTATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject stationLine = data.getJSONObject(i);

                        StationList.add(new ModelStation(stationLine.getString("SequenceNo"), stationLine.getString("Station_Name")));
                    }
                    StationAdapter = new ArrayAdapter(BarcodeScanner.this, R.layout.simple_spinner_item, StationList);
                    StationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    dataStation.setAdapter(StationAdapter);
                } catch (JSONException e) {
                    Toast.makeText(BarcodeScanner.this, "Codingan Error: Load Station !", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BarcodeScanner.this, "No Data !", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest3);
    }

    public void loadInOut(){
        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, ServerApi.URL_DATAINOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject inoutLine = data.getJSONObject(i);

                        InOutList.add(new ModelInOut(inoutLine.getString("SequenceNo"), inoutLine.getString("IO_Name")));
                    }
                    InOutAdapter = new ArrayAdapter(BarcodeScanner.this, R.layout.simple_spinner_item, InOutList);
                    InOutAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    dataInOut.setAdapter(InOutAdapter);
                } catch (JSONException e) {
                    Toast.makeText(BarcodeScanner.this, "Codingan Error: Load In/Out !", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BarcodeScanner.this, "No Data !", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest4);
    }

    // Load data hasil scan
    public void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerApi.URL_DATASCAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    item = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++)
                    {
                        ModelDataBarcode playerModel = new ModelDataBarcode();
                        JSONObject send_data_to_api = data.getJSONObject(i);
                        playerModel.setBarcodeNo(send_data_to_api.getString("BarcodeNo"));
                        playerModel.setScanDate(send_data_to_api.getString("ScanDate"));

                        item.add(playerModel);
                    }
                    adapter = new AdapterDataBarcode(BarcodeScanner.this, item);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BarcodeScanner.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e){
                    Toast.makeText(BarcodeScanner.this, "Codingan Error: Load Data !", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BarcodeScanner.this, "No Data !", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}