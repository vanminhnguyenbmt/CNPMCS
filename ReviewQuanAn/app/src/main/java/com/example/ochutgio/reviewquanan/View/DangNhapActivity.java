package com.example.ochutgio.reviewquanan.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ochutgio.reviewquanan.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * Created by ochutgio on 4/3/2018.
 */

public class DangNhapActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener {

    LoginButton btnFacebookLogin;
    SignInButton btnGoogleSignin;
    TextView tvDangKy;
    TextView tvQuenMatKhau;

    GoogleApiClient apiClient;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    public static int RequestGoogleCode = 99;
    public static int SignIn_Flat = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_dangnhap);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();

        btnFacebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);
        btnGoogleSignin = (SignInButton) findViewById(R.id.btnGoogleSignin);
        tvDangKy = (TextView) findViewById(R.id.tvDangKy);
        tvQuenMatKhau =(TextView) findViewById(R.id.tvQuanMatKhau);


        tvDangKy.setOnClickListener((View.OnClickListener)this);
        tvQuenMatKhau.setOnClickListener((View.OnClickListener)this);
        btnGoogleSignin.setOnClickListener((View.OnClickListener) this);

        khoiTaoGoogleClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    /// Khởi tạo google signin và google api client
    private void khoiTaoGoogleClient(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void dangNhapFacebook(){
        btnFacebookLogin.setReadPermissions("email", "public_profile");
        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SignIn_Flat = 2;
                String TokenId = loginResult.getAccessToken().getToken();
                chungThucDangNhapFireBase(TokenId);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    /// Mở popup đăng nhập google với apiClient đã tạo
    private void dangNhapGoogle(GoogleApiClient apiClient){
        SignIn_Flat = 1;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RequestGoogleCode);
    }

    /// Hàm chứng thực đăng nhập firebase với TokenID(đại diện cho tài khoản của người dùng)
    private void chungThucDangNhapFireBase(String tokenid){
        if(SignIn_Flat == 1){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenid, null);
            mAuth.signInWithCredential(authCredential);
        }else if (SignIn_Flat == 2){
            AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenid);
            mAuth.signInWithCredential(authCredential);
        }
    }

    /// kết quả trả về sau khi hoàn thành các bước trong  popup đăng nhập google
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestGoogleCode){
            if(resultCode == RESULT_OK){
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount signInAccount = signInResult.getSignInAccount();
                String TokenId = signInAccount.getIdToken();
                chungThucDangNhapFireBase(TokenId);
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btnGoogleSignin:
                dangNhapGoogle(apiClient);
                break;
            case R.id.btnFacebookLogin:
                dangNhapFacebook();
                break;
            case R.id.tvDangKy:
                Intent iDangKy = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(iDangKy);
                break;
            case R.id.tvQuanMatKhau:
                Intent iQuenMatKhau = new Intent(DangNhapActivity.this, QuenMatKhauActivity.class);
                startActivity(iQuenMatKhau);
                break;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = mAuth.getCurrentUser();
        if( user != null){
            Intent iTrangChu = new Intent(this, TrangChuActivity.class);
            startActivity(iTrangChu);
        }else {

        }
    }
}
