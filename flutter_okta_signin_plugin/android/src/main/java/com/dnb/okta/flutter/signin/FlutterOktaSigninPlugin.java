package com.dnb.okta.flutter.signin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.okta.oidc.AuthorizationStatus;
import com.okta.oidc.OIDCConfig;
import com.okta.oidc.Okta;
import com.okta.oidc.RequestCallback;
import com.okta.oidc.ResultCallback;
import com.okta.oidc.Tokens;
import com.okta.oidc.clients.sessions.SessionClient;
import com.okta.oidc.clients.web.WebAuthClient;
import com.okta.oidc.net.response.IntrospectInfo;
import com.okta.oidc.net.response.UserInfo;
import com.okta.oidc.util.AuthorizationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/**
 * FlutterOktaSigninPlugin
 */
public class FlutterOktaSigninPlugin implements FlutterPlugin, MethodCallHandler, ConfigKeys,
        Methods, ActivityAware, PluginRegistry.ActivityResultListener {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private static MethodChannel channel;
    private static final String CHANNEL = "com.dnb.okta/login";
    private static WebAuthClient mWebAuth;
    private static OktaProgressDialog oktaProgressDialog;
    private JSONObject configuration;
    private final static String FIRE_FOX = "org.mozilla.firefox";
    private final static String ANDROID_BROWSER = "com.android.browser";
    private static SessionClient sessionClient;
    private MethodChannel.Result result;
    private Activity context;

    /** Plugin registration. */
    public static void registerWith(PluginRegistry.Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), CHANNEL);
        channel.setMethodCallHandler(new FlutterOktaSigninPlugin());
    }
    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals(config)) {
            try {
                configuration = new JSONObject(call.arguments.toString());
                config();
                // Log.e(TAG, "config data " + configuration);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (call.method.equals(signIn)) {
            login();
            this.result = result;
        } else if (call.method.equals(isSignedIn)) {
            result.success(isSignedIn());
        } else if (call.method.equals(getUserProfile)) {
            getUserProfile();
            this.result = result;
        } else if (call.method.equals(signOut)) {
            signOut();
            this.result = result;
        } else {
            result.notImplemented();
            showMessage("Method not implemented");
        }
    }

    String[] getScopes() throws Exception {
        final JSONArray jsonArray = configuration.getJSONArray(SCOPES);
        final List<String> list = new ArrayList<>(jsonArray.length());
        final String[] scopes = new String[list.size()];
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.get(i).toString());
        }
        return list.toArray(scopes);
    }

    private void init() {
        try {
            final OIDCConfig config = new OIDCConfig.Builder()
                    .clientId(configuration.getString(CLIENT_ID))
                    .discoveryUri(configuration.getString(DISCOVERY_URI))
                    .redirectUri(configuration.getString(REDIRECT_URI))
                    .endSessionRedirectUri(configuration.getString(END_SESSION_REDIRECT_URI))
                    .scopes(getScopes())
                    .create();


            mWebAuth = new Okta.WebAuthBuilder()
                    .withConfig(config)
                    .withContext(this.context)
                    .create();

            ResultCallback<AuthorizationStatus, AuthorizationException> callback =
                    new ResultCallback<AuthorizationStatus, AuthorizationException>() {
                        @Override
                        public void onSuccess(@NonNull AuthorizationStatus status) {
                            try {
                                oktaProgressDialog.hide();
                                showMessage("Success " + status.name());
                                if (status.name().equalsIgnoreCase(AuthorizationStatus.AUTHORIZED.name())) {
                                    // Log.e(TAG, "suman success");
                                    final JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("access_token", "" + sessionClient.getTokens().getAccessToken());
                                    jsonObject.put("refresh_token", "" + sessionClient.getTokens().getRefreshToken());
                                    jsonObject.put("id_token", "" + sessionClient.getTokens().getIdToken());
                                    jsonObject.put("expires_in", "" + sessionClient.getTokens().getExpiresIn());
                                    result.success(jsonObject.toString());
                                } else if (status.name().equalsIgnoreCase(AuthorizationStatus.ERROR.name())) {
                                    result.error("501", "ERROR", "ERROR on Success");
                                } else if (status.name().equals(AuthorizationStatus.CANCELED)) {
                                    result.error("500", "CANCELED", "CANCELED on Success");
                                } else if (status.name().equals(AuthorizationStatus.EMAIL_VERIFICATION_AUTHENTICATED)) {
                                    result.success("success" + sessionClient.getTokens().getAccessToken());
                                } else if (status.name().equals(AuthorizationStatus.EMAIL_VERIFICATION_UNAUTHENTICATED)) {
                                    result.error("401", "EMAIL_VERIFICATION_UNAUTHENTICATED", "");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancel() {
                            oktaProgressDialog.hide();
                            showMessage("onCancel");
                            result.error("cancelled", "", "");

                        }

                        @Override
                        public void onError(@Nullable String msg, AuthorizationException error) {
                            showMessage("onError "+ error.errorDescription + "Check Logs");
                            oktaProgressDialog.hide();
                            result.error(error.error, error.errorDescription, "Could not login");
                        }
                    };

            mWebAuth.registerCallback(callback, this.context);
            sessionClient = mWebAuth.getSessionClient();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login() {
        showLoading();
        if (mWebAuth != null) {
            mWebAuth.signIn(this.context, null);
        } else {
            config();
        }
    }

    private void config() {
        init();
    }

    private boolean isSignedIn() {
        if (sessionClient != null) {
            return sessionClient.isAuthenticated();
        } else {
            config();
            isSignedIn();
        }
        return false;
    }

    private void getUserProfile() {
        if (sessionClient != null) {
            sessionClient.getUserProfile(new RequestCallback<UserInfo, AuthorizationException>() {
                @Override
                public void onSuccess(@NonNull UserInfo res) {
                    final JSONObject jsonObject = new JSONObject();
                    result.success(res.getRaw().toString());
                }

                @Override
                public void onError(String error, AuthorizationException exception) {
                    result.error("401", exception.errorDescription, "Could not able to get user info");
                }
            });
        }
    }

    private void refreshToken() {
        if (sessionClient != null) {
            sessionClient.refreshToken(new RequestCallback<Tokens, AuthorizationException>() {
                @Override
                public void onSuccess(@NonNull Tokens res) {
                    result.success(res.getAccessToken());
                }

                @Override
                public void onError(String error, AuthorizationException exception) {
                    result.error("401", exception.errorDescription, "Could not refresh token");
                }
            });
        }
    }

    private void introspectToken() {
        if (sessionClient != null) {
            try {
                sessionClient.introspectToken(sessionClient.getTokens().getAccessToken(),
                        "", new RequestCallback<IntrospectInfo, AuthorizationException>() {
                            @Override
                            public void onSuccess(@NonNull IntrospectInfo res) {

                            }

                            @Override
                            public void onError(String error, AuthorizationException exception) {
                                result.error("401", exception.errorDescription, "Could not retrospect token");
                            }
                        }
                );
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }
    }

    private void revokeToken() {
        if (sessionClient != null) {
            showLoading();
            try {
                sessionClient.revokeToken(sessionClient.getTokens().getAccessToken(), new RequestCallback<Boolean, AuthorizationException>() {
                    @Override
                    public void onSuccess(@NonNull Boolean res) {
                        oktaProgressDialog.hide();
                        sessionClient.clear();
                        result.success(res.booleanValue());
                    }

                    @Override
                    public void onError(String error, AuthorizationException exception) {
                        oktaProgressDialog.hide();
                        result.error("401", exception.errorDescription, "Could not revoke token");
                    }
                });
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }
    }

    private void signOut() {
        try {
            oktaProgressDialog.show();
            if (sessionClient.getTokens().getAccessToken() != null) {
                mWebAuth.signOut(this.context, new RequestCallback<Integer, AuthorizationException>() {
                    @Override
                    public void onSuccess(@NonNull Integer res) {
                        oktaProgressDialog.hide();
                       /*
                       Calling clear() discards tokens from local device storage,
                        but they are technically still active until they expire.
                         An optional step is to revoke the tokens so they can't be used,
                          even by accident. You can revoke tokens using the following request:
                        */
                        revokeToken();
                    }

                    @Override
                    public void onError(String error, AuthorizationException exception) {
                        oktaProgressDialog.hide();
                        if (sessionClient != null) {
                            sessionClient.clear();
                            ;
                        }
                        result.error("401", exception.errorDescription, error);
                    }
                });
            } else {
                config();
                signOut();
            }
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }
    }

    void showMessage(String message) {
        Log.e("Login Error", " error "+message);
        Toast.makeText(this.context, ""+message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        binding.addActivityResultListener(this);
       this.context = binding.getActivity();
    }

    private void showLoading() {
        oktaProgressDialog = new OktaProgressDialog(this.context);
        oktaProgressDialog.show("Loading...Please wait");
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        this.context = null;
        oktaProgressDialog = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        binding.addActivityResultListener(this);
        this.context = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        this.context = null;
        oktaProgressDialog = null;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        //must pass the results back to the WebAuthClient.
        mWebAuth.handleActivityResult(requestCode, resultCode, data);
        return true;
    }
}
