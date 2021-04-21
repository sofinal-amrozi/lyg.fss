package com.lyg.factorysmartsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lyg.factorysmartsystem.Model.ModelDataBarcode;
import com.lyg.factorysmartsystem.R;


import java.util.List;

public class AdapterDataBarcode extends RecyclerView.Adapter<AdapterDataBarcode.MyViewHolder>{
    private List<ModelDataBarcode> item;
    private Context context;
    private String barcodeType;

    int [] bcodeimg = new int [] {R.drawable.barcode_result, R.drawable.qrcode_result};
//    int qr_int = 1;
//    int std_int = 2;

    public AdapterDataBarcode(Context context, List<ModelDataBarcode> item){
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_datainput, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(layout);
        return myViewHolder;
    }

    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        ModelDataBarcode me = item.get(position);
        holder.BarcodeNo.setText(me.getBarcodeNo());
        holder.ScanDate.setText(me.getScanDate());
        holder.BarTypeCode.setText(me.getBarcodeTypeCode());
        holder.barcodeImager.setImageResource(bcodeimg[Integer.valueOf(me.getBarcodeTypeCode())]);

//        barcodeType = me.getBarcodeType();
//
//        if (barcodeType == "QR_CODE") {
//            holder.barcodeImager.setImageResource(bcodeimg[qr_int]);
//
//            //Toast.makeText(context, "QR_CODE", Toast.LENGTH_SHORT).show();
//
//        } else {
//            holder.barcodeImager.setImageResource(bcodeimg[std_int]);
//            //Toast.makeText(context, "BARCODE", Toast.LENGTH_SHORT).show();
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, me.getBarcodeNo(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView BarcodeNo, ScanDate, BarTypeCode;
        public final ImageView barcodeImager;


        public MyViewHolder(View itemView){
            super(itemView);
            BarcodeNo = itemView.findViewById(R.id.text_barcode);
            ScanDate = itemView.findViewById(R.id.text_tglbarcode);
            barcodeImager= itemView.findViewById(R.id.imageBarcode);
            BarTypeCode = itemView.findViewById(R.id.text_barcodetype);
        }
    }
}
