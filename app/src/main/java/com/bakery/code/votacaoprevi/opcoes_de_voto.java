package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class opcoes_de_voto extends Activity {

    private Button continuar;
    private Button sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes_de_voto);

        continuar = (Button) findViewById(R.id.CONTINUAR_OPCOES);
        sair = (Button) findViewById(R.id.SAIR_OPCOES);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(opcoes_de_voto.this, Confirmar_voto.class);
                startActivity(intent);

            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(opcoes_de_voto.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}
