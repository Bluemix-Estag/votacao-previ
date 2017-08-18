package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bakery.code.votacaoprevi.models.Voto;
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


        }

        sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfpAdapter.obtainAccessToken("", new WLAccessTokenListener() {
                    @Override
                    public void onSuccess(AccessToken accessToken) {
                        System.out.println("Received token: " + accessToken);

                        URI adapterPath = null;
                        try {
                            adapterPath = new URI("/adapters/CloudantAdapter/resource/votar");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        try {
                            votoJson = new JSONObject();
                            votoJson.put("cpf", voto.getCpf());
                            votoJson.put("chapa", voto.getChapa());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.POST);

                        request.send(votoJson, new WLResponseListener() {
                            @Override
                            public void onSuccess(WLResponse wlResponse) {
                                System.out.println(wlResponse.getResponseText());
                                Intent intent = new Intent(Confirmar_voto.this, Fim_voto.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(WLFailResponse wlFailResponse) {

                            }
                        });

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
