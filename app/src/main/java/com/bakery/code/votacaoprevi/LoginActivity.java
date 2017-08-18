package com.bakery.code.votacaoprevi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakery.code.votacaoprevi.models.User;
import com.bakery.code.votacaoprevi.models.Votacao;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.worklight.wlclient.api.WLClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private Button continuar;
    private Button sair;
    private TextView login;
    private TextView senha;
    private TextView erro_mens;
    private ImageView erro_img;
    private ImageView captcha;
    private Button Call;
    private TextView titulo;
    private static final String RECAPTCHA_TOKEN = "6LcO8iwUAAAAAPQqSDrqcgoQrg0wUYI2de_gLl_-";

    private WLClient client;

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
        titulo = (TextView) findViewById(R.id.Titulo);

        //acessar extras vindo da splash screen
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        if(extras != null){
            System.out.println("Tem os extras");
            if (extras.containsKey("votacao")) {
                Votacao votacao = extras.getParcelable("votacao");
                titulo.setText(votacao.getNome());

            }
            if(extras.containsKey("chapas")){
                System.out.println("Tem as chapas");
                String chapas = extras.getString("chapas");

                try {
                    JSONArray array = new JSONArray(chapas);
                    System.out.println(array.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //titulo.setText(votacao.getNome());

        client = WLClient.createInstance(this);


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Credenciais do usuario
                String Login = login.getText().toString();
                String senhaUser = senha.getText().toString();

                //criação do objeto usuario
                User usuario = new User(Login,senhaUser);

                final JSONObject userJson = new JSONObject();

                try{
                    userJson.put("cpf",usuario.getCpf());
                    userJson.put("senha",usuario.getSenha());
                }catch (JSONException e){
                    e.printStackTrace();
                }

                //verificações de input
                if (TextUtils.isEmpty(login.getText().toString())){
                    erro_mens.setText("Login não preenchido");
                    erro_img.setImageResource(android.R.drawable.ic_delete);
                }else if(TextUtils.isEmpty(senha.getText().toString())){
                    erro_mens.setText("Senha não preenchida");
                    erro_img.setImageResource(android.R.drawable.ic_delete);
                }else{
                    //Validação de CPF
                    if (ValidaCPF.isCPF(Login) == true){

                        SafetyNet.getClient(LoginActivity.this).verifyWithRecaptcha(RECAPTCHA_TOKEN).addOnSuccessListener(LoginActivity.this, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Intent intent = new Intent(LoginActivity.this, opcoes_de_voto.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        erro_mens.setText("Erro no ReCaptcha!");
                                        erro_img.setImageResource(android.R.drawable.ic_delete);
                                    }
                                });

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
