package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class opcoes_de_voto extends Activity {

    private Button continuar;
    private Button sair;
    private TextView titulo;
    private ArrayList<JSONObject> listChapas;
    private RadioGroup radioGroup;

    private String userCpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes_de_voto);

        continuar = (Button) findViewById(R.id.CONTINUAR_OPCOES);
        sair = (Button) findViewById(R.id.SAIR_OPCOES);
        titulo = (TextView) findViewById(R.id.opcoes_de_voto_Titulo);
        radioGroup = (RadioGroup) findViewById(R.id.chapas_radioGroup);

        continuar.setVisibility(View.INVISIBLE);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(opcoes_de_voto.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        Intent i = getIntent();

        Bundle extras = i.getExtras();

        if(extras != null){
            System.out.println("Tem os extras");
            if(extras.containsKey("user")){
                userCpf = extras.getString("user");
            }
            if (extras.containsKey("nomeVotacao")) {
                titulo.setText(extras.getString("nomeVotacao"));
            }
            if(extras.containsKey("chapas")){
                System.out.println("Tem as chapas");
                String chapas = extras.getString("chapas");

                try {
                    JSONArray array = new JSONArray(chapas);
                    listChapas = new ArrayList<JSONObject>();
                    for(int j = 0; j < array.length(); j++){
                        System.out.println(array.getJSONObject(j));
                        listChapas.add(array.getJSONObject(j));
                    }

                    createRadioButtons(listChapas.size(),radioGroup,listChapas);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                System.out.println(checkedId);
                continuar.setVisibility(View.VISIBLE);

                final JSONObject chapaEscolhida = listChapas.get(checkedId-1);

                continuar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(opcoes_de_voto.this, Confirmar_voto.class);
                        intent.putExtra("nomeVotacao",titulo.getText().toString());
                        intent.putExtra("userCpf",userCpf);
                        intent.putExtra("chapaEscolhida",chapaEscolhida.toString());
                        startActivity(intent);

                    }
                });
            }

        });


    }

    private void createRadioButtons(int size, RadioGroup radioGroup, ArrayList<JSONObject> list) throws JSONException {
        final RadioButton[] rb = new RadioButton[size];

        for(int i = 0; i < size; i++){
            rb[i] = new RadioButton(this);
            radioGroup.addView(rb[i]);
            rb[i].setText(list.get(i).getString("nome"));
            rb[i].setMinimumHeight(150);
        }

    }
}
