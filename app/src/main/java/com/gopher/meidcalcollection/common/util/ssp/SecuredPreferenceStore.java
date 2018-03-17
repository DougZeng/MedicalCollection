package com.gopher.meidcalcollection.common.util.ssp;

import android.content.Context;
import android.content.SharedPreferences;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by Administrator on 2018/3/7.
 */

/*Usage
Before using the store for the first time you must initialize it

SecuredPreferenceStore.init(getApplicationContext(), new DefaultRecoveryHandler());
perhaps in onCreate of your Application class or launcher Activity.

You can use the secured preference store just like the way you use the default SharedPreferences

SecuredPreferenceStore prefStore = SecuredPreferenceStore.getSharedInstance(getApplicationContext());

String textShort = prefStore.getString(TEXT_1, null);
String textLong = prefStore.getString(TEXT_2, null);
int numberInt = prefStore.getInt(NUMBER_1, 0);

prefStore.edit().putString(TEXT_1, text1.length() > 0 ? text1.getText().toString() : null).apply();
prefStore.edit().putString(TEXT_2, text2.length() > 0 ? text2.getText().toString() : null).apply();
prefStore.edit().putInt(NUMBER_1, number1.length() > 0 ? Integer.parseInt(number1.getText().toString().trim()) : 0).commit();
You can use the EncryptionManager to encrypt/decrypt data on your own:

EncryptionManager encryptionManager = new EncryptionManager(getApplicationContext(), getSharedPreferences("my_pref", MODE_PRIVATE));
EncryptionManager.EncryptedData encryptedData = encryptionManager.encrypt(bytesToEncrypt);
byte[] decryptedData = encryptionManager.decrypt(encryptedData);
Sample file content
A sample secured preference file will look like:

<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="11CD15241CB4D6F953FA27C76F72C10920C5FADF14FF2824104FA5D67D25B43C">ZMnr87IlDKg81hKw2SQ6Lw==]dhP/ymX7CMSaCkP6jQvNig==</string>
    <string name="C8D076EFD8542A5F02F86B176F667B42BFB9B1472E974E6AF31EB27CEA5689D4">JQ6Y4TQ/Y3iYw7KtatkqAg==]P+gpavV0MXiy1Qg0UHlBMg==</string>
    <string name="F2AA713F406544A3E9ABA20A32364FA29613F01C867B3D922A85DF4FA54FA13D">jMH1Wjnk0vehHOogT27HRA==]e8UHX1ihYjtP6Cv8dWdHLBptLwowt6IojKYa+1jkeH4=</string>
    <string name="C06C6027E72B7CE947885F6ADE3A73E338881197DBE02D8B7B7248F629BE26DA">EAGwO8u2ZPdxwdpAwPlu6A==]797VOGtpzDBO1ZU3m+Sb1A==</string>
    <string name="33188AFFEC74B412765C3C86859DE4620B5427C774D92F9026D95A7A8AAE1F96">s0b5h8XNnerci5AtallCQziSbqpm+ndjIsAQQadSxM+xzw7865sE3P+hbxGmMAQQj0kK35/C//eA
MXuQ0N/F+oapBiDIKdRt2GJB3wJ+eshuh6TcEv+J8NQhqn1eO2fdao353XthHpRomIeGEWLvB4Yd
7G5YYIajLWOGWzQVsMTg1eqdcJ7+BAMXdOdWhjTTo91NvhvykgLMC03FsePOZ/X8ej4vByH1i0en
hJCiChk90AQ9FhSkaF/Oum9KoWqg7NU0PGurK755VZflXfyn1vZ8hhTulW7BrA2o9HvT9tbju+bk
4yJ5lMxgS6o4b+0tqo+H4TPOUiZPgehTwsrzJg==
    </string>
    <string name="9DCB904DFDA83286B41A329A7D8648B0BFF73C63E844C88800B2AA5119204845">XPuUd1t97pnwsOzzHY3OCA==]xqXJrEfcgDhYo2K4TTAvY9IQwP/tGctd4Fa1JT/1sB8=</string>
</map>
NOTICE
The keys stored in the KeyStore aren't encrypted at rest to avoid the issue where they get deleted when the device's lock screen protection changes. So if the device doesn't have a hardware backed key storage then the keys might be at a vulnerable state. You can read more about it here.

Recovery
Keys get deleted/locked in API levels lower than 21 and sometimes on later versions of the API on some devices when the user changes the device's security (screen lock protection). This phenomena is due to few issues in the Keystore implementation i.e 61989, 177459. Until those issues are fixed we need a way to recover from that scenario, otherwise the app itself might become unusable. To enable recovery you can add a RecoveryHandler to SecuredPreferenceStore before calling getSharedInstance for the first time.

SecuredPreferenceStore.setRecoveryHandler(new RecoveryHandler() {
            @Override
            protected void recover(Exception e, KeyStore keyStore, List<String> keyAliases, SharedPreferences preferences) {
                //Your recovery code goes here
            }
        });
A default recovery handler called DefaultRecoveryHandler is included in the library which deletes the keys and data, giving the library a chance to start over.*/

