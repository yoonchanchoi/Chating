package com.example.chating

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chating.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    lateinit var preference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initProperty()
        initializeView()
        initializeListener()
    }

    fun initProperty() {    //초기 변수 세팅
        auth = FirebaseAuth.getInstance()     //Firebase 계정 관련 변수
        preference = getSharedPreferences("setting", MODE_PRIVATE)    //로그인 정보 저장용 SharedPreference
    }

    fun initializeView() {   //뷰 초기화
        binding.edtEmail.setText(preference.getString("email", ""))     //마지막으로 로그인 한 이메일 세팅
        binding.edtPassword.setText(preference.getString("password", ""))   //마지막으로 로그인 한 패스워드 세팅
    }

    fun initializeListener() {        //버튼 클릭 시 리스너 세팅
        binding.btnSignIn.setOnClickListener()
        {
            signInWithEmailAndPassword()
        }
        binding.btnSignup.setOnClickListener()
        {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    fun signInWithEmailAndPassword() {
        if (binding.edtEmail.text.toString().isNullOrBlank() &&   //아이디 또는 패스워드가 입력되었는 지 유효성 검사
            binding.edtPassword.text.toString().isNullOrBlank())
            Toast.makeText(this, "아이디 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
        else {
            auth.signInWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())    //로그인 실행
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("로그인", "성공")
                        val user = auth.currentUser
                        updateUI(user)
                        finish()
                    } else {
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) { //로그인 성공 시 화면 이동
        if (user != null) {
            try {
                var preference = getSharedPreferences("setting", MODE_PRIVATE).edit()    //이메일 및 패스워드 저장
                preference.putString("email", binding.edtEmail.text.toString())
                preference.putString("password", binding.edtPassword.text.toString())
                preference.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}