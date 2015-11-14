package moonblade.rlock;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moonblade on 14/11/15.
 */
public class User {
    public String name;
    public String email;
    public String pass;
    private int level,id;

    public User(int id, String name, String email, String pass, int level)
    {
        this.id=id;
        this.name=name;
        this.email=email;
        this.pass=pass;
        this.level=level;
    }

    public User(JSONObject user) throws JSONException {
        this.id = user.getInt("id");
        this.name = user.getString("name");
        this.email = user.getString("email");
        this.pass = user.getString("pass");
        this.level = user.getInt("level");
    }
}