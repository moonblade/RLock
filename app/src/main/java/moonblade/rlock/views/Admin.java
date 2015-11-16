package moonblade.rlock.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import moonblade.rlock.GlobalVariables;
import moonblade.rlock.R;
import moonblade.rlock.controllers.ApiCalls;
import moonblade.rlock.controllers.AsyncResponse;
import moonblade.rlock.models.User;

public class Admin extends AppCompatActivity {
    ListView userList;
    ArrayList<User> userListArray = new ArrayList<>();
    ArrayList<String> userNameList = new ArrayList<>();
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linkElements();
        getUserList();
        setListeners();
    }

    private void setListeners() {
        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final User user = userListArray.get(i);
                final Map<String, Object> params = new LinkedHashMap<>();
                params.put("id", user.uid);
                final String url = GlobalVariables.serverUrl + "users/changelevel/" + GlobalVariables.user.uid;
                builder.setTitle("Change Privilege");
                builder.setNegativeButton("Demote", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        params.put("level", user.level - 1);
                        callApi(params, url);
                    }
                }).setPositiveButton("Promote", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        params.put("level", user.level + 1);
                        callApi(params, url);
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    private void callApi(Map<String, Object> params, String url) {
        ApiCalls changeLevel = new ApiCalls(getApplicationContext(), params, url, new AsyncResponse() {
            @Override
            public void ProcessFinish(Object output) {
                try {
                    JSONObject serverResponse = new JSONObject(output.toString());
                    Snackbar.make(userList, serverResponse.getString("message"), Snackbar.LENGTH_LONG).show();
                    getUserList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        changeLevel.execute();
    }

    private void linkElements() {
        userList = (ListView) findViewById(R.id.userList);
        builder = new AlertDialog.Builder(this);

    }

    private void getUserList() {
        String url = GlobalVariables.serverUrl + "users/viewusers/" + GlobalVariables.user.uid;
        Map<String, Object> params = new LinkedHashMap<>();
        ApiCalls getUsers = new ApiCalls(getApplicationContext(), params, url, new AsyncResponse() {
            @Override
            public void ProcessFinish(Object output) {
                try {
                    userListArray.clear();
                    userNameList.clear();
                    JSONObject serverResponse = new JSONObject(output.toString());
                    if (serverResponse.getInt("status") == 1) {
                        JSONArray userListJson = new JSONArray(serverResponse.getString("message"));
                        for (int i = 0; i < userListJson.length(); i++) {
                            User user = new User(userListJson.getJSONObject(i));
                            userListArray.add(user);
                            if (user.level > 1) {
                                userNameList.add("A: " + user.name);
                            } else if (user.level > 0) {
                                userNameList.add("V: " + user.name);
                            } else {
                                userNameList.add("P: " + user.name);
                            }
                        }
                        userList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userNameList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text = (TextView) view.findViewById(android.R.id.text1);
                                text.setTextColor(Color.BLACK);
                                return view;
                            }
                        });
                    } else {
                        Snackbar.make(userList, serverResponse.getString("message"), Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getUsers.execute();
    }

}
