package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    EditText signId, signName, signPwd, signPwdDoubleCheck, signEmail, verifyCodeTextInput;
    Button idCheckButton, nicknameCheckButton, sendVerifyCodeButton, verifyCodeCheckButton, btnSignUp;
    TextView verifyResultText;

    private static final String TAG = "SignUp";
    private static final String host = "http://ipark4.duckdns.org:58395";
    private static final String CHECK_USER_ID_URL = host + "/api/create/register/check/userID";  // CheckUserID.py의 엔드포인트로 변경하세요
    private static final String CHECK_USER_NICKNAME_URL = host + "/api/create/register/check/nickname";  // CheckUserNickname.py의 엔드포인트로 변경하세요
    private static final String SEND_VERIFY_CODE_URL = host + "/api/create/register/check/email/send";  // SendVerifyCode.py의 엔드포인트로 변경하세요
    private static final String CHECK_VERIFY_CODE_URL = host + "/api/create/register/check/email/verify";  // CheckVerifyCode.py의 엔드포인트로 변경하세요
    private static final String REGISTER_URL = host + "/api/create/register";  // Register.py의 엔드포인트로 변경하세요

    private boolean isIdChecked = false;
    private boolean isNicknameChecked = false;
    private boolean isPasswordMatching = false;
    private boolean isEmailVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        btnBack = findViewById(R.id.btn_back);
        signId = findViewById(R.id.sign_id);
        signName = findViewById(R.id.sign_name);
        signPwd = findViewById(R.id.sign_pwd);
        signPwdDoubleCheck = findViewById(R.id.sign_pwdDoubleCheck);
        signEmail = findViewById(R.id.sign_email);
        verifyCodeTextInput = findViewById(R.id.verifyCodeTextInput);
        idCheckButton = findViewById(R.id.idCheckButton);
        nicknameCheckButton = findViewById(R.id.nicknameCheckButton);
        sendVerifyCodeButton = findViewById(R.id.sendVerifyCodeButton);
        verifyCodeCheckButton = findViewById(R.id.verifyCodeCheckButton);
        btnSignUp = findViewById(R.id.btn_sign_up);
        verifyResultText = findViewById(R.id.verifyResultText);

        btnBack.setOnClickListener(this);
        idCheckButton.setOnClickListener(this);
        nicknameCheckButton.setOnClickListener(this);
        sendVerifyCodeButton.setOnClickListener(this);
        verifyCodeCheckButton.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        btnSignUp.setEnabled(false);

        signPwdDoubleCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwdString = signPwd.getText().toString();
                String passwdChkString = s.toString();

                if((passwdString.isEmpty() && passwdChkString.isEmpty())){
                    signPwdDoubleCheck.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    isPasswordMatching = false;
                } else {
                    if(passwdString.equals(passwdChkString)) {
                        signPwdDoubleCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0 ,R.drawable.check , 0);
                        isPasswordMatching = true;
                    } else {
                        signPwdDoubleCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0 ,R.drawable.error , 0);
                        isPasswordMatching = false;
                    }
                }

                checkSignUpButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            finish();
        } else if (v.getId() == R.id.idCheckButton) {
            checkUserId(signId.getText().toString().trim());
        } else if (v.getId() == R.id.nicknameCheckButton) {
            checkUserNickname(signName.getText().toString().trim());
        } else if (v.getId() == R.id.sendVerifyCodeButton) {
            sendVerifyCode(signEmail.getText().toString().trim());
        } else if (v.getId() == R.id.verifyCodeCheckButton) {
            checkVerifyCode(signEmail.getText().toString().trim(), verifyCodeTextInput.getText().toString().trim());
        } else if (v.getId() == R.id.btn_sign_up) {
            registerUser();
        }
    }

    private void checkSignUpButtonState() {
        btnSignUp.setEnabled(isIdChecked && isNicknameChecked && isPasswordMatching && isEmailVerified);
    }

    private void checkUserId(final String userId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("id", userId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url(CHECK_USER_ID_URL)
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    Log.i("아이디 찾기", responseBody);
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("사용 가능한 아이디입니다.");
                                isIdChecked = true;
                                checkSignUpButtonState();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("사용 불가능한 아이디입니다.");
                                isIdChecked = false;
                                checkSignUpButtonState();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUp.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void checkUserNickname(final String nickname) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                // 동적으로 URL을 생성하여 요청을 보냅니다.
                String url = CHECK_USER_NICKNAME_URL + "/" + nickname;

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    Log.i("닉네임 확인", responseBody);


                    if (response.isSuccessful()) {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("사용 가능한 닉네임입니다.");
                                isNicknameChecked = true;
                                checkSignUpButtonState();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("사용 불가능한 닉네임입니다.");
                                isNicknameChecked = false;
                                checkSignUpButtonState();
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUp.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    private void sendVerifyCode(final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("email", email);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url(SEND_VERIFY_CODE_URL)
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    Log.i("인증코드 전송", responseBody);
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("인증코드를 발송했습니다.");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("인증코드를 보낼 수 없습니다. 이메일을 확인하세요.");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUp.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void checkVerifyCode(final String email, final String code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("code", code);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url(CHECK_VERIFY_CODE_URL)
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("이메일이 인증되었습니다.");
                                isEmailVerified = true;
                                checkSignUpButtonState();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("인증코드가 일치하지 않습니다.");
                                isEmailVerified = false;
                                checkSignUpButtonState();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUp.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void registerUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("id", signId.getText().toString().trim());
                    json.put("nickname", signName.getText().toString().trim());
                    json.put("passwd", signPwd.getText().toString().trim());
                    json.put("email", signEmail.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url(REGISTER_URL)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    Log.i("회원가입 버튼", responseBody);
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUp.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, MainActivity.class );
                                startActivity(intent);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUp.this, "회원가입 실패: " + responseBody, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUp.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(SignUp.this)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
