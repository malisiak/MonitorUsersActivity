package com.example.martyna.Services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.example.martyna.enums.CallType;
import com.example.martyna.Utils.BuildJSON;
import com.example.martyna.Activities.MainActivity;

/**
 * Klasa CallLogService zawiera metody odpowiedzialne za pobieranie danych połączeń telefonicznych.
 * CallLogService rozszerza klasę IntentService, która wykonuje pracę w wątku roboczym oraz
 * umożliwia przeprowadzenie operacji w tle. Wymaga nadpisania metody onHandleIntent.
 */
public class CallLogService extends IntentService {

    public CallLogService() {
        super("CallLogService");
    }

    /**
     * Metoda abstrakcyjna klasy IntentService.
     * Metoda pobiera dane z wewnętrznej bazy systemu Android dotyczące połączeń telefonicznych i zapisuje referencje do nowej tablicy,
     * następnie iteruje po ów tablicy i  wydobywa z niej istotne dane.  Informacje są zapisywane do obiektu typu callJsonArray, dzięki
     * wywołaniu funckji callJSONArray().
     * @param intent Obiekt przekazany podczas uruchomienia IntentService w metodzie onReceive() w SmsAlarmReceiver.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
       // Log.i("CALL", " in HandleIntent Call");
        //getCallLogs(MainActivity.currentTime);
       // Log.i("TAG", "SMS czas1: "+ s.format(new Date(MainActivity.startTimeCall)));
        Cursor c = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE}
                , CallLog.Calls.DATE + ">?", new String[]{String.valueOf(MainActivity.startTimeCall)}, CallLog.Calls.DATE + " ASC");

        Log.i("Pobieranie danych ", "CallLogService - lista połączeń od ostatniego wysłania danych: " + c.getCount() );
        if (c.getCount() > 0) {

            c.moveToFirst();

            do {
                Log.i("Pobieranie danych ", "CallLogService - iteracja po liscie w celu zapisania danych");
                String callName = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                long callDateandTime = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
                long callDuration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION));
                int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                if (callType == CallLog.Calls.INCOMING_TYPE) {
                    //incoming call
                    Log.i("Pobieranie danych ", "CallLogService - typ polaczenie: " + callType + " (przychodzące) dlugosc rozmowy " + callDuration   );
                    //Log.i("CALL", "" + String.valueOf(CallType.Incoming) + "dur " + callDuration + "name " + callName + " date " + callDateandTime);
                    BuildJSON.callJSONArray(String.valueOf(CallType.Incoming), callDuration, callName, callDateandTime);

                } else if (callType == CallLog.Calls.OUTGOING_TYPE) {
                    //outgoing call
                    Log.i("Pobieranie danych ", "CallLogService - typ polaczenie: " + callType + " (wychodzące) dlugosc rozmowy " + callDuration   );
                   // Log.i("CALL", "" + String.valueOf(CallType.Incoming) + "dur " + callDuration + "name " + callName + " date " + callDateandTime);
                    BuildJSON.callJSONArray(String.valueOf(CallType.Outcoming), callDuration, callName, callDateandTime);

                } else if (callType == CallLog.Calls.MISSED_TYPE) {
                    //missed call
                    Log.i("CALL", "" + String.valueOf(CallType.Incoming) + "dur " + callDuration + "name " + callName + " date " + callDateandTime);
                    BuildJSON.callJSONArray(String.valueOf(CallType.Missing), callDuration, callName, callDateandTime);

                }
            } while (c.moveToNext());
            c.moveToPrevious();
           // Log.i("CALL", "po wyjsciu dowhile c: " + c.getLong(c.getColumnIndex(CallLog.Calls.DATE)));

            MainActivity.startTimeCall = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
           // Log.i("TAG", "SMS czas2: "+ s.format(new Date(MainActivity.startTimeCall)));
            c.close();
        }

    }
}
