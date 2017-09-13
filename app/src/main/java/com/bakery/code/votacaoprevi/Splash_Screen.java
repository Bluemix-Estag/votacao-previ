package com.bakery.code.votacaoprevi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.bakery.code.votacaoprevi.helper.MobileFirstAdapter;
import com.bakery.code.votacaoprevi.models.Votacao;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Splash_Screen extends Activity {

    private WLAuthorizationManager mpfAdapter;
    private WLClient client;
    private JSONObject votacaoData;
    private Votacao votacao;
    private static final String LOG_TAG = "Connectivity";
    private String tituloVotacao ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isNetworkAvailable()){
                    Toast.makeText(Splash_Screen.this, "Não há conexão com a internet.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        },2000);


            client = MobileFirstAdapter.getMfpClient(this);


            mpfAdapter = MobileFirstAdapter.getMfpInstance();
            mpfAdapter.obtainAccessToken("", new WLAccessTokenListener() {
                @Override
                public void onSuccess(AccessToken accessToken) {
                    System.out.println("Received token: " + accessToken);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Conected to Mobile First Platform", Toast.LENGTH_SHORT).show();
                        }
                    });



                    URI adapterPath = null;
                    try {
                        adapterPath = new URI("/adapters/CloudantAdapter/resource/votacao");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);

                    request.send(new WLResponseListener() {
                        @Override
                        public void onSuccess(WLResponse wlResponse) {
                            votacaoData = wlResponse.getResponseJSON();
                            try {
                                JSONObject votacaoAtual = votacaoData.getJSONObject("votacaoAtual");

                                //Recuperar informaçoes da votacao
                                String nomeVotacao = votacaoAtual.getString("nome");
                                tituloVotacao = votacaoAtual.getString("nome");
                                String infoVotacao = votacaoAtual.getString("info");
                                String iniVotacao = votacaoAtual.getString("iniVotacao");
                                String fimVotacao = votacaoAtual.getString("fimVotacao");
                                JSONArray chapas = votacaoAtual.getJSONArray("chapas");

                                Log.i("MobileFirst Acionado", "Success: " + nomeVotacao);
                                //configurar o objeto votacao a ser passado
                                votacao = new Votacao();

                                votacao.setNome(nomeVotacao);
                                votacao.setInfo(infoVotacao);
                                votacao.setIniVotacao(iniVotacao);
                                votacao.setFimVotacao(fimVotacao);


                                showLogin(votacao, chapas);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("MobileFirst Acionado", "Success: " + wlResponse.getResponseText());

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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }



    private void showLogin(Votacao v, JSONArray chapas) {
        Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
        intent.putExtra("titulo",tituloVotacao);
        intent.putExtra("votacao",v);
        intent.putExtra("chapas",chapas.toString());
        startActivity(intent);
        finish();
    }
/*WLAuthorizationManager.getInstance().obtainAccessToken("", new WLAccessTokenListener() {
        @Override
        public void onSuccess(AccessToken accessToken) {
            System.out.println("Received token: " + accessToken);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Conected to Mobile First Platform", Toast.LENGTH_SHORT).show();
                }
            });
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
    //splash screen
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(8000);
                    Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
                    intent.putExtra("votacao",votacao);
                    startActivity(intent);
                    finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

        //splash screen
    */
}
