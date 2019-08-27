package utils.jhy.ledger;

import android.app.Activity;
import android.content.Context;

import com.tsengvn.typekit.TypekitContextWrapper;

public class BaseActivity extends Activity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(TypekitContextWrapper.wrap(base));
    }

}
