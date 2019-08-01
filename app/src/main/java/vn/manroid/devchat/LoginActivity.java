package vn.manroid.devchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findId();
        initView();
        initEvent();
        setupData();

    }

    @Override
    public void findId() {
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void setupData() {

    }


    @Override
    public void onClick(View v) {
        startActivity(RegisterActivity.class);
    }
}
