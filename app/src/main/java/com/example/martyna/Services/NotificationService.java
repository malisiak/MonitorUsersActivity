package com.example.martyna.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.martyna.Utils.R;

/**
 * Klasa NotificationService zawiera metodę odpowiedzialna za stworzenie powiadomienia, wyświetonego na zewnątrz aplikacji.
 * NotificationService rozszerza klasę IntentService, która wykonuje pracę w wątku roboczym oraz
 * umożliwia przeprowadzenie operacji w tle. Wymaga nadpisania metody onHandleIntent.
 */
public class NotificationService extends IntentService {
    public NotificationService() {
        super("NotificationService");
    }

    /**
     * Metoda abstrakcyjna klasy IntentService. Buduje powiadomienie wyswietlone na zewnątrz aplikacji.
     * @param intent  Obiekt przekazany podczas uruchomienia IntentService w onReceive() w NotificationAlarmReceiver.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText( "Analiza danych dostepna na Twoim profilu na stronie internetowej!" );
        builder.setSmallIcon( R.mipmap.ic_launcher );
        builder.setContentTitle( getString( R.string.app_name ) );
        NotificationManagerCompat.from(this).notify(0, builder.build());
    }
}
