package com.example.martyna.Services;

import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.martyna.enums.ProcessType;
import com.example.martyna.Utils.BuildJSON;
import com.example.martyna.Activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Klasa ProcessService zawiera metody odpowiedzialne za pobieranie danych dotyczących aplikacji używanych przez użytkownika.
 * processService rozszerza klasę IntentService, która wykonuje pracę w wątku roboczym oraz
 * umożliwia przeprowadzenie operacji w tle. Wymaga nadpisania metody onHandleIntent.
 */
public class ProcessService extends IntentService {

    // holds previous totalForegroundTime for each process
    static long facebookTime = 0L;
    static long musicTime = 0L;
    static long messengerTime = 0L;
    static long cameraTime = 0L;
    static long websiteTime = 0L;

    public ProcessService() {
        super("ProcessService");
    }

    /**
     * Metoda abstrakcyjna klasy IntentService.
     * Metoda pobiera informacje na temat używanych przez użytkownika aplikacji. W tym celu dane na temat procesów
     * zapisywane są do listy typu UsageStats (dane są pobrane z określonego odcinka czasu). Następnie lista jest iterowana i wyciagane są informacje na temat działania
     * aplikacji Facebook, Messenger, odtwarzacza muzyki, aparatu oraz przeglądarki internetowej. W kolejnym kroku dane są zapisywane do
     * obiektu processJsonArray za pomocą  metody processJSONArray(). Przypisywana jest nowa wartość czasu MainActivity.startTimeProcess.
     * @param intent Obiekt przekazany podczas uruchomienia IntentService w onReceive() w ProcessAlarmReceiver.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        SimpleDateFormat s = new SimpleDateFormat(" yyyy:mm:dd hh:mm:ss");
       // Log.i("TAG", "process czas1: " + s.format(new Date(MainActivity.startTimeProcess)));

        List<UsageStats> usmList = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST, MainActivity.startTimeProcess, System.currentTimeMillis());

        Log.i("Pobieranie danych ", "ProcessService - iteracja po liscie procesów");
        //  current process list
        for (UsageStats us : usmList) {
            if (us.getPackageName().contains("facebook") || us.getPackageName().contains("camera") ||
                    us.getPackageName().contains("music") || us.getPackageName().contains("mozilla") ||
                    us.getPackageName().contains("chrome")) {


                if (us.getLastTimeUsed() > MainActivity.startTimeProcess) {

                    String proccessName = null;
                    long suma = 0L;


                    //facebook
                    if (us.getPackageName().contains("facebook.katana")) {
                        proccessName = ProcessType.Facebook.name();
                        suma = makeTimeInForegorund(us.getTotalTimeInForeground(), facebookTime);
                        facebookTime = us.getTotalTimeInForeground();


                    }
                    // massenger
                    else if (us.getPackageName().contains("facebook.orca")) {
                        proccessName = ProcessType.Messenger.name();
                        suma = makeTimeInForegorund(us.getTotalTimeInForeground(), messengerTime);
                        messengerTime = us.getTotalTimeInForeground();
                    }
                    // musicPlayer
                    else if (us.getPackageName().contains("music")) {
                        proccessName = ProcessType.MusicPlayer.name();
                        suma = makeTimeInForegorund(us.getTotalTimeInForeground(), musicTime);
                        musicTime = us.getTotalTimeInForeground();
                    }
                    // camera
                    else if (us.getPackageName().contains("camera")) {
                        proccessName = ProcessType.Camera.name();
                        suma = makeTimeInForegorund(us.getTotalTimeInForeground(), cameraTime);
                        cameraTime = us.getTotalTimeInForeground();
                    }
                    // webstie
                    else if (us.getPackageName().contains("mozilla") || us.getPackageName().contains("chrome")) {
                        proccessName = ProcessType.Website.name();
                        suma = makeTimeInForegorund(us.getTotalTimeInForeground(), websiteTime);
                        websiteTime = us.getTotalTimeInForeground();
                    }
                    Log.i("Pobieranie danych ", "ProcessService - wykryto proces: " + proccessName + " czas używania w [1s]: " + suma/1000);
                    //Log.i("TAG", " " + proccessName + " foreground " + suma / 1000 + " " + us.getLastTimeUsed());
                    BuildJSON.processJSONArray(proccessName, suma / 1000, us.getLastTimeUsed());

                }
            }


        }
        MainActivity.startTimeProcess = System.currentTimeMillis();
        //Log.i("TAG", "process czas2: " + s.format(new Date(MainActivity.startTimeProcess)));
    }

    /**
     * Metoda oblicza czas użycia danej aplikacji.
     * @param timeForeground Całkowity czas używania aplikacji od momentu uruchomienia programu..
     * @param x Całkowity czas korzystania z aplikacji liczony do poprzedniego uruchomienia funkcji.
     * @return Suma: zwraca czas używania aplikacji.
     */
    private long makeTimeInForegorund(long timeForeground, long x) {
        long suma = 0;
        return suma = timeForeground - x;
    }
}
