package com.example.martyna.alarmReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.martyna.Services.ProcessService;

/**
 * Klasa umożliwia przechwytywanie obiektu typu Intent zadeklarowanego w metodzie processScheduleAlarm() w klasie MainActivity().
 * Broadcast nasłuchuje zdarzeń nawet kiedy aplikacja znajduje się w tle,
 * a ustawienie android:process=":remote" w pliku AndroidManifest.xml powoduje pracę Broadcastu w oddzielym wątku.
 * Metoda onReceive() gwarantuje, iż proces nie zostanie zniszczony dopóki wszystkie zadania
 * w ciele funkcji nie zostaną wykonane (a tym samym zadania zawarte w uruchamianych klasach typu IntentService)
 */
public class ProcessAlarmReceiver extends BroadcastReceiver {

    /**
     * Zmienna okreslająca kod, który umożliwia identyfikację klasy ProcessAlarmReceiver dla rozesłanego obiektu typy Intent z klasy MainActivity.
     */
    public static final int REQUEST_CODE = 111;

    /**
     * Metoda abstarkcyjna z klasy BroadcastReceiver, wywoływana jest kiedy klasa ProcessAlarmReceiver otrzyma rozesłany obiekt typu Intent.
     * Metoda uruchamia klase ProcessService, która umożliwia pobieranie danych na temat uzywanych przeż użytkownika aplikacji.
     * @param context Kontekst, w którym pracuje aplikacja.
     * @param intent  Obiekt typu Intent, który został przechwycony przez BroadcasReceiver.
     */
    @Override
    public void onReceive(Context context, Intent intent){
        Intent process = new Intent(context, ProcessService.class);
        context.startService(process);
    }
}
