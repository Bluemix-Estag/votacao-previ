package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bakery.code.votacaoprevi.helper.MobileFirstAdapter;
import com.bakery.code.votacaoprevi.models.Voto;
import com.worklight.wlclient.api.WLAccessTokenListener;
import com.worklight.wlclient.api.WLAuthorizationManager;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import com.worklight.wlclient.auth.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class Confirmar_voto extends Activity {

    private Button sim;
    private Button nao;
    private TextView titulo;
    private TextView nomeChapa;

    private String userCpf;
    private WLClient client;
    private WLAuthorizationManager mfpAdapter;
    private Voto voto;
    private JSONObject votoJson;
    private String tituloVotacao;
    private JSONObject objRes;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_voto);

        sim = (Button) findViewById(R.id.SIM_CONFIRMAR);
        nao = (Button) findViewById(R.id.NAO_CONFIRMAR);
        titulo = (TextView) findViewById(R.id.opcoes_de_voto_Titulo);
        nomeChapa = (TextView) findViewById(R.id.nomeChapa);

        client = MobileFirstAdapter.getMfpClient(this);
        mfpAdapter = MobileFirstAdapter.getMfpInstance();

        Intent in = getIntent();

        Bundle extras = in.getExtras();

        voto = new Voto();

        if(extras != null){
            tituloVotacao = extras.getString("titulo");
            titulo.setText(tituloVotacao);
            if(extras.containsKey("userCpf")){
                userCpf = extras.getString("userCpf");
                voto.setCpf(userCpf);
            }

            if(extras.containsKey("nomeVotacao")){
                titulo.setText(extras.getString("nomeVotacao"));
            }
            if(extras.containsKey("chapaEscolhida")){
                String chapaEscolhida = extras.getString("chapaEscolhida");
                try {
                    JSONObject chapa = new JSONObject(chapaEscolhida);
                    System.out.println(chapa);
                    nomeChapa.setText(chapa.getString("nome"));
                    String idChapa = chapa.getString("id");

                    voto.setChapa(idChapa);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(extras.containsKey("token")){
                token = extras.getString("token");
            }


        }

        sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfpAdapter.obtainAccessToken("", new WLAccessTokenListener() {
                    @Override
                    public void onSuccess(AccessToken accessToken) {


                        URI adapterPath = null;
                        try {
                            adapterPath = new URI("/adapters/VotarJS/doVotar");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        try {
                            votoJson = new JSONObject();
                            votoJson.put("matricula", voto.getCpf());
                            votoJson.put("voto", voto.getChapa());
                            votoJson.put("access_token", token);

                            System.out.println("VotoJSON: " + votoJson.toString());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.POST);
                        HashMap formparams = new HashMap();


                        formparams.put("params", "['"+voto.getCpf()+"','"+voto.getChapa()+"','"+token+"']");
                        System.out.println("Formparams: " + formparams.get("params"));
                        request.setQueryParameters(formparams);
                        request.send(formparams, new WLResponseListener() {
                            @Override
                            public void onSuccess(WLResponse wlResponse) {
                                objRes = wlResponse.getResponseJSON();

                                try {
                                    if(objRes.getString("codRetorno").equals("0")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                    Toast.makeText(Confirmar_voto.this, "Voto computado com Sucesso.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else if(objRes.getString("codRetorno").equals("3")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Confirmar_voto.this, "Não é permitido alterar a data de votação de associado que já votou", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Toast.makeText(Confirmar_voto.this, objRes.getString("msgRetorno"), Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Sucesso no voto." + wlResponse.getResponseText());
                            }

                            @Override
                            public void onFailure(WLFailResponse wlFailResponse) {
                                System.out.println("Erro: " + wlFailResponse.getErrorMsg());
                            }
                        });
//                        request.send(votoJson, new WLResponseListener() {
//                            @Override
//                            public void onSuccess(WLResponse wlResponse) {
//                                System.out.println(wlResponse.getResponseText());
//                                Intent intent = new Intent(Confirmar_voto.this, Fim_voto.class);
//                                intent.putExtra("titulo",titulo.getText().toString());
//                                startActivity(intent);
//                                finish();
//                            }
//
//                            @Override
//                            public void onFailure(WLFailResponse wlFailResponse) {
//                                System.out.println(wlFailResponse.getErrorMsg());
//                            }
//                        });



                    }

                    @Override
                    public void onFailure(WLFailResponse wlFailResponse) {

                    }
                });


            }
        });

        nao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
}
