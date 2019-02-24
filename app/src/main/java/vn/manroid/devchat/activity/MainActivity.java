package vn.manroid.devchat.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;

import vn.manroid.devchat.BuildConfig;
import vn.manroid.devchat.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtFont, txtMarqueeText;
    private Button btnStartChat, btnShare, btnInfor, btnLogOut, btnChatRoom;
    private Intent intent;
    private AlertDialog dialog;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shareDialog = new ShareDialog(this);
        txtFont = (TextView) findViewById(R.id.txtFontMain);
        txtMarqueeText = (TextView) findViewById(R.id.MarqueeText);

        txtFont.setTypeface(Typeface.createFromAsset(getAssets(),
                "myfont.ttf"));
        txtMarqueeText.setSelected(true);


        btnStartChat = (Button) findViewById(R.id.btnStartChat);
        btnChatRoom = (Button) findViewById(R.id.btnChatRoom);
        btnInfor = (Button) findViewById(R.id.btnMore);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);

        btnStartChat.setOnClickListener(this);
        btnChatRoom.setOnClickListener(this);
        btnInfor.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartChat:

                intent = new Intent(MainActivity.this, ListUserActivity.class);
                startActivity(intent);
                break;
            case R.id.btnChatRoom:

                intent = new Intent(MainActivity.this, ChatRoomActivity.class);
                startActivity(intent);
                break;

            case R.id.btnMore:
                final String appPackageName = getPackageName();
                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=Manroid")));
                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Manroid")));
                }
//                try {
//                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
//                    String shareMessage= "\nLet me recommend you this application\n\n";
//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                    startActivity(Intent.createChooser(shareIntent, "choose one"));
//                } catch(Exception e) {
//                    //e.toString();
//                }
                break;

            case R.id.btnShare:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=vn.manroid.devchat"))
                            .build();

                    shareDialog.show(content);

                }
                break;

            case R.id.btnLogOut:
                try {
                    LogInActivity.sendOjectLoginSuccessfull(false);

                    LogInActivity.sendObjectFacebookSuccessfull(false);

                    LogInActivity.sendOjectGmailSuccessfull(false);

                    FirebaseAuth.getInstance().signOut();

                    //Logout facebook
                    LoginManager.getInstance().logOut();

                    intent = new Intent(v.getContext(), LogInActivity.class);
                    startActivity(intent);

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setIcon(getResources().getDrawable(R.drawable.icon));
            builder.setTitle("ChatDev");
            builder.setCancelable(false);
            builder.setMessage("Bạn muốn thoát khỏi ứng dụng ???");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
