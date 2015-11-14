package moonblade.rlock.views;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import moonblade.rlock.GlobalVariables;
import moonblade.rlock.R;
import moonblade.rlock.controllers.ApiCalls;
import moonblade.rlock.controllers.AsyncResponse;
import moonblade.rlock.models.Passkey;
import moonblade.rlock.models.User;

public class Number extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    FloatingActionButton fab;
    SignInButton signIn;
    Toolbar toolbar;
    TextView passkey,name;
    private Map<String, Object> params;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        linkElements();
        setListeners();
        setCurrentKey();
        getPasskey();
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void setCurrentKey() {
        Iterator<Passkey> keys = Passkey.findAll(Passkey.class);
        while (keys.hasNext()) {
            Passkey key = keys.next();
            if (!keys.hasNext())
                passkey.setText(key.key);
        }
    }

    private void linkElements() {
        name = (TextView) findViewById(R.id.name);
        signIn = (SignInButton) findViewById(R.id.sign_in_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passkey = (TextView) findViewById(R.id.passkey);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            User user = new User(acct);
            userLogin(user);
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void userLogin(User user) {
        Map<String,Object> params =new LinkedHashMap<>();
        params.put("id",user.id);
        params.put("name",user.name);
        String url = GlobalVariables.serverUrl + "users/login";
        ApiCalls userlogin = new ApiCalls(getApplicationContext(), params, url, new AsyncResponse() {
            @Override
            public void ProcessFinish(Object output) {
                try{
                    JSONObject serverResponse = new JSONObject(output.toString());
                    if (serverResponse.getInt("status") == 1) {
                        User user = new User(new JSONObject(serverResponse.getString("message")));
                        try {
                            User.deleteAll(User.class);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (SQLiteException e) {
                            e.printStackTrace();
                        }
                        try {
                            user.save();
                            name.setText(user.name);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (SQLiteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Snackbar.make(name, serverResponse.getString("message"), Snackbar.LENGTH_LONG).show();
                        name.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUI(boolean b) {
        if(b)
        {
            signIn.setVisibility(View.GONE);
        }
        else
        {
            signIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_number, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPasskey() {
        try {
            final Map<String, Object> params = getParams();
            String url = GlobalVariables.serverUrl + "keys/viewcurrent";
            ApiCalls viewCurrent = new ApiCalls(this, params, url, new AsyncResponse() {
                @Override
                public void ProcessFinish(Object output) {
                    try {
                        JSONObject serverResponse = new JSONObject(output.toString());
                        if (serverResponse.getInt("status") == 1) {
                            Passkey key = new Passkey(new JSONObject(serverResponse.getString("message")));
                            try {
                                Passkey.deleteAll(Passkey.class);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (SQLiteException e) {
                                e.printStackTrace();
                            }
                            try {
                                key.save();
                                passkey.setText(key.key);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (SQLiteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Snackbar.make(passkey, serverResponse.getString("message"), Snackbar.LENGTH_LONG).show();
                            passkey.setText("XXXX");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            viewCurrent.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Snackbar.make(passkey, "Please log in", Snackbar.LENGTH_LONG).show();
            passkey.setText("XXXX");
        }

    }

    public Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        Iterator<User> users = User.findAll(User.class);
        while (users.hasNext()) {
            User user = users.next();
            if (!users.hasNext())
                params.put("id",user.id);
        }
        return params;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Snackbar.make(passkey, "Please check network Connection", Snackbar.LENGTH_LONG).show();
    }
}
