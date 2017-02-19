package com.example.martyna.alarmReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.martyna.Services.LocationService;

/**
 * Klasa umożliwia przechwycenie obiektu typu Intent wysłanego z metody locationScheduleAlarm() w klasie MainActivity().
 * Broadcast nasłuchuje zdarzeń nawet kiedy aplikacja znajduje się w tle,
 * a ustawienie android:process=":remote" w pliku AndroidManifest.xml powoduje pracę Broadcastu w oddzielym wątku.
 * Metoda onReceive() gwarantuje, iż proces nie zostanie zniszczony dopóki wszystkie zadania
 * w ciele funkcji nie zostaną wykonane (a tym samym zadania zawarte w uruchamianych klasach typu IntentService)
 */
public class LocationAlarmReceiver extends BroadcastReceiver {

    /**
     * Zmienna okreslająca kod, który umożliwia identyfikację klasy LocationAlarmReceiver dla rozesłanego obiektu typy Intent z klasy MainActivity.
     */
    public static final int REQUEST_CODE = 121;

    /**
     * Metoda abstarkcyjna z klasy BroadcastReceiver, wywoływana jest kiedy klasa LocationAlarmReceiver otrzyma rozesłany obiekt typu Intent.
     * Metoda uruchamia klase locationService, która umożliwia pobiera danych na temat lokalizacji użytkownika.
     * @param context Kontekst, w którym pracuje aplikacja.
     * @param intent  Obiekt typu Intent, który został przechwycony przez BroadcasReceiver.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent location = new Intent(context, LocationService.class);
        context.startService(location);
    }
}
