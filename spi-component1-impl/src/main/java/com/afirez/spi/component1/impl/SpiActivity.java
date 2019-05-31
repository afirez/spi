package com.afirez.spi.component1.impl;

import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.afirez.spi.SPI;

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "/spi/activity/spi")
public class SpiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spi);
        findViewById(R.id.tvSpiActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SpiActivity.this.getApplicationContext(), "SpiActivity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
