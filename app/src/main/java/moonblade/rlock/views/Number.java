package moonblade.rlock.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

import moonblade.rlock.GlobalVariables;
import moonblade.rlock.R;
import moonblade.rlock.controllers.ApiCalls;
import moonblade.rlock.controllers.AsyncResponse;
import moonblade.rlock.models.User;

public class Number extends AppCompatActivity {
    FloatingActionButton fab;
    Toolbar toolbar;
    TextView passkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        linkElements();
        getPasskey();
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
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("id", User.findById(User.class, (long) 1).id);
            String url = GlobalVariables.serverUrl + "keys/viewcurrent";
            ApiCalls viewCurrent = new ApiCalls(this, params, url, new AsyncResponse() {
                @Override
                public void ProcessFinish(Object output) {

                }
            });
            viewCurrent.execute();
        }
        catch (NullPointerException e)
        {
            Snackbar.make(passkey,"Please log in",Snackbar.LENGTH_LONG).show();
        }

    }
}
