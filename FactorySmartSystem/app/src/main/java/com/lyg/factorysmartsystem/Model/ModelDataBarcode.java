package com.lyg.factorysmartsystem.Model;

import android.widget.ImageView;

import com.lyg.factorysmartsystem.R;

public class ModelDataBarcode {
    private String BarcodeNo;
    private String ScanDate;
    private String BarcodeType;
    private String BarcodeTypeCode;
    //private ImageView ivBarcodeImage;

    public String getBarcodeNo() {
        return BarcodeNo;
    }
    public String getScanDate() {
        return ScanDate;
    }
    public String getBarcodeType() { return BarcodeType; }
    public String getBarcodeTypeCode() { return BarcodeTypeCode; }
//    public ImageView getIvBarcodeImage() {
//        return ivBarcodeImage;
//    }



    public void setBarcodeNo(String BarcodeNo) {
        this.BarcodeNo = BarcodeNo;
    }
    public void setScanDate(String ScanDate) {
        this.ScanDate = ScanDate;
    }
    public void setBarcodeType(String BarcodeType) {
        this.BarcodeType = BarcodeType;
    }
    public void setBarcodeTypeCode(String BarcodeTypeCode) { this.BarcodeTypeCode = BarcodeTypeCode; }

//    public void setIvBarcodeImage(ImageView BarcodeImager) {
//        this.ivBarcodeImage = BarcodeImager;
//    }

}
