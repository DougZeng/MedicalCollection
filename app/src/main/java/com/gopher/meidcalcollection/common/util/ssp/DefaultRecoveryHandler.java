package com.gopher.meidcalcollection.common.util.ssp;

import android.content.SharedPreferences;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class DefaultRecoveryHandler extends RecoveryHandler {
    @Override
    protected boolean recover(Exception e, KeyStore keyStore, List<String> keyAliases, SharedPreferences preferences) {
        e.printStackTrace();

        try {
            clearKeyStore(keyStore, keyAliases);
            clearPreferences(preferences);
            return true;
        } catch (KeyStoreException e1) {
            e1.printStackTrace();
        }

        return false;
    }
}
