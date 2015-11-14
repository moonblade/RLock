package moonblade.rlock.views;

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
import android.widget.TextView;

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

public class Number extends AppCompatActivity {
    FloatingActionButton fab;
    Toolbar toolbar;
    TextView passkey;
    private Map<String, Object> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        linkElements();
        setCurrentKey();
        getPasskey();
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passkey = (TextView) findViewById(R.id.passkey);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
}
