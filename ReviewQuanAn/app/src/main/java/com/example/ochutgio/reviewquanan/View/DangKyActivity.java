package com.example.ochutgio.reviewquanan.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ochutgio.reviewquanan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ochutgio on 4/4/2018.
 */

public class DangKyActivity extends AppCompatActivity {

    Button btnDangKy;
    EditText edEmail;
    EditText edPassword;
    EditText edRepeatPassword;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);

        btnDangKy = (Button) findViewById(R.id.btnDangKy);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edRepeatPassword = (EditText) findViewById(R.id.edRepeatPassword);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String matkhau1 = edPassword.getText().toString();
                String matkhau2 = edRepeatPassword.getText().toString();

                String loi = "";

                progressDialog.setMessage("Đang xử lý");
                progressDialog.show();
                progressDialog.setIndeterminate(true);

                if(email.trim().length() == 0 | matkhau1.trim().length() == 0){
                    loi = "vui lòng nhập email hoặc password";
                    Toast.makeText(DangKyActivity.this, loi, Toast.LENGTH_SHORT).show();
                }else {
                    if(!matkhau1.equals(matkhau2)){
                        loi = "Hai mật khẩu cần trùng nhau";
                        Toast.makeText(DangKyActivity.this, loi, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.show();
                        firebaseAuth.createUserWithEmailAndPassword(email, matkhau1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.dismiss();
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(DangKyActivity.this, "Đăng ký thành công vui lòng xác thực email để đăng nhập", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                    }
                }
            }
        });
    }
}
