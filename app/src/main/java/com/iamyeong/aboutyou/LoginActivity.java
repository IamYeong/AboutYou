package com.iamyeong.aboutyou;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.talk.TalkApiClient;
import com.kakao.sdk.talk.model.Friend;
import com.kakao.sdk.talk.model.Friends;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;
import com.kakao.sdk.user.model.User;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    private Button kakaoLoginButton, appLoginButton;
    private CheckBox autoLoginCheck;
    private boolean isAutoLogin = false;

    private Function2<OAuthToken, Throwable, Unit> function2 = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {

            if (oAuthToken != null) {

            } else {

            }



            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        kakaoLoginButton = findViewById(R.id.btn_kakao_login);
        appLoginButton = findViewById(R.id.btn_app_login);
        autoLoginCheck = findViewById(R.id.chk_box_kakao_auto_login);

        //파이어스토어에 자동로그인 여부 저장할 것
        kakaoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {

                } else {

                }

            }
        });


    }

    private void getMe() {

        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                if (user != null) {

                    Account account = user.getKakaoAccount();
                    Profile profile = account.getProfile();

                    long kakaoId = user.getId();
                    String knickName = profile.getNickname();
                    String thumbnail = profile.getThumbnailImageUrl();
                    if (account.isEmailValid()) {
                        String email = account.getEmail();
                    }

                    String year = account.getBirthyear();
                    String monthDay = account.getBirthday();
                    TalkApiClient.getInstance().friends(new Function2<Friends<Friend>, Throwable, Unit>() {
                        @Override
                        public Unit invoke(Friends<Friend> friendFriends, Throwable throwable) {

                            List<Friend> friends = friendFriends.getElements();

                            for (Friend friend : friends) {
                                friend.getUuid();
                                friend.getId();
                                friend.getProfileNickname();
                                friend.getProfileThumbnailImage();
                            }

                            return null;
                        }
                    });

                }

                return null;
            }
        });

    }

}