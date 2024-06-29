package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText etName, etemail, etPhone, etPassword;
    private Button btnRegister;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        etName = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etPhone = findViewById(R.id.etphone);
        etPassword = findViewById(R.id.etpassword);
        btnRegister = findViewById(R.id.reg);
        txtLogin = findViewById(R.id.txt);
    }

    private void setupListeners() {
        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etemail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (isValid(name,email, phone, password)) {
                registerUser(name,email, phone, password);
            }
        });
    }

    private boolean isValid(String name,String email, String phone, String password) {
        if (name.isEmpty()) {
            showToast("Please enter username");
            etName.requestFocus();
            return false;
        }
        if (name.length() > 20) {
            showToast("Username is too long");
            etName.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            showToast("Please enter your email");
            etName.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            showToast("Please enter phone number");
            etPhone.requestFocus();
            return false;
        }
        if (phone.length() != 11) {
            showToast("Please enter a valid phone number");
            etPhone.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            showToast("Please enter password");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6 || password.length() > 8) {
            showToast("Password must be between 6 & 8 characters");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void registerUser(final String name,final String email, final String phone, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endspoint.register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Server Response", response);
                        handleResponse(response.trim());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Please check your internet connection");
                        Log.d("VOLLEY", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void handleResponse(String response) {
        switch (response.toLowerCase()) {
            case "success":
                showToast("Registration successful");
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case "exists_username":
                showToast("Username already exists");
                etName.requestFocus();
                break;
            case "exists_email":
                showToast("This email already registered");
                etName.requestFocus();
                break;
            case "exists_phone":
                showToast("Phone number already registered");
                etPhone.requestFocus();
                break;
            case "exists_password":
                showToast("Please choose a different password");
                etPassword.requestFocus();
                break;
            default:
                showToast("Registration failed: " + response);
                break;
        }
    }


}