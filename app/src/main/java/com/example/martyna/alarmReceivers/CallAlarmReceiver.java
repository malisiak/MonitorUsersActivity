package com.example.martyna.alarmReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.martyna.Services.CallLogService;

/**
 * Klasa umożliwia przechwytywanie obiektu typu Intent zadeklarowanego w metodzie callScheduleAlarm() w klasie MainActivity().
 * Broadcast nasłuchuje zdarzeń nawet kiedy aplikacja znajduje się w tle,
 * a ustawienie android:process=":remote" w pliku AndroidManifest.xml powoduje pracę Broadcastu w oddzielym wątku.
 * Metoda onReceive() gwarantuje, iż proces nie zostanie zniszczony dopóki wszystkie zadania
 * w ciele funkcji nie zostaną wykonane (a tym samym zadania zawarte w uruchamianych klasach typu IntentService)
 */
public class CallAlarmReceiver extends BroadcastReceiver {
    /**
     * Zmienna okreslająca kod, który umożliwia identyfikację klasy CallAlarmReceiver dla rozesłanego obiektu typy Intent z klasy MainActivity.
     */
    public static final int REQUEST_CODE = 131;

    /**
     * Metoda abstarkcyjna z klasy BroadcastReceiver, wywoływana kiedy klasa CallAlarmReceiver otrzyma rozesłany obiekt typu Intent.
     * Metoda uruchamia klasę CallLogService, która umożliwia pobranie dnaych na temat połaczeń.
     * @param context Kontekst, w którym pracuje aplikacja.
     * @param intent Obiekt typu Intent, który został przechwycony przez BroadcasReceiver.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent call = new Intent(context, CallLogService.class);
        context.startService(call);
    }

}
