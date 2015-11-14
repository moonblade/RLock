package moonblade.rlock.models;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moonblade on 14/11/15.
 */
public class User extends SugarRecord<User> {
    public String id;
    public String name;
    private int level;

    public User(){}

    public User(String id, String name, int level)
    {
        this.id=id;
        this.name=name;
        this.level=level;
    }

    public User(JSONObject user) throws JSONException {
        this.id = user.getString("id");
        this.name = user.getString("name");
        this.level = user.getInt("level");
    }

    public User(GoogleSignInAccount acct)
    {
        this.id=acct.getId();
        this.name=acct.getDisplayName();
        this.level=0;
    }
}
