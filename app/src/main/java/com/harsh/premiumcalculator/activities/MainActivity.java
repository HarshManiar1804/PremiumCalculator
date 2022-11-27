package com.harsh.premiumcalculator.activities;
import static java.lang.Integer.parseInt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.harsh.premiumcalculator.R;
import com.harsh.premiumcalculator.dbhandler.DatabaseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

@TargetApi(Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    RadioButton rbIndividual, rbHFF, radioButton;
    RadioGroup radioGroup;
    String a, m , ans="";
    CardView cardView;
    String pdfMember = "";
    Date d = new Date();


    Button  btnShare ,btnView;
    Spinner spPlan, spMember, spAmount, spRelation2, spRelation3, spRelation4, spRelation5,spRelation6,spRelation7, spAge ,spAge2, spAge3, spAge4, spAge5, spAge6, spAge7;
    LinearLayout ll2, ll3, ll4, ll5, ll6 , ll7 , llResult;
    TextView txtAmount , txtAmount2, txtAmount3 , txtAmount4 , txtAmount5 , txtAmount6, txtAmount7,txtMainMember , txtHeaderName ,txtAmountTotal , txtCal3 , txtCal4 , txtCal5;
    DatabaseHandler databaseHandler;
    EditText member2,member3,member4,member5,member6, member7;
    Boolean button = false;
    int x=0;
    int ab =0;
    int temp = 0;
    int temp4 = 0;
    int temp5 = 0, temp6 = 0, temp7 = 0;
    double TotalAmount1 ,cal2 = 0 , cal4 =0 ,cal5= 0 ,cal6= 0,cal7= 0, abcd4 = 0,  abcd5 = 0 , abcd6 = 0 , abcd7 = 0;
    int hi =0;
    double TotalAmount=0,val1=0,val2=0,val3=0,val4=0,val5=0,val6=0,val7=0;
    CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
    String data = String.valueOf(s);

    private String filePath="";
    private String fileName="";
    PdfDocument mypdfdocument = new PdfDocument();
    Paint mypaint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //pop-up dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.first_dialog);
        dialog.setCancelable(false);
        dialog.setTitle("Welcome!");

        EditText txtName;

        Button btnSave;
        initialization();

        // pop-up box events
        txtName = (EditText) dialog.findViewById(R.id.txtName);
        btnSave = (Button) dialog.findViewById(R.id.btnSave);
        dialog.show();

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(txtName.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Please write Name first", Toast.LENGTH_SHORT).show();
                    ab = 1;
                }
                else
                {
                    ab =  0;
                }
                if(ab==0)
                {
                    txtMainMember.setText(txtName.getText().toString());
                    txtHeaderName.setText((txtName.getText().toString()));
                    dialog.dismiss();
                }
            }
        });

        List<String> Types = new ArrayList<>();
        Types.add(0, "--Select Plan--");
        Types.add("Silver");
        Types.add("Gold");
        Types.add("Diamond");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPlan.setAdapter(dataAdapter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sp();
            }
        });
        // members
        List<String> mem = new ArrayList<>();
        mem.add(0, "--Select--");
        mem.add("1");
        mem.add("2");
        mem.add("3");
        mem.add("4");
        mem.add("5");
        mem.add("6");
        mem.add("7");

        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, mem);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMember.setAdapter(dataAdapter2);

        spAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spMember.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                member();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String am = spAmount.getSelectedItem().toString();
                String age = spAge.getSelectedItem().toString();
                spAge2.setSelection(0);
                spAge3.setSelection(0);
                spAge4.setSelection(0);
                spAge5.setSelection(0);
                spAge6.setSelection(0);
                spAge7.setSelection(0);
                if (rbIndividual.isChecked()) {
                    x = 1;
                }
                if(rbHFF.isChecked()) {
                    x = 2;
                }
                String amount = databaseHandler.getAmount(am, age, x);
                txtAmount.setText(amount);
                val1 = Double.parseDouble(txtAmount.getText().toString());
                onSet();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spAge2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String  am = spAmount.getSelectedItem().toString();
                String  age = spAge2.getSelectedItem().toString();
                spAge3.setSelection(0);
                spAge4.setSelection(0);
                spAge5.setSelection(0);
                spAge6.setSelection(0);
                spAge7.setSelection(0);
                if (rbIndividual.isChecked()){
                    delete();
                    x = 1;
                }
                if(rbHFF.isChecked()){
                    x = 2;
                }
                String amount = databaseHandler.getAmount(am, age, x);
                txtAmount2.setText(amount);
//                val2 = Double.parseDouble(txtAmount.getText().toString()) + Double.parseDouble(txtAmount2.getText().toString());
                val2 = Double.parseDouble(txtAmount2.getText().toString());
