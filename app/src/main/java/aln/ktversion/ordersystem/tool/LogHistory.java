package aln.ktversion.ordersystem.tool;

import android.util.Log;

public class LogHistory {
    private static final Boolean logFlag = true;
    public static void d(String tag,String message) {
        Log.d(tag,message);
    }
}
