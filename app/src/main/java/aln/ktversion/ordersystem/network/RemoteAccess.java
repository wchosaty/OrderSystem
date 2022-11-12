package aln.ktversion.ordersystem.network;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RemoteAccess {
    private static final String TAG = "TAG RemoteAccess";
    // localhost url
    public static final String URL = "http://10.0.2.2:8080/OrderSystemWeb/";

    public static String accessProduct(String url,String outString){
        ConnectString connectString = new ConnectString(url,outString);
        FutureTask<String> task = new FutureTask<>(connectString);
        Thread thread = new Thread(task);
        thread.start();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            task.cancel(true);
            return "";
        }

    }


}
