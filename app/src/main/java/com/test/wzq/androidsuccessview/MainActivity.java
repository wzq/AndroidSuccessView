package com.test.wzq.androidsuccessview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wzq.successview.SuccessView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuccessView sv = (SuccessView) findViewById(R.id.view);
                sv.startAnim(200);
                SuccessView sv1 = (SuccessView) findViewById(R.id.view1);
                sv1.startAnim(400);
                SuccessView sv2 = (SuccessView) findViewById(R.id.view2);
                sv2.startAnim(400);
            }
        });
    }
}
