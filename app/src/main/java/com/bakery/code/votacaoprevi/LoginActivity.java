package com.bakery.code.votacaoprevi;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button continuar;
    private Button sair;
    private TextView login;
    private TextView senha;
    private TextView erro_mens;
    private ImageView erro_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        continuar = (Button) findViewById(R.id.CONTINUAR_LOGIN);
        sair = (Button) findViewById(R.id.SAIR_LOGIN);
        login = (TextView) findViewById(R.id.LOGIN);
        erro_mens = (TextView) findViewById(R.id.User_invalido);
        erro_img = (ImageView) findViewById(R.id.INVALID_IMG);
        senha = (TextView) findViewById(R.id.SENHA);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Login = login.getText().toString();
                if (TextUtils.isEmpty(login.getText().toString())){
                    erro_mens.setText("Login não preenchido");
                    erro_img.setImageResource(android.R.drawable.ic_delete);
                }else if(TextUtils.isEmpty(senha.getText().toString())){
                    erro_mens.setText("Senha não preenchida");
                    erro_img.setImageResource(android.R.drawable.ic_delete);
                }else{
                    if (ValidaCPF.isCPF(Login) == true){
                        Intent intent = new Intent(LoginActivity.this, opcoes_de_voto.class);
                        startActivity(intent);
                    }else if (ValidaCPF.isCPF(Login) == false){
                        Log.i("login","");
                        erro_mens.setText("CPF inválido!");
                        erro_img.setImageResource(android.R.drawable.ic_delete);
                    }
                }
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
}
