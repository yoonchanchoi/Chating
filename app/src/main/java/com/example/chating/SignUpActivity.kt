package com.example.chating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chating.databinding.ActivitySignUpBinding
import com.example.chating.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth


    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        initializeListener()
    }

    fun initializeView() {  //뷰 초기화
        auth = FirebaseAuth.getInstance()
    }

    fun initializeListener() {   //버튼 클릭 시 리스너 초기화
        binding.btnSignup.setOnClickListener()
        {
            signUp()
        }
    }

    fun signUp() {     //회원 가입 실행
        var email = binding.edtEmail.text.toString()           //각 입력란 값 String으로 변환
        var password = binding.edtPassword.text.toString()
        var name = binding.edtOpponentName.text.toString()

        auth.createUserWithEmailAndPassword(email, password)      //FirebaseAuth에 회원가입 성공 시
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {     //회원 가입 성공 시
                    try {
                        val user = auth.currentUser
                        val userId = user?.uid
                        val userIdSt = userId.toString()
                        FirebaseDatabase.getInstance().getReference("User").child("users")
                            .child(userId.toString()).setValue(User(name, userIdSt, email))             //Firebase RealtimeDatabase에 User 정보 ㅊ추가
                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("UserId", "$userId")
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "화면 이동 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()

            }

    }
}