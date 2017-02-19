package com.example.martyna.Services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.martyna.enums.SMSType;
import com.example.martyna.Utils.BuildJSON;
import com.example.martyna.Activities.MainActivity;

/**
 * Klasa SmsLogService zawiera metody odpowiedzialne za pobieranie danych dotyczących wiadomości SMS.
 * SmsLogService rozszerza klasę IntentService, która wykonuje pracę w wątku roboczym oraz
 * umożliwia przeprowadzenie operacji w tle. Wymaga nadpisania metody onHandleIntent.
 */
public class SmsLogService extends IntentService {


    public SmsLogService() {
        super("SmsLogService");
    }


    /**
     * Metoda abstrakcyjna klasy IntentService.
     * Metoda pobiera dane z wewnętrznej bazy systemu Android dotyczące wiadomości SMS i zapisuje referencje do nowej tablicy,
     * następnie iteruje po ów tablicy i  wydobywa z niej istotne dane.  Informacje są zapisywane do obiektu typu smsJsonArray, dzięki
     * wywołaniu funckji smsJSONArray().
     *
     * @param intent Obiekt przekazany podczas uruchomienia IntentService w metodzie onReceive() w SmsAlarmReceiver.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        // określa adres dostępu do bazy danych
        Uri uri = Uri.parse("content://sms");

       /*  stwórz tablicę, w której znajduje się wynik zapytania bazodanowego. Zapytanie: pobierz dane z kolumny
         type, date , address, pod warunkiem, że data jest większa niż MainActivity.startTimeSMS oraz posortuj wyniki rosnąco.
       */
        Cursor c = getApplicationContext().getContentResolver().query(uri, new String[]{"type", "date", "address"}, "date" + ">?", new String[]{String.valueOf(MainActivity.startTimeSMS)}, "date" + " COLLATE LOCALIZED ASC");

        // Jeżeli liczba krotek w tablicy jest większa niz zero, iteruj po tablicy i zapisz dane do smsJsonArray.
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                // data
                long dateSMS = c.getLong(c.getColumnIndex("date"));
                // imię osoby, od/do ktorej wysłany został sms
                String namePerson = getContactName(getApplicationContext(), c.getString(c.getColumnIndexOrThrow("address")));

                // sms przychodzące
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    BuildJSON.smsJSONArray(SMSType.Inbox.toString(), namePerson, dateSMS);

                    // sms wychodzące
                } else if (c.getString(c.getColumnIndexOrThrow("type")).contains("2")) {
                    Log.i("Pobieranie danych ", "SmsLogService - wykryto wiadomość typu Outbox: ");
                    BuildJSON.smsJSONArray(SMSType.Outbox.toString(), namePerson, dateSMS);
                }

            } while (c.moveToNext());
            // Wróc do ostatniej krotki
            c.moveToPrevious();
            // zapisz date z ostatniej krotki w celu pobierania danych z tego miejsca
            MainActivity.startTimeSMS = c.getLong(c.getColumnIndex("date"));
            c.close();
        }

    }

    /**
     * Metoda pozwala okreslić imię osoby, od/do której został wysłana wiadomość SMS na podstawie numeru telefonu.
     *
     * @param context     Kontekst aplikacji.
     * @param phoneNumber Numer telefonu osoby od/do której został wysłana wiadomość SMS.
     * @return Imię osoby od/do której został wysłana wiadomość SMS.
     */
    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }
}
