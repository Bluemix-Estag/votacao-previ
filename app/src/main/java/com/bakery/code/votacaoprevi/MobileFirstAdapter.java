package com.bakery.code.votacaoprevi;

import android.util.Log;
import android.widget.Toast;

import com.worklight.wlclient.api.WLAccessTokenListener;
import com.worklight.wlclient.api.WLAuthorizationManager;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import com.worklight.wlclient.auth.AccessToken;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Ramon on 17-Aug-17.
 */

public class MobileFirstAdapter {

    private static WLAuthorizationManager mfpInstance;

    public static WLAuthorizationManager getMfpInstance(){
        if(mfpInstance == null){
            mfpInstance = WLAuthorizationManager.getInstance();
        }
        return mfpInstance;
    }


}
