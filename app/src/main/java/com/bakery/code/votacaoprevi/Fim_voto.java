package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Fim_voto extends Activity {

    private Button sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_voto);

        sair = (Button) findViewById(R.id.SAIR_FIM);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fim_voto.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
