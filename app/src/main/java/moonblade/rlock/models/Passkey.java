package moonblade.rlock.models;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by moonblade on 14/11/15.
 */
public class Passkey extends SugarRecord<Passkey> {
    String key;
    Date date;
    String name;

    public Passkey() {
    }

    public Passkey(String key, Date date, String name) {
        this.key = key;
        this.date = date;
        this.name = name;
    }

    public Passkey(JSONObject passkey) throws JSONException {
        this.key = passkey.getString("key");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.date = format.parse(passkey.getString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.name = passkey.getString("name");
    }
}