//                onSet(val2);
                onSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spAge3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String  am = spAmount.getSelectedItem().toString();
                String  age = spAge3.getSelectedItem().toString();
                spAge4.setSelection(0);
                spAge5.setSelection(0);
                spAge6.setSelection(0);
                spAge7.setSelection(0);
                if (rbIndividual.isChecked()){
                    delete();
                    x = 1;
                }
                if(rbHFF.isChecked()){
                    x = 2;
                    temp = 1;
                }
                String amount = databaseHandler.getAmount(am, age, x);

                txtAmount3.setText(amount);

                if(rbHFF.isChecked()){
                    if(Double.parseDouble(txtAmount.getText().toString()) > Double.parseDouble(txtAmount3.getText().toString())) {
                        val3 = Double.parseDouble(txtAmount3.getText().toString());
                        cal2 = val3 / 2;
                        cal2 =  (int) (Math.round(cal2));

                        txtAmount3.setText(String.valueOf(cal2));
                    }
                }
//                val3 = Double.parseDouble(txtAmount.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount2.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount3.getText().toString().replaceAll("S\\$|\\.$", ""));
//                onSet(val3);
                val3 = Double.parseDouble(txtAmount3.getText().toString());
                onSet();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spAge4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String  am = spAmount.getSelectedItem().toString();
                String  age = spAge4.getSelectedItem().toString();

                spAge5.setSelection(0);
                spAge6.setSelection(0);
                spAge7.setSelection(0);
                if (rbIndividual.isChecked()){
                    delete();
                    x = 1;
                }
                if(rbHFF.isChecked()){
                    x = 2;
                    temp4 =1;
                }
                String amount = databaseHandler.getAmount(am, age, x);
                txtAmount4.setText(amount);

                if(x == 2) {
                    if (Double.parseDouble(txtAmount.getText().toString()) > Double.parseDouble(txtAmount4.getText().toString())) {
                        val4 = Double.parseDouble(txtAmount4.getText().toString());
                        cal4 = (val4 * 60) / 100;
                        abcd4 = val4 - cal4;
                        abcd4 =  (int) (Math.round(abcd4));
                        txtAmount4.setText(String.valueOf(abcd4));
                    }
                }
//                val4 = Double.parseDouble(txtAmount.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount2.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount3.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount4.getText().toString().replaceAll("S\\$|\\.$", "")) ;
//                onSet(val4);
                val4 = Double.parseDouble(txtAmount4.getText().toString());
                onSet();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spAge5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String  am = spAmount.getSelectedItem().toString();
                String  age = spAge5.getSelectedItem().toString();
                spAge6.setSelection(0);
                spAge7.setSelection(0);
                if (rbIndividual.isChecked()){
                    delete();
                    x = 1;
                }
                if(rbHFF.isChecked()){
                    x = 2;
                    temp5 =1;
                }
                String amount = databaseHandler.getAmount(am, age, x);
                txtAmount5.setText(amount);
                if(x == 2) {
                    if(Double.parseDouble(txtAmount.getText().toString()) > Double.parseDouble(txtAmount5.getText().toString())) {

                        val5 = Double.parseDouble(txtAmount5.getText().toString());
                        cal5 = (val5 * 60)/100;
                        abcd5 = val5 - cal5;
                        abcd5 =  (int) (Math.round(abcd5));
                        txtAmount5.setText(String.valueOf(abcd5));
                    }
                }
//                val5 = Double.parseDouble(txtAmount.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount2.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount3.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount4.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount5.getText().toString().replaceAll("S\\$|\\.$", ""));
                val5 = Double.parseDouble(txtAmount5.getText().toString());
                onSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spAge6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String  am = spAmount.getSelectedItem().toString();
                String  age = spAge6.getSelectedItem().toString();
                spAge7.setSelection(0);
                if (rbIndividual.isChecked()){
                    delete();
                    x = 1;
                }
                if(rbHFF.isChecked()){
                    x = 2;
                    temp6 =1;
                }
                String amount = databaseHandler.getAmount(am, age, x);
                txtAmount6.setText(amount);

                if(x == 2) {
                    if(Double.parseDouble(txtAmount.getText().toString()) > Double.parseDouble(txtAmount6.getText().toString())) {

                        val6 = Double.parseDouble(txtAmount6.getText().toString());
                        cal6 = (val6 * 60)/100;
                        abcd6 = val6 - cal6;
                        abcd6 =  (int) (Math.round(abcd6));
                        txtAmount6.setText(String.valueOf(abcd6));
                    }
                }
