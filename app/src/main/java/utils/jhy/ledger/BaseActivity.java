package utils.jhy.ledger;

import android.app.Activity;
import android.content.Context;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import utils.jhy.ledger.data.MainData;

public class BaseActivity extends Activity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(TypekitContextWrapper.wrap(base));
    }

    public void callbackGetData (Activity act, ArrayList<MainData> data) {
        if(act instanceof ListActivity)
            ((ListActivity)act).dataUpdate(data);
    }
}
