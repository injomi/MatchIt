
package com.example.matchit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterSelection extends Activity {
    private Button btnCoporate;
    private Button btnIndividual;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_selection);

        btnCoporate = (Button) findViewById(R.id.btnCop);
        btnIndividual = (Button) findViewById(R.id.btnIndi);

        // Register Button Click event
        btnCoporate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivityCop.class);
                startActivity(intent);
                finish();
            }
        });

        // Link to Login Screen
        btnIndividual.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}