//                val6 = Double.parseDouble(txtAmount.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount2.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount3.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount4.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount5.getText().toString().replaceAll("S\\$|\\.$", ""))+ Double.parseDouble(txtAmount6.getText().toString().replaceAll("S\\$|\\.$", ""));
                val6 = Double.parseDouble(txtAmount6.getText().toString());
                onSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spAge7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String  am = spAmount.getSelectedItem().toString();
                String  age = spAge7.getSelectedItem().toString();
                if (rbIndividual.isChecked()){
                    delete();
                    x = 1;
                }
                if(rbHFF.isChecked()){
                    x = 2;
                    temp7 =1;
                }
                String amount = databaseHandler.getAmount(am, age, x);
                txtAmount7.setText(amount);

                if(x == 2) {
                    if(Double.parseDouble(txtAmount.getText().toString()) > Double.parseDouble(txtAmount7.getText().toString())) {

                        val7 = Double.parseDouble(txtAmount7.getText().toString());
                        cal7 = (val7 * 60)/100;
                        abcd7 = val7 - cal7;
                        abcd7 =  (int) (Math.round(abcd7));
                        txtAmount7.setText(String.valueOf(abcd7));
                    }
                }
//                val7 = Double.parseDouble(txtAmount.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount2.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount3.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount4.getText().toString().replaceAll("S\\$|\\.$", "")) + Double.parseDouble(txtAmount5.getText().toString().replaceAll("S\\$|\\.$", ""))+ Double.parseDouble(txtAmount6.getText().toString().replaceAll("S\\$|\\.$", "")) +Double.parseDouble(txtAmount7.getText().toString().replaceAll("S\\$|\\.$", ""));
                val7 = Double.parseDouble(txtAmount7.getText().toString());
                onSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareClick();
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button){
                    Toast.makeText(MainActivity.this, "File is already Created", Toast.LENGTH_SHORT).show();
                }else{
                    generatePDF();
                    onViewCLick();
                }

            }
        });
    }
    private void onSet(){
        int i =0;
        Double[] sum = new Double[]{val1,val2,val3,val4,val5,val6,val7};
        Double abc=0.0;
        int a =parseInt(spMember.getSelectedItem().toString());

        for (i = 0; i <=a-1; i++) {
            abc += sum[i];
        }
        int b=0;
        b = (int) Math.round(abc);
        String str = String.valueOf(b);
        txtAmountTotal.setText("Total Amount is :- " + str);
        TotalAmount = b;
    }


    private void onViewCLick(){

        String path = filePath ;

        File fileShare = new File(path);

        Intent share = new Intent();
        share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.setAction(Intent.ACTION_VIEW);
        share.setDataAndType(Uri.fromFile(fileShare),"application/pdf");
        startActivity(share);
    }
    private  void onShareClick(){
        String path = filePath ;

        File fileShare = new File(path);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileShare));
        share.setType("application/pdf");
        startActivity(share);
    }

    public void generatePDF() {
        button = true;
        String a = "0" , b = "0";

        String pdfPlan= "";

        pdfMember =spMember.getSelectedItem().toString();

        if(rbIndividual.isChecked()){
            pdfPlan ="Individual";
        }
        if(rbHFF.isChecked()){
            pdfPlan ="HFF_" + spPlan.getSelectedItem().toString()+ "_" + spAmount.getSelectedItem().toString() + "₹";
            a = "50%";
            b = "60%";
        }
        Long tsLong = System.currentTimeMillis() / 1000;

        Date d = new Date();
        CharSequence s  = DateFormat.format("d MM, yyyy ", d.getTime());

        String timeStamp = tsLong.toString();
        fileName ="PC" + "_" + txtHeaderName.getText().toString()+ "_" + s  + ".pdf";

        File quotationFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString());

        //getting the full path of the PDF report name
        filePath = quotationFolder.getAbsolutePath() + File.separator +
                fileName; //reportName could be any name

        //constructing the PDF file
        File pdfFile = new File(filePath);

        PdfDocument.PageInfo mypageinfo = new PdfDocument.PageInfo.Builder(1000,1800,1).create();
        PdfDocument.Page mypage = mypdfdocument.startPage(mypageinfo);
        Canvas canvas = mypage.getCanvas();

        mypaint.setTextSize(80);
        canvas.drawText("The Oriental ins. Co. Lmt",30,80,mypaint);

        mypaint.setTextSize(50);
        canvas.drawText("Your Plan is :-> " + pdfPlan ,30,200,mypaint);

        mypaint.setColor(Color.BLUE);
        canvas.drawText("Mr/Mrs :->"  + txtHeaderName.getText().toString(),30,320,mypaint);

        mypaint.setColor(Color.BLUE);
        mypaint.setTextSize(40);

        mypaint.setColor(Color.BLACK);
        canvas.drawText("Members",30,440,mypaint);

        int x = parseInt(pdfMember);

        String [] mem = new String[0];
        String [] mem2= new String[0];;
        String [] mem3= new String[0];;
        String [] mem4 = new String[0];;

        if(x == 7){
            mem =  new String[] {txtHeaderName.getText().toString(), member2.getText().toString(),member3.getText().toString(),member4.getText().toString(),member5.getText().toString(),member6.getText().toString(),member7.getText().toString()};
            mem2 =  new String[] {"Owner", spRelation2.getSelectedItem().toString(),spRelation3.getSelectedItem().toString(),spRelation4.getSelectedItem().toString(),spRelation5.getSelectedItem().toString(),spRelation6.getSelectedItem().toString(),spRelation7.getSelectedItem().toString()};
            mem3 =  new String[] {"0","0",a,b,b,b,b};
            mem4 = new String[] {txtAmount.getText().toString(), txtAmount2.getText().toString(),txtAmount3.getText().toString(),txtAmount4.getText().toString(),txtAmount5.getText().toString(),txtAmount6.getText().toString(),txtAmount7 .getText().toString()};
        }
        if(x == 6){
            mem =  new String[] {txtHeaderName.getText().toString(), member2.getText().toString(),member3.getText().toString(),member4.getText().toString(),member5.getText().toString(),member6.getText().toString()};
            mem2 =  new String[] {"Owner", spRelation2.getSelectedItem().toString(),spRelation3.getSelectedItem().toString(),spRelation4.getSelectedItem().toString(),spRelation5.getSelectedItem().toString(),spRelation6.getSelectedItem().toString()};
            mem3 =  new String[] {"0","0",a,b,b,b};
            mem4 = new String[] {txtAmount.getText().toString(), txtAmount2.getText().toString(),txtAmount3.getText().toString(),txtAmount4.getText().toString(),txtAmount5.getText().toString(),txtAmount6.getText().toString()};
        }
        if(x == 5){
             mem =  new String[] {txtHeaderName.getText().toString(), member2.getText().toString(),member3.getText().toString(),member4.getText().toString(),member5.getText().toString()};
             mem2 =  new String[] {"Owner", spRelation2.getSelectedItem().toString(),spRelation3.getSelectedItem().toString(),spRelation4.getSelectedItem().toString(),spRelation5.getSelectedItem().toString()};
             mem3 =  new String[] {"0","0",a,b,b};
             mem4 = new String[] {txtAmount.getText().toString(), txtAmount2.getText().toString(),txtAmount3.getText().toString(),txtAmount4.getText().toString(),txtAmount5.getText().toString()};
        }
        if(x == 4){
            mem =  new String[] {txtHeaderName.getText().toString(), member2.getText().toString(),member3.getText().toString(),member4.getText().toString()};
            mem2 =  new String[] {"Owner", spRelation2.getSelectedItem().toString(),spRelation3.getSelectedItem().toString(),spRelation4.getSelectedItem().toString()};
            mem3 =  new String[] {"0","0",a,b};
            mem4 = new String[] {txtAmount.getText().toString(), txtAmount2.getText().toString(),txtAmount3.getText().toString(),txtAmount4.getText().toString()};
        }
        if(x == 3){
            mem =  new String[] {txtHeaderName.getText().toString(), member2.getText().toString(),member3.getText().toString()};
            mem2 =  new String[] {"Owner", spRelation2.getSelectedItem().toString(),spRelation3.getSelectedItem().toString()};
            mem3 =  new String[] {"0","0",a};
            mem4 = new String[] {txtAmount.getText().toString(), txtAmount2.getText().toString(),txtAmount3.getText().toString()};
        }
        if(x == 2){
            mem =  new String[] {txtHeaderName.getText().toString(), member2.getText().toString()};
            mem2 =  new String[] {"Owner", spRelation2.getSelectedItem().toString()};
            mem3 =  new String[] {"0","0"};
            mem4 = new String[] {txtAmount.getText().toString(), txtAmount2.getText().toString()};
        }
        if(x == 1){
            mem =  new String[] {txtHeaderName.getText().toString()};
            mem2 =  new String[] {"Owner"};
            mem3 =  new String[] {"0"};
            mem4 = new String[] {txtAmount.getText().toString()};
        }
        int sps = 440;

        for(int i = 0; i<x; i++){
            sps += 80;
            canvas.drawText(mem[i],30,sps,mypaint);
        }

        canvas.drawText("Relation",265,440,mypaint);

        int sps2 = 440;

        for(int i = 0; i<x; i++){
            sps2 += 80;
            canvas.drawText(mem2[i],265,sps2,mypaint);
        }

        canvas.drawText("Discount",500,440,mypaint);

        int sps3 = 440;

        for(int i = 0; i<x; i++){
            sps3 += 80;
            canvas.drawText(mem3[i],500,sps3,mypaint);
        }

        canvas.drawText("Premium",735,440,mypaint);

        int sps4 = 440;

        for(int i = 0; i<x; i++){
            sps4 += 80;
            canvas.drawText(mem4[i],735,sps4,mypaint);
        }

        TotalAmount1 = ((TotalAmount /100)*18);
        double GrandTotal = TotalAmount1 + TotalAmount  ;
        GrandTotal = Math.round(GrandTotal);
        TotalAmount1 = Math.round(TotalAmount1);

        mypaint.setColor(Color.BLUE);
        sps4+=80;
        mypaint.setTextSize(40);
        canvas.drawText("Total :- ",480,sps4,mypaint);
        canvas.drawText(String.valueOf(TotalAmount), 735, sps4, mypaint);

        mypaint.setColor(Color.BLACK);
        sps4+=120;
        mypaint.setTextSize(40);
        canvas.drawText("18% GST :- ",480,sps4,mypaint);

        canvas.drawText(String.valueOf(TotalAmount1), 735, sps4, mypaint);

        sps4+=80;
        mypaint.setColor(Color.BLUE);
        canvas.drawText("Grand Total :- ",480,sps4,mypaint);
        canvas.drawText(String.valueOf(GrandTotal), 735, sps4, mypaint);

        mypaint.setColor(Color.BLACK);
        canvas.drawText("Thanks,",350,sps4+220,mypaint);
        canvas.drawText("Ajay Computers",350,sps4+280,mypaint);
        canvas.drawText("ph :- 98252 32243",350,sps4+340,mypaint);
        canvas.drawText("email :- parekh.ajay87@gmail.com",350,sps4+400,mypaint);

        mypdfdocument.finishPage(mypage);
        try {
            mypdfdocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(getApplicationContext(), fileName+" has saved at Document Folder",Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mypdfdocument.close();
    }

    public void initialization() {
        cardView = findViewById(R.id.cardView);
        spPlan = findViewById(R.id.spPlan);
        spMember = findViewById(R.id.spMember);
        spAmount = findViewById(R.id.spAmount);
        spAge = findViewById(R.id.spAge);
        spAge2 = findViewById(R.id.spAge2);
        spAge3 = findViewById(R.id.spAge3);
        spAge4 = findViewById(R.id.spAge4);
        spAge5 = findViewById(R.id.spAge5);
        spAge6 = findViewById(R.id.spAge6);
        spAge7 = findViewById(R.id.spAge7);
        spRelation2 = findViewById(R.id.spRelation2);
        spRelation3 = findViewById(R.id.spRelation3);
        spRelation4 = findViewById(R.id.spRelation4);
        spRelation5 = findViewById(R.id.spRelation5);
        spRelation6 = findViewById(R.id.spRelation6);
        spRelation7 = findViewById(R.id.spRelation7);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);
        llResult = findViewById(R.id.llResult);

        btnShare = findViewById(R.id.btnShare);
        btnView = findViewById(R.id.btnView);

        txtAmount = findViewById(R.id.txtAmount);
        txtAmount2 = findViewById(R.id.txtAmount2);
        txtAmount3 = findViewById(R.id.txtAmount3);
        txtAmount4 = findViewById(R.id.txtAmount4);
        txtAmount5 = findViewById(R.id.txtAmount5);
        txtAmount6 = findViewById(R.id.txtAmount6);
        txtAmount7 = findViewById(R.id.txtAmount7);
        txtAmountTotal = findViewById(R.id.txtAmountTotal);
        txtMainMember = findViewById(R.id.txtMainMember);
        txtHeaderName = findViewById(R.id.txtHeaderName);


        member2= findViewById(R.id.member2);
        member3= findViewById(R.id.member3);
        member4= findViewById(R.id.member4);
        member5= findViewById(R.id.member5);
        member6= findViewById(R.id.member6);
        member7= findViewById(R.id.member7);

        databaseHandler = new DatabaseHandler(getApplicationContext());

        rbIndividual = findViewById(R.id.rbIndividual);
        rbHFF = findViewById(R.id.rbHFF);

        radioGroup = findViewById(R.id.rbgPlan);
    }
    public void sp() {
        if (rbIndividual.isChecked()) {
            delete();
            spMember.setSelection(0);
            spPlan.setVisibility(View.GONE);
            spPlan.setSelection(0);
            spAmount.setSelection(0);
            List<String> am = new ArrayList<>();

            am.add(0, "1,00,000");
            am.add("1,50,000");
            am.add("2,00,000");
            am.add("2,50,000");
            am.add("3,00,000");
            am.add("3,50,000");
            am.add("4,00,000");
            am.add("4,50,000");
            am.add("5,00,000");
            am.add("6,00,000");
            am.add("7,00,000");
            am.add("8,00,000");
            am.add("9,00,000");
            am.add("10,00,000");

            ArrayAdapter<String> dataAdapter1;
            dataAdapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, am);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spAmount.setAdapter(dataAdapter1);
            txtAmountTotal.setText(null);
        } else {
            spMember.setSelection(0);
            spPlan.setVisibility(View.VISIBLE);
            spAmount.setAdapter(null);
            spPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position).equals("--Select Plan--")) {
                        spAmount.setAdapter(null);
                    } else {
                        a = spPlan.getSelectedItem().toString();

                        if (a == "Silver") {

                            List<String> am = new ArrayList<>();
                            am.add(0, "1,00,000");
                            am.add("2,00,000");
                            am.add("3,00,000");
                            am.add("4,00,000");
                            am.add("5,00,000");
                            ArrayAdapter<String> dataAdapter1;
                            dataAdapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, am);
                            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spAmount.setAdapter(dataAdapter1);
                            txtAmountTotal.setText("Total Amount is :- 0");
                        }

                        if (a == "Gold") {
                            spAmount.setAdapter(null);
                            List<String> am = new ArrayList<>();
                            am.add(0, "6,00,000");
                            am.add("7,00,000");
                            am.add("8,00,000");
                            am.add("9,00,000");
                            am.add("10,00,000");
                            ArrayAdapter<String> dataAdapter1;
                            dataAdapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, am);
                            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spAmount.setAdapter(dataAdapter1);
                            txtAmountTotal.setText("Total Amount is :- 0");
                        }
                        if (a == "Diamond") {
                            spAmount.setAdapter(null);
                            List<String> am = new ArrayList<>();
                            am.add(0, "12,00,000");
                            am.add("15,00,000");
                            am.add("18,00,000");
                            am.add("20,00,000");
                            ArrayAdapter<String> dataAdapter1;
                            dataAdapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, am);
                            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spAmount.setAdapter(dataAdapter1);
                            txtAmountTotal.setText("Total Amount is :- 0");
                        }
                    }
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    public void member () {
        m = spMember.getSelectedItem().toString();
        if (m == "--Select--") {
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
            spAge.setAdapter(null);
            txtAmount.setText(null);
            txtAmountTotal.setText(null);
        }
        if (m == "1") {
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-70 yrs");
                age11.add("71-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age12;
                age12 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age12);
            }
            if (rbHFF.isChecked()) {
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add("0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-65 yrs");
                age11.add("66-70 yrs");
                age11.add("71-75 yrs");
                age11.add("76-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age;
                age = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age);

            }
        }
        //2
        if (m == "2") {
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.GONE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add("0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-70 yrs");
                age11.add("71-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age12;
                age12 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age12);
            }
            if (rbHFF.isChecked()) {

                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-65 yrs");
                age11.add("66-70 yrs");
                age11.add("71-75 yrs");
                age11.add("76-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age;
                age = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age);
            }

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }

            List<String> re = new ArrayList<>();
            re.add(0, "wife");
            re.add("husband");
            re.add("son");
            re.add("daughter");
            re.add("brother");
            re.add("sister");
            re.add("mom");
            re.add("dad");
            re.add("nephew");

            ArrayAdapter<String> Relation;
            Relation = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation2.setAdapter(Relation);
        }
        //3
        if (m == "3") {
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-70 yrs");
                age11.add("71-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age12;
                age12 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age12);
            }
            if (rbHFF.isChecked()) {

                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-65 yrs");
                age11.add("66-70 yrs");
                age11.add("71-75 yrs");
                age11.add("76-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age;
                age = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age);
            }

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }

            List<String> re = new ArrayList<>();
            re.add(0, "wife");
            re.add("husband");
            re.add("son");
            re.add("daughter");
            re.add("brother");
            re.add("sister");
            re.add("mom");
            re.add("dad");
            re.add("nephew");

            ArrayAdapter<String> Relation;
            Relation = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation2.setAdapter(Relation);

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }

            List<String> re3 = new ArrayList<>();
            re3.add(0, "wife");
            re3.add("husband");
            re3.add("son");
            re3.add("daughter");
            re3.add("brother");
            re3.add("sister");
            re3.add("mom");
            re3.add("dad");
            re3.add("nephew");

            ArrayAdapter<String> Relation3;
            Relation3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation3.setAdapter(Relation);
        }
        //4
        if (m == "4") {
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-70 yrs");
                age11.add("71-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age12;
                age12 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age12);
            }
            if (rbHFF.isChecked()) {

                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-65 yrs");
                age11.add("66-70 yrs");
                age11.add("71-75 yrs");
                age11.add("76-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age;
                age = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age);
            }

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }

            List<String> re = new ArrayList<>();
            re.add(0, "wife");
            re.add("husband");
            re.add("son");
            re.add("daughter");
            re.add("brother");
            re.add("sister");
            re.add("mom");
            re.add("dad");
            re.add("nephew");

            ArrayAdapter<String> Relation2;
            Relation2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation2.setAdapter(Relation2);

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }

            List<String> re3 = new ArrayList<>();
            re3.add(0, "wife");
            re3.add("husband");
            re3.add("son");
            re3.add("daughter");
            re3.add("brother");
            re3.add("sister");
            re3.add("mom");
            re3.add("dad");
            re3.add("nephew");

            ArrayAdapter<String> Relation3;
            Relation3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation3.setAdapter(Relation3);

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }

            List<String> re4 = new ArrayList<>();
            re4.add(0, "wife");
            re4.add("husband");
            re4.add("son");
            re4.add("daughter");
            re4.add("brother");
            re4.add("sister");
            re4.add("mom");
            re4.add("dad");
            re4.add("nephew");

            ArrayAdapter<String> Relation4;
            Relation4 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation4.setAdapter(Relation4);
        }
        //5
        if (m == "5") {
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll6.setVisibility(View.GONE);
            ll7.setVisibility(View.GONE);
            //2 in 5

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-70 yrs");
                age11.add("71-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age12;
                age12 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age12);
            }
            if (rbHFF.isChecked()) {

                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add( "0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-65 yrs");
                age11.add("66-70 yrs");
                age11.add("71-75 yrs");
                age11.add("76-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age;
                age = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age);
            }

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }

            List<String> re = new ArrayList<>();
            re.add(0, "wife");
            re.add("husband");
            re.add("son");
            re.add("daughter");
            re.add("brother");
            re.add("sister");
            re.add("mom");
            re.add("dad");
            re.add("nephew");

            ArrayAdapter<String> Relation2;
            Relation2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation2.setAdapter(Relation2);

            //3 in 5
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            List<String> re3 = new ArrayList<>();
            re3.add(0, "wife");
            re3.add("husband");
            re3.add("son");
            re3.add("daughter");
            re3.add("brother");
            re3.add("sister");
            re3.add("mom");
            re3.add("dad");
            re3.add("nephew");

            ArrayAdapter<String> Relation3;
            Relation3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation3.setAdapter(Relation3);
            //4 in 5
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }
            List<String> re4 = new ArrayList<>();
            re4.add(0, "wife");
            re4.add("husband");
            re4.add("son");
            re4.add("daughter");
            re4.add("brother");
            re4.add("sister");
            re4.add("mom");
            re4.add("dad");
            re4.add("nephew");

            ArrayAdapter<String> Relation4;
            Relation4 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation4.setAdapter(Relation4);

            //5 in 5
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge5.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge5.setAdapter(age2);
            }

            List<String> re5 = new ArrayList<>();
            re5.add(0, "wife");
            re5.add("husband");
            re5.add("son");
            re5.add("daughter");
            re5.add("brother");
            re5.add("sister");
            re5.add("mom");
            re5.add("dad");
            re5.add("nephew");

            ArrayAdapter<String> Relation5;
            Relation5 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation5.setAdapter(Relation5);


        }
        //6
        if (m == "6") {
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll6.setVisibility(View.VISIBLE);
            ll7.setVisibility(View.GONE);
            //1 and 2 in 6

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add("0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-70 yrs");
                age11.add("71-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age12;
                age12 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age12);
            }
            if (rbHFF.isChecked()) {

                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add("0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-65 yrs");
                age11.add("66-70 yrs");
                age11.add("71-75 yrs");
                age11.add("76-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age;
                age = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age);
            }
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }

            List<String> re = new ArrayList<>();
            re.add(0, "wife");
            re.add("husband");
            re.add("son");
            re.add("daughter");
            re.add("brother");
            re.add("sister");
            re.add("mom");
            re.add("dad");
            re.add("nephew");

            ArrayAdapter<String> Relation2;
            Relation2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation2.setAdapter(Relation2);

            //3 in 6
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            List<String> re3 = new ArrayList<>();
            re3.add(0, "wife");
            re3.add("husband");
            re3.add("son");
            re3.add("daughter");
            re3.add("brother");
            re3.add("sister");
            re3.add("mom");
            re3.add("dad");
            re3.add("nephew");

            ArrayAdapter<String> Relation3;
            Relation3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation3.setAdapter(Relation3);

            //4 in 6
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }
            List<String> re4 = new ArrayList<>();
            re4.add(0, "wife");
            re4.add("husband");
            re4.add("son");
            re4.add("daughter");
            re4.add("brother");
            re4.add("sister");
            re4.add("mom");
            re4.add("dad");
            re4.add("nephew");

            ArrayAdapter<String> Relation4;
            Relation4 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation4.setAdapter(Relation4);

            //5 in 6
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge5.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge5.setAdapter(age2);
            }
            List<String> re5 = new ArrayList<>();
            re5.add(0, "wife");
            re5.add("husband");
            re5.add("son");
            re5.add("daughter");
            re5.add("brother");
            re5.add("sister");
            re5.add("mom");
            re5.add("dad");
            re5.add("nephew");

            ArrayAdapter<String> Relation5;
            Relation5 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation5.setAdapter(Relation5);

            // 6 in 6
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge6.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge6.setAdapter(age2);
            }

            List<String> re6 = new ArrayList<>();
            re6.add(0, "wife");
            re6.add("husband");
            re6.add("son");
            re6.add("daughter");
            re6.add("brother");
            re6.add("sister");
            re6.add("mom");
            re6.add("dad");
            re6.add("nephew");

            ArrayAdapter<String> Relation6;
            Relation6 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation6.setAdapter(Relation6);
        }
        //7
        if (m == "7") {
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
            ll5.setVisibility(View.VISIBLE);
            ll6.setVisibility(View.VISIBLE);
            ll7.setVisibility(View.VISIBLE);
            //1 and 2 in 7

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add("0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-70 yrs");
                age11.add("71-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age12;
                age12 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age12);
            }
            if (rbHFF.isChecked()) {

                List<String> age11 = new ArrayList<>();
                age11.add(0, "--");
                age11.add("0-20 yrs");
                age11.add("21-35 yrs");
                age11.add("36-45 yrs");
                age11.add("46-55 yrs");
                age11.add("56-60 yrs");
                age11.add("61-65 yrs");
                age11.add("66-70 yrs");
                age11.add("71-75 yrs");
                age11.add("76-80 yrs");
                age11.add("80 yrs above");

                ArrayAdapter<String> age;
                age = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age11);
                age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge.setAdapter(age);
            }

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge2.setAdapter(age2);
            }

            List<String> re = new ArrayList<>();
            re.add(0, "wife");
            re.add("husband");
            re.add("son");
            re.add("daughter");
            re.add("brother");
            re.add("sister");
            re.add("mom");
            re.add("dad");
            re.add("nephew");
            ArrayAdapter<String> Relation2;
            Relation2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRelation2.setAdapter(Relation2);
            //3 in 7
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge3.setAdapter(age2);
            }
            List<String> re3 = new ArrayList<>();
            re3.add(0, "wife");
            re3.add("husband");
            re3.add("son");
            re3.add("daughter");
            re3.add("brother");
            re3.add("sister");
            re3.add("mom");
            re3.add("dad");
            re3.add("nephew");

            ArrayAdapter<String> Relation3;
            Relation3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRelation3.setAdapter(Relation3);
            //4 in 7
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge4.setAdapter(age2);
            }
            List<String> re4 = new ArrayList<>();
            re4.add(0, "wife");
            re4.add("husband");
            re4.add("son");
            re4.add("daughter");
            re4.add("brother");
            re4.add("sister");
            re4.add("mom");
            re4.add("dad");
            re4.add("nephew");

            ArrayAdapter<String> Relation4;
            Relation4 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation4.setAdapter(Relation4);
            //5 in 7
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add( "0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge5.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge5.setAdapter(age2);
            }

            List<String> re5 = new ArrayList<>();
            re5.add(0, "wife");
            re5.add("husband");
            re5.add("son");
            re5.add("daughter");
            re5.add("brother");
            re5.add("sister");
            re5.add("mom");
            re5.add("dad");
            re5.add("nephew");

            ArrayAdapter<String> Relation5;
            Relation5 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation5.setAdapter(Relation5);
            // 6 in 7
            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge6.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {

                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge6.setAdapter(age2);
            }
            List<String> re6 = new ArrayList<>();
            re6.add(0, "wife");
            re6.add("husband");
            re6.add("son");
            re6.add("daughter");
            re6.add("brother");
            re6.add("sister");
            re6.add("mom");
            re6.add("dad");
            re6.add("nephew");

            ArrayAdapter<String> Relation6;
            Relation6 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation6.setAdapter(Relation6);

            //7 in 7

            if (rbIndividual.isChecked()) {
                delete();
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-70 yrs");
                age.add("71-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge7.setAdapter(age2);
            }
            if (rbHFF.isChecked()) {
                List<String> age = new ArrayList<>();
                age.add(0, "--");
                age.add("0-20 yrs");
                age.add("21-35 yrs");
                age.add("36-45 yrs");
                age.add("46-55 yrs");
                age.add("56-60 yrs");
                age.add("61-65 yrs");
                age.add("66-70 yrs");
                age.add("71-75 yrs");
                age.add("76-80 yrs");
                age.add("80 yrs above");

                ArrayAdapter<String> age2;
                age2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, age);
                age2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spAge7.setAdapter(age2);
            }

            List<String> re7 = new ArrayList<>();
            re7.add(0, "wife");
            re7.add("husband");
            re7.add("son");
            re7.add("daughter");
            re7.add("brother");
            re7.add("sister");
            re7.add("mom");
            re7.add("dad");
            re7.add("nephew");

            ArrayAdapter<String> Relation7;
            Relation7 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, re);
            Relation7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRelation7.setAdapter(Relation7);
        }
    }
    public void delete() {}

    public void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Make sure you put all the Data   ");
        dialog.setTitle("Confirmation");
        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(),"Okay Done...! Pdf is Generating...!",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Now You Can Make Changes...!",Toast.LENGTH_LONG).show();
                hi = 1;
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}
