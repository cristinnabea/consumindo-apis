package com.br.consumindoapis.ui.fragment;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.br.consumindoapis.R;
import com.br.consumindoapis.ui.activity.MainActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.concurrent.Executor;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GoogleFragment extends Fragment {
    private View view;
    private GoogleSignInClient mGoogleSignInClient;
    private Context context;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;
    private TextView txtEmail;
    private TextView txtNome;
    private ImageView imgProfile;
    private CardView cardView;

    //4F:45:9C:BE:88:E4:D3:6B:A2:8F:FC:58:56:E8:E1:B8:85:06:C1:3F chave SHAI

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_google, container, false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        context = view.getContext();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);


        imgProfile = view.findViewById(R.id.imgProfile);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtNome = view.findViewById(R.id.txtNome);
        cardView = view.findViewById(R.id.card_view);

        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        cardView.setVisibility(View.GONE);

        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
        view.findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        return view;
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String displayName = account.getDisplayName();
            String familyName = account.getFamilyName();
            Uri photoUrl = account.getPhotoUrl();
            Account eita = account.getAccount();
            signInButton.setVisibility(View.GONE);
            txtEmail.setText(email);
            txtNome.setText(displayName);

            if (photoUrl != null) {
                Picasso.with(context).setLoggingEnabled(true);
                Picasso.with(context).load(photoUrl.toString()).into(imgProfile);
            }
//                imgProfile.loadUrl(photoUrl.toString());


            cardView.setVisibility(View.VISIBLE);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Eita", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Você se desconectou da conta", Toast.LENGTH_LONG).show();
                        Log.e("Eita", "desconectou");
                        cardView.setVisibility(View.GONE);
                        signInButton.setVisibility(View.VISIBLE);
                    }
                });
    }


}
