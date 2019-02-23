package vn.manroid.devchat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import vn.manroid.devchat.R;
import vn.manroid.devchat.model.UserModel;

public class LogInActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Button btnSignIn, btnSignUp;
    private EditText edtAcc, edtPass;
    private SignInButton btnGmail;
    private static Uri uri;
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    private static FirebaseAuth mAuthen;
    private static final int RC_SIGN_IN = 38;
    private GoogleApiClient mGoogleApiClient;
    private TextView txtFont;
    private ProgressDialog dialog;
    private static FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.setApplicationId("479285745752653");
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_log_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initView();
        txtFont.setTypeface(Typeface.createFromAsset(getAssets(),
                "myfont.ttf"));

        if (!checkNetwork()) {
            Toast.makeText(this, "Vui lòng kiểm tra lại internet", Toast.LENGTH_SHORT).show();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnLoginFacebook.setReadPermissions("email", "public_profile");

        btnLoginFacebook.setOnClickListener(this);
        btnGmail.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        //Đối tượng này hỗ trợ cho việc khởi tọa login trên face
        callbackManager = CallbackManager.Factory.create();
        mAuthen = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("857974585347-p56flsrdjuh5clr16u6sor7thkqbv82l.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LogInActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Lắng nghe trạng thái login
        mAuthen.addAuthStateListener(onAuthenStated);

        firebaseDatabase = FirebaseDatabase.getInstance();


    }

    private void initView() {
        btnLoginFacebook = findViewById(R.id.btnFacebook);
        btnGmail = findViewById(R.id.btnGmail);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        edtAcc = findViewById(R.id.edtAcc);
        edtPass = findViewById(R.id.edtPass);

        txtFont = findViewById(R.id.txtFontLogIn);
    }

    private OnCompleteListener onRegister_Complete = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            try {
                dialog.dismiss();
                if (task.isComplete() && task.isSuccessful()) {
                    sendOjectLoginSuccessfull(true);
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Tài khoản đã tồn tại hoặc không hợp lệ !!!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private FirebaseAuth.AuthStateListener onAuthenStated =
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() != null) {
                        //Tạo user vào firebase thành công
                        Intent main = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }

                }
            };
    private OnCompleteListener onLogin_Complete = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            try {
                FirebaseUser user = mAuthen.getCurrentUser();
                if (user != null) {
                    sendOjectLoginSuccessfull(true);
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Thông tin không chính xác hoặc tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static void sendOjectLoginSuccessfull(Boolean status) {
        try {
            DatabaseReference reference =
                    firebaseDatabase.getReference("ListUser");

            UserModel user = new UserModel();
            FirebaseUser us = mAuthen.getCurrentUser();

            //user.setPhotoURL(us.getPhotoUrl());

            user.setPhotoURL("https://www.drupal.org/files/styles/drupalorg_user_picture/public/default-avatar.png?itok=qMUyWcaa");
            user.setUserID(us.getUid());
            user.setName("Người lạ");
            user.setEmail(us.getEmail());
            user.setOnline(status);

            Gson gs = new Gson();
            reference.child(us.getUid()).setValue(gs.toJson(user));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendOjectGmailSuccessfull(Boolean status) {
        try {
            DatabaseReference reference =
                    firebaseDatabase.getReference("ListUser");

            UserModel user = new UserModel();
            FirebaseUser us = mAuthen.getCurrentUser();

            //user.setPhotoURL(us.getPhotoUrl());

            user.setPhotoURL(String.valueOf(us.getPhotoUrl()));
            user.setUserID(us.getUid());
            user.setName(us.getDisplayName());
            user.setEmail(us.getEmail());
            user.setOnline(status);

            Gson gs = new Gson();
            reference.child(us.getUid()).setValue(gs.toJson(user));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendObjectFacebookSuccessfull(Boolean status) {
        DatabaseReference reference =
                firebaseDatabase.getReference("ListUser");

        UserModel user = new UserModel();
        FirebaseUser us = mAuthen.getCurrentUser();

        // user.setPhotoURL(us.getPhotoUrl());
        if (us != null) {
            String stringUri;
            uri = us.getPhotoUrl();

            if (uri != null) {
                stringUri = uri.toString();

                user.setPhotoURL(stringUri);
                user.setUserID(us.getUid());
                user.setName(us.getDisplayName());
                user.setEmail(us.getEmail());
                user.setOnline(status);

                Gson gs = new Gson();
                reference.child(us.getUid()).setValue(gs.toJson(user));
            }

        }
    }

    private FacebookCallback<LoginResult> login_CallBack
            = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //Login vào face thành công
            //Như tạo user lên firebase
            AuthCredential credential =
                    FacebookAuthProvider.getCredential(
                            loginResult.getAccessToken().getToken());


            //Tạo User
            mAuthen.signInWithCredential(credential)
                    .addOnCompleteListener(LogInActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    try {
                                        if (task.isSuccessful()) {
                                            //Tạo user vào firebase thành công
                                            sendObjectFacebookSuccessfull(true);
                                            Intent main = new Intent(LogInActivity.this, MainActivity.class);
                                            startActivity(main);

                                            finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


        }

        @Override
        public void onCancel() {
            //Hủy việc login
            Log.d("onCancel", "onCancel() ============= ");
        }

        @Override
        public void onError(FacebookException error) {
            //Login có lỗi xảy ra
            Log.d("onError", error.getMessage() + " ============");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Toast.makeText(LogInActivity.this, "" + credential.getProvider(), Toast.LENGTH_LONG).show();
        mAuthen.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        sendOjectGmailSuccessfull(true);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFacebook:
                btnLoginFacebook.registerCallback(callbackManager,
                        login_CallBack);
                break;

            case R.id.btnSignIn:
                try {
                    if (edtAcc.getText().toString().trim().length() == 0
                            && edtPass.getText().toString().length() == 0) {
                        Toast.makeText(this, "Yêu cầu nhập thông tin !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        new LoginTask().execute(edtAcc.getText().toString().trim()
                                , edtPass.getText().toString());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnGmail:
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
                break;

            case R.id.btnSignUp:
                try {
                    if (edtAcc.getText().toString().trim().length() == 0
                            && edtPass.getText().toString().length() == 0
                            && edtPass.getText().toString().length() < 6) {
                        Toast.makeText(this, "Yêu cầu nhập thông tin !!!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Mật khẩu phải 6 ký tự trở lên", Toast.LENGTH_LONG).show();

                    } else {
                        new RegisterTask().
                                execute(edtAcc.getText().toString().trim()
                                        , edtPass.getText().toString());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", connectionResult.getErrorMessage());
    }

    class RegisterTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LogInActivity.this);
            dialog.setMessage("Hệ thống đang xử lý ...!!!");
            dialog.setCancelable(false);
            dialog.setTitle("Quản lý tài khoản");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mAuthen.createUserWithEmailAndPassword(params[0], params[1])
                    .addOnCompleteListener(onRegister_Complete);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }

    class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LogInActivity.this);
            dialog.setMessage("Hệ thống đang xử lý ...!!!");
            dialog.setCancelable(false);
            dialog.setTitle("Quản lý tài khoản");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mAuthen.signInWithEmailAndPassword(params[0], params[1])
                    .addOnCompleteListener(onLogin_Complete);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }

    private boolean checkNetwork() {
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null &&
                info.isAvailable() &&
                info.isConnected()) {
            //có tín hiệu mạng
            available = true;
        }
        return available;
    }
}

