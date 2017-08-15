package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Confirmar_voto extends Activity {

    private Button sim;
    private Button nao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_voto);

        sim = (Button) findViewById(R.id.SIM_CONFIRMAR);
        nao = (Button) findViewById(R.id.NAO_CONFIRMAR);

        sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Confirmar_voto.this, Fim_voto.class);
                startActivity(intent);
            }
        });

        nao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Confirmar_voto.this, opcoes_de_voto.class);
                startActivity(intent);

            }
        });
    }
}
