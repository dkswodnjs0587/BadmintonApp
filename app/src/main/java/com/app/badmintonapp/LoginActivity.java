package com.app.badmintonapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth; // 파이어베이스 인증 도구
    private EditText etId, etPassword;
    private MaterialCardView btnLogin;
    private TextView tvSignup, tvFindPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. 파이어베이스 준비
        auth = FirebaseAuth.getInstance();

        // 2. 화면의 도구들(XML)과 연결
        etId = findViewById(R.id.et_id);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignup = findViewById(R.id.tv_signup);
        tvFindPw = findViewById(R.id.tv_find_pw);

        // 3. 버튼 클릭 이벤트 설정

        // [로그인 버튼]
        btnLogin.setOnClickListener(v -> doLogin());

        // [회원가입 버튼] -> 회원가입 화면으로 이동
        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        // [비밀번호 찾기 버튼] (추후 구현)
        tvFindPw.setOnClickListener(v -> {
            Toast.makeText(this, "비밀번호 재설정 준비중.", Toast.LENGTH_SHORT).show();
        });
    }

    // 로그인 실행 함수
    private void doLogin() {
        String email = etId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 빈칸 검사
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 파이어베이스 로그인 요청
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공!
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                        // 메인 화면으로 이동
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        // 뒤로가기 눌렀을 때 로그인 화면 다시 안 나오게 설정
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        // 로그인 실패
                        String msg = "로그인 실패";
                        if (task.getException() != null) {
                            msg = task.getException().getMessage();
                        }
                        Toast.makeText(this, "오류: " + msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 뒤로가기 (XML에서 android:onClick="onBackClick"으로 연결됨)
    public void onBackClick(android.view.View view) {
        finish();
    }
}