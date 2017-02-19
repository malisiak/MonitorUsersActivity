package com.example.martyna.alarmReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.martyna.Services.ConnectionService;
import com.example.martyna.enums.ConnectionTask;
import com.example.martyna.Utils.BuildJSON;
import com.example.martyna.Utils.IsNetworkConnection;

/**
 * Klasa umożliwia przechwycenie obiektu typu Intent wysłanego z metody networkScheduleAlarm() w klasie MainActivity()
 * oraz przez system Android, gdy zostanie wykryte połączenie z Internetem.
 * Broadcast nasłuchuje zdarzeń nawet kiedy aplikacja znajduje się w tle,
 * ustawienie android:process=":remote" w pliku AndroidManifest.xml powoduje pracę Broadcastu w oddzielym wątku.
 * Metoda onReceive() gwarantuje, iż proces nie zostanie zniszczony dopóki wszystkie zadania
 * w ciele funkcji nie zostaną wykonane (a tym samym zadania zawarte w uruchamianych klasach typu IntentService)
 */
public class DetectNetworkConnection extends BroadcastReceiver {

    /**
     * Zmienna okreslająca kod, który umożliwia identyfikację klasy DetectNetworkConnection dla rozesłanego obiektu typy Intent z klasy MainActivity.
     */
    public static final int REQUEST_CODE = 151;

    /**
     * Metoda abstarkcyjna z klasy BroadcastReceiver, wywoływana jest kiedy klasa DetectNetworkConnection otrzyma rozesłany obiekt typu Intent.
     * Metoda uruchamia klase ConnectionService, która nawiązuje połączenie z serweram aplikacji.
     * Ponadto, w utworzonym w metodzie obiekcie typu Intent, przekazana jest dodatkowa informacja, określająca typ wysyłanych danych -
     * zmienna typu enum Data wskazuje na wysłanie informacji dotyczących pobieranych przez aplikację danych.
     *
     * @param context Kontekst, w którym pracuje aplikacja.
     * @param intent  Obiekt typu Intent, który został przechwycony przez BroadcasReceiver.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (IsNetworkConnection.isNetworkConnection(context) && !BuildJSON.isJsonArrayEmpty()) {

            Toast.makeText(context, "polaczenie", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, ConnectionService.class);
            i.putExtra("task", ConnectionTask.Data.name());
            context.startService(i);
        }

    }
}

