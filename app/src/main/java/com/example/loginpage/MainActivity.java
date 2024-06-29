package com.example.loginpage;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etIdentifier, etPassword;
    private Button btn;
    private TextView txt , Fpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check if user is already logged in
        if (isLoggedIn()) {
            navigateToHome();
        }
    }

    private void initViews() {
        etIdentifier = findViewById(R.id.etIdentifier); // Updated ID
        etPassword = findViewById(R.id.etPassword); // Updated ID
        btn = findViewById(R.id.login);
        txt = findViewById(R.id.txt);
        Fpassword = findViewById(R.id.Fpassword);
    }

    private void setupListeners() {
        txt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
            finish();
        });
        Fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(v -> {
            String identifier = etIdentifier.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (isValid(identifier, password)) {
                signIn(identifier, password);
            }
        });
    }

    private boolean isValid(String identifier, String password) {
        if (identifier.isEmpty()) {
            Toast.makeText(this, "Username or Email is empty", Toast.LENGTH_SHORT).show();
            etIdentifier.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void signIn(final String identifier, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endspoint.login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Server Response", response.trim());
                        handleLoginResponse(response.trim());
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("identifier", identifier); // Updated key to match server-side
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void handleLoginResponse(String response) {
        switch (response.toLowerCase()) {
            case "success":
                showToast("Login successfully");

                // Save login state
                saveLoginState();

                // Navigate to HomeActivity
                navigateToHome();
                break;
            case "invalid":
                showToast("Invalid username or password");
                etPassword.setText(null); // Clear password field on invalid login
                etPassword.requestFocus();
                break;
            default:
                showToast("Unexpected response: " + response);
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveLoginState() {
        // Save login state in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private boolean isLoggedIn() {
        // Check if user is logged in based on stored session data
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn", false);
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainActivity.this, home.class);
        startActivity(intent);
        finish();
    }
}
