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

import com.bakery.code.votacaoprevi.helper.MobileFirstAdapter;
import com.bakery.code.votacaoprevi.helper.ValidaCPF;
import com.bakery.code.votacaoprevi.models.User;
import com.bakery.code.votacaoprevi.models.Votacao;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.worklight.wlclient.api.WLAccessTokenListener;
import com.worklight.wlclient.api.WLAuthorizationManager;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import com.worklight.wlclient.auth.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


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
    private WLAuthorizationManager mfpAdapter;

    String chapas;

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

        //teste com credenciais
        login.setText("20274526301");
        senha.setText("123quatro");

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
                chapas = extras.getString("chapas");

                try {
                    JSONArray array = new JSONArray(chapas);
                    System.out.println(array.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //titulo.setText(votacao.getNome());

        client = MobileFirstAdapter.getMfpClient(this);

        mfpAdapter = MobileFirstAdapter.getMfpInstance();

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //verificações de input
                if (TextUtils.isEmpty(login.getText().toString())){
                    erro_mens.setText("Login não preenchido");
                    erro_img.setImageResource(android.R.drawable.ic_delete);
                }else if(TextUtils.isEmpty(senha.getText().toString())){
                    erro_mens.setText("Senha não preenchida");
                    erro_img.setImageResource(android.R.drawable.ic_delete);
                }else{
                    //Credenciais do usuario
                    String Login = login.getText().toString();
                    String senhaUser = senha.getText().toString();

                    //criação do objeto usuario
                    final User usuario = new User(Login,senhaUser);

                    final JSONObject userJson = new JSONObject();

                    try{
                        userJson.put("cpf",usuario.getCpf());
                        userJson.put("senha",usuario.getSenha());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    //Validação de CPF
                    if (ValidaCPF.isCPF(Login) == true){

                        SafetyNet.getClient(LoginActivity.this).verifyWithRecaptcha(RECAPTCHA_TOKEN).addOnSuccessListener(LoginActivity.this, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        mfpAdapter.obtainAccessToken("", new WLAccessTokenListener() {
                                            @Override
                                            public void onSuccess(AccessToken accessToken) {
                                                System.out.println("Received token: " + accessToken);

                                                URI adapterPath = null;
                                                try {
                                                    adapterPath = new URI("/adapters/CloudantAdapter/resource/login");
                                                } catch (URISyntaxException e) {
                                                    e.printStackTrace();
                                                }

                                                WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.POST);

                                                request.send(userJson,new WLResponseListener() {
                                                    @Override
                                                    public void onSuccess(WLResponse wlResponse) {
                                                        // Will print "Hello world" in LogCat.
                                                        Log.i("MobileFirst Acionado", "Success: " + wlResponse.getResponseText());

                                                        try {
                                                            if(wlResponse.getResponseJSON().getString("error").equals("true")){
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        erro_mens.setText("Acesso negado, seu voto já foi registrado, agradecemos a sua participação!");
                                                                        erro_img.setImageResource(android.R.drawable.ic_delete);
                                                                    }
                                                                });
                                                            }else{
                                                                Intent intent = new Intent(LoginActivity.this, opcoes_de_voto.class);
                                                                intent.putExtra("nomeVotacao", titulo.getText().toString());
                                                                intent.putExtra("chapas",chapas);
                                                                intent.putExtra("user",usuario.getCpf());
                                                                startActivity(intent);
                                                                finish();
                                                            }


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(WLFailResponse wlFailResponse) {
                                                        Log.i("MobileFirst Quick Start", "Failure: " + wlFailResponse.getErrorMsg());
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(WLFailResponse wlFailResponse) {

                                            }
                                        });
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
                finish();
            }
        });
    }
}
