package com.example.ochutgio.reviewquanan.View;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;


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
    FirebaseAuth firebaseAuth;
    LoginManager loginManager;

    public static int RequestGoogleCode = 99;
    public static int SignIn_Flat = 0;

    List<String> permissionFacebook = Arrays.asList("email","public_profile");
//    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_dangnhap);

        callbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();
        loginManager = LoginManager.getInstance();

        firebaseAuth.signOut();
        loginManager.logOut();


        btnFacebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);
        btnGoogleSignin = (SignInButton) findViewById(R.id.btnGoogleSignin);
        tvDangKy = (TextView) findViewById(R.id.tvDangKy);
        tvQuenMatKhau =(TextView) findViewById(R.id.tvQuenMatKhau);


        tvDangKy.setOnClickListener((View.OnClickListener)this);
        tvQuenMatKhau.setOnClickListener((View.OnClickListener)this);
        btnGoogleSignin.setOnClickListener((View.OnClickListener) this);
        btnFacebookLogin.setOnClickListener((View.OnClickListener)this);

        khoiTaoGoogleClient();
    }


    /// mở popup đăng nhập với facebook
    private void dangNhapFacebook(){
        loginManager.logInWithReadPermissions(this,permissionFacebook);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(DangNhapActivity.this, "dang nhap facebook 3", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    /// Mở popup đăng nhập google với apiClient đã tạo
    private void dangNhapGoogle(GoogleApiClient apiClient){
        SignIn_Flat = 1;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RequestGoogleCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    /// Hàm chứng thực đăng nhập firebase với TokenID(đại diện cho tài khoản của người dùng)
    private void chungThucDangNhapFireBase(String tokenid){
        if(SignIn_Flat == 1){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenid, null);
            firebaseAuth.signInWithCredential(authCredential);
        }else if (SignIn_Flat == 2){
            final AuthCredential authCredentialFacebook = FacebookAuthProvider.getCredential(tokenid);
            firebaseAuth.signInWithCredential(authCredentialFacebook);
            Toast.makeText(this, "dang nhap facebook 2", Toast.LENGTH_SHORT).show();
        }
    }

    /// kết quả trả về sau khi hoàn thành các bước trong  popup đăng nhập google
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            Toast.makeText(this, "dang nhap facebook", Toast.LENGTH_SHORT).show();
        }
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
            case R.id.tvQuenMatKhau:
                Intent iQuenMatKhau = new Intent(DangNhapActivity.this, QuenMatKhauActivity.class);
                startActivity(iQuenMatKhau);
                break;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null){

//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("mauser",user.getUid());
//            editor.commit();
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            Intent iTrangChu = new Intent(DangNhapActivity.this, TrangChuActivity.class);
            startActivity(iTrangChu);
        }else {

        }
    }
}