public class SecuredPreferenceStore implements SharedPreferences {
    private final String PREF_FILE_NAME = "SPS_file";

    private SharedPreferences mPrefs;
    private EncryptionManager mEncryptionManager;

    private static RecoveryHandler mRecoveryHandler;

    private static SecuredPreferenceStore mInstance;

    private SecuredPreferenceStore(Context appContext) throws Exception {
        Logger.d("SECURE-PREFERENCE", "Creating store instance");
        mPrefs = appContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        mEncryptionManager = new EncryptionManager(appContext, mPrefs, new KeyStoreRecoveryNotifier() {
            @Override
            public boolean onRecoveryRequired(Exception e, KeyStore keyStore, List<String> keyAliases) {
                if (mRecoveryHandler != null)
                    return mRecoveryHandler.recover(e, keyStore, keyAliases, mPrefs);
                else throw new RuntimeException(e);
            }
        });
    }

    public static void setRecoveryHandler(RecoveryHandler recoveryHandler) {
        SecuredPreferenceStore.mRecoveryHandler = recoveryHandler;
    }

    synchronized public static SecuredPreferenceStore getSharedInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Must call init() before using the store");
        }

        return mInstance;
    }

    /**
     * Must be called once before using the SecuredPreferenceStore to initialize the shared instance.
     * You may call it in @code{onCreate} method of your application class or launcher activity
     *
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnrecoverableEntryException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     */
    public static void init(Context appContext,
                            RecoveryHandler recoveryHandler) throws Exception {

        if (mInstance != null) {
            Logger.w("SECURED-PREFERENCE", "init called when there already is a non-null instance of the class");
            return;
        }

        setRecoveryHandler(recoveryHandler);
        mInstance = new SecuredPreferenceStore(appContext);
    }

    public EncryptionManager getEncryptionManager() {
        return mEncryptionManager;
    }

    @Override
    public Map<String, String> getAll() {
        Map<String, ?> all = mPrefs.getAll();
        Map<String, String> dAll = new HashMap<>(all.size());

        if (all.size() > 0) {
            for (String key : all.keySet()) {
                try {
                    dAll.put(key, mEncryptionManager.decrypt((String) all.get(key)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dAll;
    }

    @Override
    public String getString(String key, String defValue) {
        try {
            String hashedKey = EncryptionManager.getHashed(key);
            String value = mPrefs.getString(hashedKey, null);
            if (value != null) return mEncryptionManager.decrypt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defValue;
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        try {
            String hashedKey = EncryptionManager.getHashed(key);
            Set<String> eSet = mPrefs.getStringSet(hashedKey, null);

            if (eSet != null) {
                Set<String> dSet = new HashSet<>(eSet.size());

                for (String val : eSet) {
                    dSet.add(mEncryptionManager.decrypt(val));
                }

                return dSet;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return defValues;
    }

    @Override
    public int getInt(String key, int defValue) {
        String value = getString(key, null);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        String value = getString(key, null);
        if (value != null) {
            return Long.parseLong(value);
        }
        return defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        String value = getString(key, null);
        if (value != null) {
            return Float.parseFloat(value);
        }
        return defValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String value = getString(key, null);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defValue;
    }

    public byte[] getBytes(String key) {
        String val = getString(key, null);
        if (val != null) {
            return EncryptionManager.base64Decode(val);
        }

        return null;
    }

    @Override
    public boolean contains(String key) {
        try {
            String hashedKey = EncryptionManager.getHashed(key);
            return mPrefs.contains(hashedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        if (mPrefs != null)
            mPrefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        if (mPrefs != null)
            mPrefs.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public class Editor implements SharedPreferences.Editor {
        SharedPreferences.Editor mEditor;

        public Editor() {
            mEditor = mPrefs.edit();
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            try {
                String hashedKey = EncryptionManager.getHashed(key);
                String evalue = mEncryptionManager.encrypt(value);
                mEditor.putString(hashedKey, evalue);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            try {
                String hashedKey = EncryptionManager.getHashed(key);
                Set<String> eSet = new HashSet<String>(values.size());

                for (String val : values) {
                    eSet.add(mEncryptionManager.encrypt(val));
                }

                mEditor.putStringSet(hashedKey, eSet);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return this;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            String val = Integer.toString(value);
            return putString(key, val);
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            String val = Long.toString(value);
            return putString(key, val);
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            String val = Float.toString(value);
            return putString(key, val);
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            String val = Boolean.toString(value);
            return putString(key, val);
        }

        public SharedPreferences.Editor putBytes(String key, byte[] bytes) {
            if (bytes != null) {
                String val = EncryptionManager.base64Encode(bytes);
                return putString(key, val);
            } else return remove(key);
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            try {
                String hashedKey = EncryptionManager.getHashed(key);
                mEditor.remove(hashedKey);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            mEditor.clear();

            return this;
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        public void apply() {
            mEditor.apply();
        }
    }

    public interface KeyStoreRecoveryNotifier {
        /**
         * @param e
         * @param keyStore
         * @param keyAliases
         * @return true if the error could be resolved
         */
        boolean onRecoveryRequired(Exception e, KeyStore keyStore, List<String> keyAliases);
    }
}
