package com.example.qrqodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //View Object
    private Button buttonScanning;
    private TextView textViewName, textViewClass, textViewId;
    //qr scanning object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //View Object
        buttonScanning = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewNama);
        textViewClass = (TextView) findViewById(R.id.textViewKelas);
        textViewId = (TextView) findViewById(R.id.textViewNIM);
        //Inisiolisasi scan Object
        qrScan = new IntentIntegrator(this);

        //implementasi onclick Listener
        buttonScanning.setOnClickListener(this);

    }

    //untuk mendapatkan hasil scanning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //Jika qrcode tidak ada sama sekali
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil Scanning tidak ada", Toast.LENGTH_LONG).show();
            } else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
            } else if (Patterns.PHONE.matcher(result.getContents()).matches()) {
                String telp = String.valueOf(result.getContents());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telp));
                startActivity(callIntent);
            }

            try {
                //konversi datanya ke json
                JSONObject obj = new JSONObject(result.getContents());
                //diset nilai datanya ke textviews
                textViewName.setText(obj.getString("nama"));
                textViewClass.setText(obj.getString("kelas"));
                textViewId.setText(obj.getString("NIM"));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, result.getContents(),
                        Toast.LENGTH_LONG).show();
            }

        } else{
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onClick(View v) {
        //inisialisasi qrcode scanning
        qrScan.initiateScan();

    }
}