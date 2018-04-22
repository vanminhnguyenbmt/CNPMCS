package com.example.ochutgio.reviewquanan.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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
    Button btnDangNhap;
    EditText edEmail;
    EditText edPassword;

    GoogleApiClient apiClient;
    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;
    LoginManager loginManager;

    ProgressDialog progressDialog;

    public static int RequestGoogleCode = 99;
    public static int SignIn_Flat = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangnhap);

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        firebaseAuth.signOut();
        loginManager.logOut();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập");
        progressDialog.setIndeterminate(true);

        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnFacebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);
        btnGoogleSignin = (SignInButton) findViewById(R.id.btnGoogleSignin);
        tvDangKy = (TextView) findViewById(R.id.tvDangKy);
        tvQuenMatKhau =(TextView) findViewById(R.id.tvQuenMatKhau);
        edEmail = (EditText) findViewById(R.id.edEmailDN);
        edPassword = (EditText) findViewById(R.id.edPasswordDN);

        tvDangKy.setOnClickListener((View.OnClickListener) this);
        tvQuenMatKhau.setOnClickListener((View.OnClickListener) this);
        btnGoogleSignin.setOnClickListener((View.OnClickListener) this);
        //btnFacebookLogin.setOnClickListener((View.OnClickListener) this);
        btnDangNhap.setOnClickListener((View.OnClickListener) this);

        khoiTaoGoogleClient();

        btnFacebookLogin.setReadPermissions("email", "public_profile");
        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SignIn_Flat = 2;
                String TokenId = loginResult.getAccessToken().getToken();
                Toast.makeText(DangNhapActivity.this, "dang nhap facebook 2", Toast.LENGTH_SHORT).show();
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

    /// gọi hàm đăng nhập
    private void DangNhap(){
        String email =  edEmail.getText().toString();
        String password = edPassword.getText().toString();

        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(DangNhapActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                }
            }
        });
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

    /// Hàm chứng thực đăng nhập firebase với TokenID(đại diện cho tài khoản của người dùng)
    private void chungThucDangNhapFireBase(String tokenid){
        if(SignIn_Flat == 1){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenid, null);
            progressDialog.show();
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if (SignIn_Flat == 2){
            AuthCredential authCredentialFacebook = FacebookAuthProvider.getCredential(tokenid);
            progressDialog.show();
            firebaseAuth.signInWithCredential(authCredentialFacebook).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(DangNhapActivity.this, "dang nhap facebook thanh cong", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(DangNhapActivity.this, "dang nhap facebook that bai", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }


    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btnGoogleSignin:
                dangNhapGoogle(apiClient);
                break;
            case R.id.tvDangKy:
                Intent iDangKy = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(iDangKy);
                break;
            case R.id.tvQuenMatKhau:
                Intent iQuenMatKhau = new Intent(DangNhapActivity.this, QuenMatKhauActivity.class);
                startActivity(iQuenMatKhau);
                break;
            case R.id.btnDangNhap:
                DangNhap();
                break;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null){
            if(user.isEmailVerified() == true){
                Intent iTrangChu = new Intent(DangNhapActivity.this, TrangChuActivity.class);
                startActivity(iTrangChu);
            }else {
                Toast.makeText(DangNhapActivity.this, "Vui lòng xác thực email để thực hiện đăng nhập", Toast.LENGTH_SHORT).show();
            }

        }else {
        }
    }
}
