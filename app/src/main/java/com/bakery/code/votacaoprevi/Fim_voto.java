package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Fim_voto extends Activity {

    private Button sair;
    private TextView titulo;
    private String tituloVotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_voto);

        titulo = (TextView) findViewById(R.id.opcoes_de_voto_Titulo);

        Intent i = getIntent();

        Bundle extras = i.getExtras();

        if(extras != null) {
            System.out.println("tem os extras");
            if(extras.containsKey("titulo")){
                System.out.println(extras.get("titulo"));
            }
            tituloVotacao = extras.getString("titulo");

            titulo.setText(tituloVotacao);
        }
        sair = (Button) findViewById(R.id.SAIR_FIM);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
}
