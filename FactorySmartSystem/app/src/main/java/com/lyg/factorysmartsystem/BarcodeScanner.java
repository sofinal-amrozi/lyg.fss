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
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarcodeScanner extends AppCompatActivity {
    Spinner dataAdm, dataLine, dataStation, dataInOut;
    ImageView scanBarcode, Exit, CreateNewScan;
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

    String admData;
    String lineData;
    String stationData;
    String inoutData;
    String recordCount;
    String tSessionId;
    String tBarcodeType;

    TextView resultCount;
    Integer recCount;

    Spinner sAdmName, sLineNo, sStation, sInOut;

    int StatusCheck = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        getSupportActionBar().hide();
        StatusCheck = 0;
        tSessionId = "";
        init();
        loadData();



        //Parsing data

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

        CreateNewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StatusCheck = 0;
                sAdmName.setEnabled(true);
                sLineNo.setEnabled(true);
                sStation.setEnabled(true);
                sInOut.setEnabled(true);

                sAdmName.setSelection(0);
                sLineNo.setSelection(0);
                sStation.setSelection(0);
                sInOut.setSelection(0);

                tSessionId = "";
                tSessionId = getRandomString(10);
                //Toast.makeText(BarcodeScanner.this, "New SessionId has been created!", Toast.LENGTH_SHORT).show();


                TextView mTextView = (TextView) findViewById(R.id.text_resultbarcode);
                mTextView.setText("0 Barcode");

                loadData();

            }
        });


        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tSessionId == "") {
                    tSessionId = getRandomString(10);
                }


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

                    StatusCheck = 1;
                    Toast.makeText(BarcodeScanner.this, tSessionId, Toast.LENGTH_SHORT).show();
                    Intent input = new Intent(BarcodeScanner.this, ZxingScanner.class);
                    input.putExtra("intent_admData", admData);
                    input.putExtra("intent_lineData", lineData);
                    input.putExtra("intent_stationData", stationData);
                    input.putExtra("intent_inoutData", inoutData);
                    input.putExtra("intent_SessionId", tSessionId);
                    // input.putExtra("intent_BarcodeType", tBarcodeType);
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

        if (StatusCheck == 0) {
            sAdmName.setEnabled(true);
            sLineNo.setEnabled(true);
            sStation.setEnabled(true);
            sInOut.setEnabled(true);
        } else
        {
            sAdmName.setEnabled(false);
            sLineNo.setEnabled(false);
            sStation.setEnabled(false);
            sInOut.setEnabled(false);
        }

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
        CreateNewScan = findViewById(R.id.ivNewScan);
        Exit = findViewById(R.id.ivBackOnBarcode);
        resultCount = findViewById(R.id.text_resultbarcode);
        recCount = 0;

        // Spinner
        sAdmName = findViewById(R.id.adm_name);
        sLineNo = findViewById(R.id.line_no);
        sStation = findViewById(R.id.station);
        sInOut = findViewById(R.id.in_out);

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
                Toast.makeText(BarcodeScanner.this, "No Data Adm !", Toast.LENGTH_LONG).show();
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
                Toast.makeText(BarcodeScanner.this, "No Data Line !", Toast.LENGTH_LONG).show();
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
                Toast.makeText(BarcodeScanner.this, "No Data Station !", Toast.LENGTH_LONG).show();
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
                Toast.makeText(BarcodeScanner.this, "No Data In/Out !", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest4);
    }

    public static String getRandomString(int i) {
        final String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        while (i > 0) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i--;
        }
        return result.toString();


    }



    // Load data hasil scan
    public void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerApi.URL_DATASCAN + tSessionId, new Response.Listener<String>() {
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
                        playerModel.setBarcodeType(send_data_to_api.getString("BarcodeType"));
                        playerModel.setBarcodeTypeCode(send_data_to_api.getString("BarcodeTypeCode"));
                        item.add(playerModel);

                    }

//                    for (int i = 0; i < data.length(); i++)
//                    {
//                        tBarcodeType = "";
//                        tBarcodeType = data.getJSONObject(i).getString("BarcodeType");
//                        Toast.makeText(BarcodeScanner.this, tBarcodeType, Toast.LENGTH_SHORT).show();
//                    }

                    //Toast.makeText(BarcodeScanner.this, String.valueOf(item.size()), Toast.LENGTH_SHORT).show();
                    TextView mTextView = (TextView) findViewById(R.id.text_resultbarcode);
                    recordCount = String.valueOf(item.size()) + " Barcode";
                    mTextView.setText(recordCount);
                    adapter = new AdapterDataBarcode(BarcodeScanner.this, item);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BarcodeScanner.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e){
                    TextView mTextView = (TextView) findViewById(R.id.text_resultbarcode);
                    mTextView.setText("0 Barcode");
                    Toast.makeText(BarcodeScanner.this, "New SessionId has been created!", Toast.LENGTH_SHORT).show();

                    item = new ArrayList<>();
                    adapter = new AdapterDataBarcode(BarcodeScanner.this, item);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BarcodeScanner.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setAdapter(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView mTextView = (TextView) findViewById(R.id.text_resultbarcode);
                mTextView.setText("0 Barcode");
                Toast.makeText(BarcodeScanner.this, "No Data !", Toast.LENGTH_LONG).show();

                item = new ArrayList<>();
                adapter = new AdapterDataBarcode(BarcodeScanner.this, item);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BarcodeScanner.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                recyclerView.setAdapter(null);

            }
        });
        requestQueue.add(stringRequest);
        //TextView mTextView = (TextView) findViewById(R.id.text_resultbarcode);
        //mTextView.setText(getString(adapter.getItemCount()));
    }
}