package tech.gori.firebasetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected void onResume() {
    super.onResume();

    // Firebaseの初期化
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    // SharedPreferencesの初期化
    SharedPreferences sp = getSharedPreferences("Firebase", Context.MODE_PRIVATE);
    // SharedPreferencesからuserKeyを取得
    String userKey = sp.getString("userKey", null);

    DatabaseReference users = null;
    // userKeyが無ければ、新規登録
    if (userKey == null) {
      // pushすると、AutoIdが作られる
      users = ref.push();
      // SharedPreferencesにuser_keyを登録
      SharedPreferences.Editor editor = sp.edit();
      editor.putString("userKey", users.getKey());
      editor.apply();
    } else {
      // users以下のuserKeyで、referenceを作成
      users = ref.child(userKey);
    }

    HashMap<String, Object> userData = new HashMap<>();
    Calendar cal = Calendar.getInstance();
    userData.put("login_time", String.format("%1$tY/%1$tm/%1$td %1$tT", cal));

    // users/userKey以下のuserDataを更新
    users.updateChildren(userData);
  }
}
