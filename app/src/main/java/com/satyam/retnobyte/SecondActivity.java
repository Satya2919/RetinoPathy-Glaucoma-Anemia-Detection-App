package com.satyam.retnobyte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;

public class SecondActivity extends AppCompatActivity {
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mainGrid =findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
    }
    private void setSingleEvent(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        Intent intent = new Intent(SecondActivity.this, RetinoActivity.class);
                        startActivity(intent);
                    } else if (finalI == 1) {
                        Intent intent = new Intent(SecondActivity.this, GlaucoActivity.class);
                        startActivity(intent);
                    } else if (finalI == 2) {
                        Intent intent = new Intent(SecondActivity.this, MacularActivity.class);
                        startActivity(intent);
                    } else if (finalI == 3) {
                        Intent intent = new Intent(SecondActivity.this, RetinitisActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
