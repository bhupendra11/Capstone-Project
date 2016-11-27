package quickjournal.bhupendrashekhawat.me.android.quickjournal.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.MainActivity;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.R;

public class AuthActivity extends AppCompatActivity {

    //for authentication via firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 9001;
    private CoordinatorLayout coordinatorLayout;
    public static final String USER_EMAIL="user_email";
    public static final String USER_DISPLAY_NAME ="user_display_name";
    public static final String USER_PROFILE_PIC_URL ="user_profile_pic_url";
    public static final String LOG_TAG= AuthActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_auth);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // already signed in
            String userMail  = mAuth.getCurrentUser().getEmail();
            //Log.d(LOG_TAG, "Usermail is "+userMail);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER_DISPLAY_NAME, mAuth.getCurrentUser().getDisplayName());
            intent.putExtra(USER_EMAIL, userMail);
            if(mAuth.getCurrentUser().getPhotoUrl() != null)
                    intent.putExtra(USER_PROFILE_PIC_URL, mAuth.getCurrentUser().getPhotoUrl().toString());


            startActivity(intent);
            finish();

        } else {
            // not signed in
            login();
        }

    }

    public void login(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.AppTheme)
                        .setLogo(R.drawable.ic_app)
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            String userMail  = mAuth.getCurrentUser().getEmail();
            //Log.d(LOG_TAG, "Usermail is "+userMail);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER_DISPLAY_NAME, mAuth.getCurrentUser().getDisplayName());
            intent.putExtra(USER_EMAIL, userMail);
            if(mAuth.getCurrentUser().getPhotoUrl() != null)
                intent.putExtra(USER_PROFILE_PIC_URL, mAuth.getCurrentUser().getPhotoUrl().toString());

            startActivity(intent);
            finish();
            return;
        }

        // Sign in canceled
        if (resultCode == RESULT_CANCELED) {
            showSnackbar(R.string.sign_in_cancelled);
            return;
        }

        // No network
        if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            showSnackbar(R.string.no_internet_connection);
            return;
        }

        // User is not signed in. Maybe just wait for the user to press
        // "sign in" again, or show a message.
    }

    public void showSnackbar(int res_id){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getString(res_id), Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
