package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.security.auth.Subject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //View object
    private Button buttonScanning;
    private TextView textViewName, textViewClass, textViewId;

    //qr code scanner object
    private IntentIntegrator qrScan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View object
        buttonScanning = (Button) findViewById(R.id.buttonscan);
        textViewName = (TextView) findViewById(R.id.textViewNama);
        textViewClass = (TextView) findViewById(R.id.textViewkelas);
        textViewId = (TextView) findViewById(R.id.textViewNim);

        //inisialisasi scan object
        qrScan = new IntentIntegrator(this);

        //implementasi onClickListener
        buttonScanning.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //jika result code scanner tidak sama sekali (awal)
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil scan tidak ada", Toast.LENGTH_LONG).show();
                //jika result code scanner tidak sama sekali (akhir)
                //Fungsi Web View (awal)
            } else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
                //Fungsi Web View (akhir)
                //fungsi telepon (awal)
            } else if(Patterns.PHONE.matcher(result.getContents()).matches()){
                String telp = String.valueOf(result.getContents());
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telp));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                try{
                    startActivity(Intent.createChooser(intent, "waiting"));
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(MainActivity.this, "no phone apk installed", Toast.LENGTH_SHORT).show();

                }
                //fungsi telepon (akhir)
                //fungsi email (awal)
            }else if (Patterns.EMAIL_ADDRESS.matcher(result.getContents()).matches()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                String isiqr = String.valueOf(result.getContents());


                String kalimat = isiqr;
                String[] bagian = kalimat.split(":");

                String variabel1 = bagian[0];
                String variabel2 = bagian[1];
                String variabel3 = bagian[2];
                String variabel4 = bagian[3];
                if (isiqr.startsWith("MATMSG")) {
                    intent.setType("plain/text");
                    String sub = String.valueOf(result.getContents());
                    String email = sub.replace("MATMSG:TO:", "");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    intent.putExtra(Intent.EXTRA_SUBJECT, variabel3);
                    intent.putExtra(Intent.EXTRA_TEXT, variabel4);

                    try {
                        startActivity(intent.createChooser(intent, "Send Email"));
                    } catch (android.content.ActivityNotFoundException ex) {

                    }
                }
                //email

            }else if (result.getContents().startsWith("MATMSG")) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                String isiqr = String.valueOf(result.getContents());

                String kalimat = isiqr;
                String[] bagian = kalimat.split(":");
                //masukan isi kode QR ke dalam list Array
                String varD = bagian[3];
                String varE = bagian[4];
                String hasil4 = varD.replace(";BODY", "");
                String hasil5 = varE.replace(";;", "");
                String email = kalimat.replace("MATMSG:TO:", "");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, hasil4);
                intent.putExtra(Intent.EXTRA_TEXT, hasil5);

                try {
                    startActivity(intent.createChooser(intent, "Send Email"));
                } catch (android.content.ActivityNotFoundException ex) {

                }
                //fungsi email (ahir)
                //fungsi map (awal)
            }else if (result.getContents().startsWith("geo:")){
                String map = String.valueOf(result.getContents());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);}
                //fungsi map (akhir)
                //Fungsi JSON (awal)
            }try {

                JSONObject obj = new JSONObject(result.getContents());

                textViewName.setText(obj.getString("nama"));
                textViewClass.setText(obj.getString("kelas"));
                textViewId.setText(obj.getString("nim"));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, result.getContents(),
                        Toast.LENGTH_LONG).show();

            }
            //fungsi json (akhir)

        }else

        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }





    @Override
    public void onClick(View v) {
        //inisialisasi qrcode scanning
        qrScan.initiateScan();
        scanQRCode();
    }
    private void scanQRCode() {
    }
}