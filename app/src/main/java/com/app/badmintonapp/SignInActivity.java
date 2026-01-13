package com.app.badmintonapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    // 1. 모든 입력칸 변수 선언
    private EditText etId, etPw, etPwConfirm;
    private EditText etName, etBirth, etPhone, etAddress, etAddressDetail;
    private RadioGroup rgGender; // 성별

    private MaterialCardView btnProceed;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();

        // 2. XML ID와 전부 연결
        etId = findViewById(R.id.et_signup_id);
        etPw = findViewById(R.id.et_signup_password);
        etPwConfirm = findViewById(R.id.et_signup_password_confirm);

        etName = findViewById(R.id.et_signup_name);
        etBirth = findViewById(R.id.et_signup_birth);
        etPhone = findViewById(R.id.et_signup_phone);
        etAddress = findViewById(R.id.et_signup_address);
        etAddressDetail = findViewById(R.id.et_signup_address_detail);

        rgGender = findViewById(R.id.rg_gender); // 성별 그룹 연결

        btnProceed = findViewById(R.id.btn_signup_proceed);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());
        btnProceed.setOnClickListener(v -> doSignUp());
    }

    private void doSignUp() {
        // 3. 입력된 값 가져오기
        String email = etId.getText().toString().trim();
        String pw = etPw.getText().toString().trim();
        String pw2 = etPwConfirm.getText().toString().trim();

        String name = etName.getText().toString().trim();
        String birth = etBirth.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();



        //  아이디 비번
        if (email.isEmpty() || pw.isEmpty() || pw2.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이름
        if (name.isEmpty()) {
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }

        // 생년월일
        if (birth.isEmpty()) {
            Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            etBirth.requestFocus();
            return;
        }

        //  성별
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 전화번호
        if (phone.isEmpty()) {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return;
        }

        /*
        // (6) 주소
        if (address.isEmpty()) {
            Toast.makeText(this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            etAddress.requestFocus();
            return;
        }

         */

        // 비밀번호 일치 확인
        if (!pw.equals(pw2)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pw.length() < 6) {
            Toast.makeText(this, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        //  회원가입 진행
        auth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String msg = "회원가입 실패";
                        if (task.getException() != null) {
                            msg = task.getException().getMessage();
                        }
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


