package com.example.martyna.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class IsNetworkConnection {

    /**
     * Metoda sprawdza czy jest nawiązane połączenie internetowe.
     * @param context Kontekst, w którym pracuje aplikacja
     * @return Metoda zwraca wartość logiczną true - jeżeli jest połączenie internetowe oraz false - jeżeli nie ma.
     */
    public static boolean isNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        //in airplane mode it will be null
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else{
            Toast.makeText(context, "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
