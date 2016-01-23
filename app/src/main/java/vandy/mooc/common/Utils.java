package vandy.mooc.common;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    private Utils() {
        throw new AssertionError();
    }

    public static void showToast(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }
}
