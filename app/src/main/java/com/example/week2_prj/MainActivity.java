package com.example.week2_prj;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


//Hash Key
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.week2_prj.databinding.ActivityMainBinding;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    private View loginBtn, logoutBtn;
    private TextView nickName;
    private ImageView profileImg;

   // ImageButton kakao_login_button = (ImageButton) findViewById(R.id.kakao_login_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginBtn = findViewById(R.id.kakao_login_button);
        logoutBtn = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        profileImg = findViewById(R.id.profile);

        //getHashKey();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>(){
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable){
                if (oAuthToken != null){
                    // 로그인 성공 시
                }
                if (throwable != null){
                    // 로그인 성공 시
                }
                updateKakaoLoginUi();
                return null;
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){ // 이미 카카오톡 앱이 있는 경우
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
                }
                else
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
            }
        });


        updateKakaoLoginUi();

    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) { // user가 있을때

                    Log.i(TAG, "invoke: id=" + user.getId());
                    System.out.println(user.getId());
                    System.out.println(" 여기~~~~~~~~~");
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());

                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());
                    Glide.with(profileImg).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImg);


                    loginBtn.setVisibility(View.GONE);
                    logoutBtn.setVisibility(View.VISIBLE);
                } else { // logout
                    nickName.setText(null);
                    profileImg.setImageBitmap(null);
                    loginBtn.setVisibility(View.VISIBLE);
                    logoutBtn.setVisibility(View.GONE);
                }
                return null;
            }
        });
    }
}
//        kakao_login_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
//                    login();
//                }
//                else
//                    accountLogin();
//            }
//        });


//    public void login(){
//        String TAG = "login()";
//        UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this,(oAuthToken, error) -> {
//            if (error != null) {
//                Log.e(TAG, "로그인 실패", error);
//            } else if (oAuthToken != null) {
//                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
//                getUserInfo();
//            }
//            return null;
//        });
//    };


//    public void accountLogin(){
//        String TAG = "accountLogin()";
//        UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,(oAuthToken, error) -> {
//            if (error != null) {
//                Log.e(TAG, "로그인 실패", error);
//            } else if (oAuthToken != null) {
//                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
//                getUserInfo();
//            }
//            return null;
//        });
//    }
//
//    public void getUserInfo(){
//        String TAG = "getUserInfo()";
//        UserApiClient.getInstance().me((user, meError) -> {
//            if (meError != null) {
//                Log.e(TAG, "사용자 정보 요청 실패", meError);
//            } else {
//                System.out.println("로그인 완료");
//                Log.i(TAG, user.toString());
//                {
//                    Log.i(TAG, "사용자 정보 요청 성공" +
//                            "\n회원번호: "+user.getId() +
//                            "\n이메일: "+user.getKakaoAccount().getEmail());
//                }
//                Account user1 = user.getKakaoAccount();
//                System.out.println("사용자 계정" + user1);
//            }
//            return null;
//        });
//    }
//
//
//    private void getHashKey() {
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (packageInfo == null)
//            Log.e("KeyHash", "KeyHash:null");
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));	// 해시키를 로그로 찍어서 확인
//            } catch (NoSuchAlgorithmException e) {
//                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//    }
