package com.example.martyna.Activities;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martyna.alarmReceivers.CallAlarmReceiver;
import com.example.martyna.alarmReceivers.DetectNetworkConnection;
import com.example.martyna.alarmReceivers.LocationAlarmReceiver;
import com.example.martyna.alarmReceivers.NotificationAlarmReceiver;
import com.example.martyna.alarmReceivers.ProcessAlarmReceiver;
import com.example.martyna.alarmReceivers.SmsAlarmReceiver;
import com.example.martyna.Services.ActivityRecognizedService;
import com.example.martyna.Utils.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.util.Calendar;

/**
 * Aktywność reprezentująca ekran główny aplikacji.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /**
     *  Obiekt umozliwiający integrację aplikacji z usługą Google Play, umożliwiającą okreslanie aktywności użytkownika.
     */
    public GoogleApiClient mApiClient;

    /**
     *  Zmienna przechowywująca adres e-mail użytkownika.
     */
    public static String email;

    /**
     * Przycisk wywołujący metodę changeAppStatus().
     */
    Button btnStatus;

    /**
     * Przycisk wywołujący metodę logOut().
     */
    Button btnLogout;

    /**
     * Pole tekstowe, przechowywujące tekst zachęty do uruchomienia pobierania danych.
     */
    TextView encounterTextView;

    /**
     * Pole tekstowe zawierające nazwę aplikacji.
     */
    TextView mainTextView;

    /**
     *  Zmienna logiczna, okreslająca czy uruchamianie danych zostało włączone.
     *  Wartość false oznacza, iż aplikacja nie pobiera danych.
     *  Wartość true oznacza, iż pobieranie danych jest w toku.
     */
    public static boolean ifApiWasStarted = false;

    /**
     * Obiekt pomocniczy przy rozsyłaniu alarmu budzącego LocationAlarmReceiver
     */
    PendingIntent piLocation;

    /**
     * Obiekt pomocniczy przy rozsyłaniu alarmu budzącego ProcessAlarmReceiver
     */
    PendingIntent piProcess;

    /**
     * Obiekt pomocniczy przy rozsyłaniu alarmu budzącego CallAlarmReceiver
     */
    PendingIntent piCall;

    /**
     * Obiekt pomocniczy przy rozsyłaniu alarmu budzącego SmsAlarmReceiver
     */
    PendingIntent piSms;

    /**
     * Obiekt pomocniczy przy rozsyłaniu alarmu budzącego DetectNetworkConnection
     */
    PendingIntent piNet;

    /**
     * Obiekt pomocniczy przy rozsyłaniu alarmu budzącego NotificationAlarmReceiver
     */
    PendingIntent piNotification;

    /**
     * Zmienna przechowywująca czas, kiedy ostatni raz pobierano dane na temat połączeń telefonicznych.
     */
    public static long startTimeCall;

    /**
     * Zmienna przechowywująca czas, kiedy ostatni raz pobierano dane na temat wiadomości SMS.
     */
    public static long startTimeSMS;

    /**
     * Zmienna przechowywująca czas, kiedy ostatni raz pobierano dane na temat używanych aplikacji.
     */
    public static long startTimeProcess;

    /**
     * Zmienna określająca numer pozwolenia.
     */
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;


    /**
     * Abstrakcyjna metoda udostępniana przez klasę Activity.
     * Pierwsza metoda, która zostaje wywołana w czasie tworzenia aktywności.
     * Następuje tutaj powiazanie aktywności z widokiem ekranu, dzięki setContentView() oraz
     * zdobycie referencji do obiektów reprezentujacych widgety (przyciski, pola tekstowe itp), które
     * zostały zdefiniowane w pliku XML.
     * Tworzy obiekt reprezentujący klienta GoogleApiClient.
     * @param savedInstanceState Jeżeli aktywnosć zostanie jeszcze raz utworzona
     *                           obiekt przekazuje dane o stanie poprzedniej aktywności.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStatus = (Button) findViewById(R.id.btnStatus);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        encounterTextView = (TextView) findViewById(R.id.encounterTextView);
        mainTextView = (TextView)findViewById(R.id.mainTextView);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    /**
     * Metoda wywoływana, po przechwyceniu zdarzenia kliknięcia w przycisk btnStatus.
     * W przypadku wartości false zmiennej ifApiWasStarted metoda sprawdza czy użytkownik zezwolił na pobieranie danych wrażliwych z urządzenia,
     * przypsiuje obecny czas w zmiennych startTimeCall, startTimeSMS, startTimeProcess, uruchamia pobieranie danych poprzez wywołanie metody changeScheduleAlarmStatus()
     * z parametrem true oraz nawiązuje połączenie z GoogleApiClient.
     * Jeżeli wartość zmiennej ifApiWasStarted równa jest false, wówczas metoda zatrzymuje pobieranie danych poprzez wywołanie metody changeScheduleAlarmStatus()
     * z parametrem false oraz kończy połączenie z GoogleApiClient.
     * @param view Obiekt reprezentujący przycisk btnStatus,
     *             przechwytuje zdarzenie klikniecia w przycisk.
     */
    public void changeAppStatus(View view) {
        if (!ifApiWasStarted) {

            chceckPermisson();
            startTimeCall = System.currentTimeMillis();
            startTimeSMS = System.currentTimeMillis();
            startTimeProcess = System.currentTimeMillis();
            ifApiWasStarted = true;

            // uruchom wszytskie procesy
            changeScheduleAlarmStatus(true);
            mApiClient.connect();
            encounterTextView.setText("Działam!:)");
            btnStatus.setText("Zatrzymaj");

        }
        // zatrzymanie apki
        else if(ifApiWasStarted) {

            ifApiWasStarted = false;
            //zatrzymaj wszytskie procesy
            changeScheduleAlarmStatus(false);
            mApiClient.disconnect();
            encounterTextView.setText("Czekam na Twój ruch!");
            btnStatus.setText("Uruchom");
        }
    }

    /**
     * Metoda wywoływana po wcisnięciu przycisku 'Wyloguj'.
     * Dzięki uruchomieniu metody changeScheduleAlarmStatus() z parametrem o wartości false
     * aplikacja wyłącza pobieranie danych z telefonu i rozłącza się z klientem mApiClient oraz
     * przenosi użytkownika do ekranu WelcomeActivity.
     * @param view Obiekt reprezentujący przycisk btnLogout,
     *             przechwytuje zdarzenie klikniecia w przycisk.
     */
    public void logOut(View view) {
        WelcomeActivity.isLogin = false;
        changeScheduleAlarmStatus(false);
        mApiClient.disconnect();
        Intent i = new Intent(this, WelcomeActivity.class);
        startActivity(i);
    }


    /**
     * Metoda wywołuje sześć metod: processScheduleAlarm(), locationScheduleAlarm(), callScheduleAlarm(),
     * smsScheduleAlarm(), networkScheduleAlarm(), notificationScheduleAlarm() z odpowiednią wartością parametru flag.
     * @param flag Wartość logiczna, przekazywana do sześciu metod, które sa wywoływane w funkcji.
     */
    public void changeScheduleAlarmStatus(boolean flag) {
        processScheduleAlarm(flag);
        locationScheduleAlarm(flag);
        callScheduleAlarm(flag);
        smsScheduleAlarm(flag);
        networkScheduleAlarm(flag);
        notificationScheduleAlarm(flag);
    }


    /**
     * Metoda, która w zależnosci od wartości parametru, ustawia badź anuluje alarm odpowiedzialny za uruchomienie procesów, które pobierają dane na temat
     * procesów używanych przez użytkownika. W metodzie zainicjalizowany jest obiekt i typu Intent,
     * który w czasie włączenia alarmu, rozsyła odpowiednie powiadomienie
     * w systemie, które jest wyłapywanane przez klasę ProcessAlarmReceiver rozszerzającą BroadcastReceiver,
     * która uruchamia metody z ProcessService.
     * W przypadku wartości true zmiennej run następuje uruchomienie alarmu. Alarm uruchamia sie po raz pierwszy po piętnastu minutach
     * od włączenia przycisku 'Uruchom'
     * i powtarza się co piętnaście minut. Alarm wybudza urządzenia nawet jezeli jest wygaszone.
     * W przypadku wartości false zmiennej run alarm zostaje wyłączony i nie wybudza systemu co piętnaście minut.
     *
     * @param run Wartość logiczna określająca czy włączyć alarm (wartość zmiennej true) lub anulować (wartośc zmiennej false).
     */
    public void processScheduleAlarm(boolean run) {

        Intent i = new Intent(getApplicationContext(), ProcessAlarmReceiver.class);
        piProcess = PendingIntent.getBroadcast(this, ProcessAlarmReceiver.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (run) {
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, piProcess);
            //alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, 65 * 1000, 300 * 1000, piProcess);
        } else {
            alarm.cancel(piProcess);
        }
    }

    /**
     * Metoda, która w zależnosci od wartości parametru, ustawia badź anuluje alarm odpowiedzialny za uruchomienie procesów, które pobierają dane na temat
     * lokalizacji uzytkownika. W metodzie zainicjalizowany jest obiekt i typu Intent. Rozsyła on odpowiednie powiadomienia
     * w systemie, które są wyłapywanane przez klasę LocationAlarmReceiver rozszerzającą BroadcastReceiver.
     * W przypadku wartości true zmiennej run następuje uruchomienie alarmu. Alarm uruchamia sie po raz pierwszy po dziesięciu sekundach
     * od włączenia przycisku 'Uruchom' i powtarza się co dziesięć minut. Alarm wybudza urządzenia nawet jezeli jest wygaszone.
     * @param run Wartość logiczna określająca czy włączyć alarm (wartość zmiennej true) lub anulować (wartośc zmiennej false).
     */
    public void locationScheduleAlarm(boolean run) {

        Intent i = new Intent(getApplicationContext(), LocationAlarmReceiver.class);
        piLocation = PendingIntent.getBroadcast(this, LocationAlarmReceiver.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (run) {
            Log.i("Pobieranie danych", "Ustawienie alarmu - Lokalizacja");
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, (startTimeCall + 10 * 1000), (10 * 60 * 1000), piLocation);
            //alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, 30 * 1000, 30 * 1000, piLocation);
        } else {

            alarm.cancel(piLocation);
        }
    }

    /**
     * Metoda, która w zależnosci od wartości parametru, ustawia badź anuluje alarm odpowiedzialny za uruchomienie procesów, które pobierają dane na temat
     * połączeń telefonicznych użytkownika. W metodzie zainicjalizowany jest obiekt i typu Intent. Rozsyła on odpowiednie powiadomienia
     * w systemie, które są wyłapywanane przez klasę CallAlarmReceiver rozszerzającą BroadcastReceiver.
     * W przypadku wartości true zmiennej run następuje uruchomienie alarmu. Alarm uruchamia sie o godzinie 20.30 każdego dnia od momentu wciśnięcia
     * przycisku 'Uruchom'. Alarm wybudza urządzenia nawet jezeli jest wygaszone.
     * @param run Wartość logiczna określająca czy włączyć alarm (wartość zmiennej true) lub anulować (wartośc zmiennej false).
     */
    public void callScheduleAlarm(boolean run) {

        Intent i = new Intent(getApplicationContext(), CallAlarmReceiver.class);
        piCall = PendingIntent.getBroadcast(this, CallAlarmReceiver.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 30);

        if (run) {
            Log.i("Pobieranie danych", "Ustawienie alarmu - Połączenia");
           // alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, (startTimeCall + 90 * 1000), (150 * 60 * 1000), piCall);
           alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, piCall);
        } else {
            alarm.cancel(piCall);
        }
    }

    /**
     * Metoda, która w zależnosci od wartości parametru, ustawia badź anuluje alarm odpowiedzialny za uruchomienie procesów, które pobierają dane na temat
     * wiadomości SMS użytkownika. W metodzie zainicjalizowany jest obiekt i typu Intent. Rozsyła on odpowiednie powiadomienia
     * w systemie, które są wyłapywanane przez klasę SmsAlarmReceiver rozszerzającą BroadcastReceiver.
     * W przypadku wartości true zmiennej run następuje uruchomienie alarmu. Alarm uruchamia sie o godzinie 20.57 każdego dnia od momentu wciśnięcia
     * przycisku 'Uruchom'. Alarm wybudza urządzenia nawet jezeli jest wygaszone.
     * @param run Wartość logiczna określająca czy włączyć alarm (wartość zmiennej true) lub anulować (wartośc zmiennej false).
     */
    public void smsScheduleAlarm(boolean run) {


        Intent i = new Intent(getApplicationContext(), SmsAlarmReceiver.class);
        piSms = PendingIntent.getBroadcast(this, SmsAlarmReceiver.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 57);

        if (run) {
            Log.i("Pobieranie danych", "Ustawienie alarmu - SMS");
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, piSms);
           // alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, (startTimeCall + 120 * 1000), (10 * 60 * 1000), piSms);
        } else {
            Log.i("TAG", "anulowanie alarmu S");
            alarm.cancel(piSms);
        }

    }

    /**
     * Metoda, która w zależnosci od wartości parametru, ustawia badź anuluje alarm odpowiedzialny za uruchomienie procesów, które sprawdzają
     * czy jest nawiązane połączenie z Internetem i jeżeli jest - wysyłaja pobrane dane na serwer.
     * Metoda gwarantuje, iż dane będą wysyłane cyklicznie, jeżeli użytkownik ma ciągle włączony dostęp do Intenretu.
     * W metodzie zainicjalizowany jest obiekt i typu Intent. Rozsyła on odpowiednie powiadomienia
     * w systemie, które są wyłapywanane przez klasę DetectNetworkConnection rozszerzającą BroadcastReceiver.
     * W przypadku wartości true zmiennej run następuje uruchomienie alarmu. Alarm uruchamia sie o godzinie 18.02 każdego dnia od momentu wciśnięcia
     * przycisku 'Uruchom'. Alarm wybudza urządzenia nawet jezeli jest wygaszone.
     * @param run Wartość logiczna określająca czy włączyć alarm (wartość zmiennej true) lub anulować (wartośc zmiennej false).
     */
    public void networkScheduleAlarm(boolean run) {
        Intent i = new Intent(getApplicationContext(), DetectNetworkConnection.class);
        piNet = PendingIntent.getBroadcast(this, DetectNetworkConnection.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 2);

        if (run) {
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, piNet);
        } else {
            alarm.cancel(piNet);
        }
    }

    /**
     * Metoda, która w zależnosci od wartości parametru, ustawia badź anuluje alarm odpowiedzialny
     * za uruchomienie procesu, którego rezultatem jest wyświetlenie powiadomienia na zewnątrz aplikacji. W powiadomieniu zawarta jest informacja
     * przypominająca o możliwości sprawdzenia wyników analizy danych na stronie internetowej.
     * W metodzie zainicjalizowany jest obiekt i typu Intent. Rozsyła on odpowiednie powiadomienia
     * w systemie, które są wyłapywanane przez klasę NotificationAlarmReceiver rozszerzającą BroadcastReceiver.
     * W przypadku wartości true zmiennej run następuje uruchomienie alarmu. Alarm uruchamia sie o godzinie 16.00 po trzech dniach od uruchomienia procesu pobierania danych
     * i powtarzany jest co 3 dni.
     * Alarm nie wybudza urządzenia jeżeli jest uśpione. Alarm zostaje wykonany w momencie obudzenia się urządzenia.
     * @param run Wartość logiczna określająca czy włączyć alarm (wartość zmiennej true) lub anulować (wartośc zmiennej false).
     * @param run
     */
    public void notificationScheduleAlarm(boolean run) {
        Intent i = new Intent(getApplicationContext(), NotificationAlarmReceiver.class);
        piNotification = PendingIntent.getBroadcast(this, NotificationAlarmReceiver.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);

        if (run) {
            alarm.setInexactRepeating(AlarmManager.RTC, 3 * 24 * 60 * 60 * 1000, 3 * AlarmManager.INTERVAL_DAY, piNotification);
        } else {
            alarm.cancel(piNet);
        }

    }

    /**
     * Metoda udostępniana przez interfejs GoogleApiClient.ConnectionCallbacks,
     * uruchamia klasę ActivityRecognizedService oraz ustawia czas, po jakim API sprawdza aktywność uzytkownika (4 minuty).
     * @param bundle Pakiet danych dostarczanych przez usługę Google Play. Dozwolona jest wartość null.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 4 * 60 * 1000, pendingIntent);
    }

    /**
     * Metoda udostępniana przez interfejs GoogleApiClient.ConnectionCallbacks, wywoływana w przypadku wstrzymania połączenia z GoogleApiClient.
     * Umożliwia wznowienie połączenie.
     * @param i Kod błędu otrzymany po wstrzymaniu połaczenia z GoogleApiClient.
     */
    @Override
    public void onConnectionSuspended(int i) {
        mApiClient.connect();
    }

    /**
     * Metoda udostępniana przez interfejs GoogleApiClient.ConnectionCallbacks, wywoływana w przypadku
     * problemów z nawiązaniem polączenie z GoogleApiClient.
     * @param connectionResult Kod błędu otrzymany po błędnym połączeniu z GoogleApiClient.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mApiClient.connect();
        Log.i("TAG", "Nieudane połączenie. Error: " + connectionResult.getErrorCode());
    }

    /**
     * Zgodnie z dokumentacją Android wszystkie urządzenie z wersją systemu Android 6.0 lub wyżej
     * muszą mieć pozwolenie użytkownika na pobieranie wrażliwych danych. Do tej grupy zaliczane są dane m.in o lokalizacji
     * czy używanych przez użytkownika aplikacji. Metoda umożliwia sprawdzenie czy użytkownik zezwolił na pobieranie danych, jeżeli nie
     * uruchamia metodę requestPermission().
     */
    private void chceckPermisson() {

        if (!hasPermission()) {
            requestPermission();}
    }


    /**
     * Metoda sprawdza czy aplikacja ma pozwolenie na pobieranie danych.
     * @return Zwraca mode. Zmienna określa czy aplikacja ma pozwolenie na wykonywanie odpowiednich operacji czy nie.
     */
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    /**
     * Metoda włącza panel Ustawienia telefonu w celu uzyskania potwierdzenia,
     * czy uzytkownik pozwala aplikacji na pobieranie danych wrażliwych.
     */
    private void requestPermission() {
        Toast.makeText(this, "Wymagane pozwolenie od uzytkownika", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
    }


    /**
     * Metoda abstrakcyjna klasy FragmentActivity.
     * Metoda zwraca informacje w przypadku gdy użytkownik cofnał pozwolenie na pobieranie danych wrażliwych w czasie
     * dzialania aplikacji i wywołuje checkPermission().
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "resultCode " + resultCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS:
                chceckPermisson();
                break;
        }
    }

    /**
     * Metoda wywoływana, kiedy użytkownik wciśnie klawisz wstecz.
     * Nadpisanie metody uniemozliwia cofanie się do poprzedniej aktywności z użyciem
     * wyżej wspomnianego klawisza.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Activity.
     * W momencie niszczenia aktywności gwarantuje wyłączenie procesu pobierania danych i rozłącza się z
     * GoogleApiClient.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        processScheduleAlarm(false);
        locationScheduleAlarm(false);
        callScheduleAlarm(false);
        smsScheduleAlarm(false);
        mApiClient.disconnect();

    }
}