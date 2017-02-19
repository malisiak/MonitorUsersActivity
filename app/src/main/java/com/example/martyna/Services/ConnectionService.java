package com.example.martyna.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.martyna.enums.ConnectionTask;
import com.example.martyna.Utils.BuildJSON;
import com.example.martyna.Activities.MainActivity;
import com.example.martyna.Activities.WelcomeActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Klasa ConnectionService zawiera metody odpowiedzialne za nawiązywanie połączenia z serweram aplikacji oraz określenie
 * danych, które zostaną wysłane.
 * ConnectionService rozszerza klasę IntentService, która wykonuje pracę w wątku roboczym oraz
 * umożliwia przeprowadzenie opeacji w tle. Wymaga nadpisania metody onHandleIntent.
 */
public class ConnectionService extends IntentService {

    public ConnectionService() {
        super("ConnectionService");
    }

    /**
     * Metoda abstrakcyjna klasy IntentService.
     * Metoda odbiera informacje przesłane razem z obiektem typu Intent, który uruchomił metodę.
     * Na ich podstawie określa jaki typ danych będzie przesyłany na serwer aplikacji oraz na jaki adres URL.
     * Następnie zostaje wywołana metoda connection().
     *
     * @param intent Obiekt przekazany podczas uruchomienia IntentService w metodzie onReceive() w DetectNetworkConnection.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        if (extras != null) {
            String value = intent.getStringExtra("task");

            if (value.equals(ConnectionTask.Data.name())) {
                String address = ConnectionTask.Data.getAddress();
                connection(address, BuildJSON.bulidJSONToSend(), ConnectionTask.Data.name());
            } else if (value.equals(ConnectionTask.Login.name())) {
                String address = ConnectionTask.Login.getAddress();
                connection(address, BuildJSON.jsonLogin, ConnectionTask.Login.name());
            } else if (value.equals(ConnectionTask.Register.name())) {
                String address = ConnectionTask.Register.getAddress();
                connection(address, BuildJSON.jsonRegister, ConnectionTask.Register.name());
            } else {
                Log.i("TAG", "Błąd przekazania informacji w Intencie");
            }
        }

    }

    /**
     * Metoda nawiązuje połączenie z serwerem aplikacji i wysyła dane na serwer.
     * Po otrzymaniu odpowiedzi HTTP z serwera, funkcja podejmuje decyzję na tamat dalszych działań.
     * Odpowiedź oznaczona kodem 2xx - oznacza prawidłowe przesłanie danych, kod 3xx - nieprawidłowe wykonanie operacji po stronie serwera,
     * natomiast kod 4xx - konflikt, spowodowany błędnymi danymi wysłanymi przez użytkownika.
     * W przypadku wysłania danych, pobieranych z urządzenia, odpowiedż oznaczająca sukces, powoduje
     * usunięcie informacji z tablcy typu JSONAray oraz obiektu jsonToSend.
     * Dla danych z rejestracji i logowania odpowiedż oznaczająca sukces, powoduje zalogowanie do systemu i przekierowanie użytkownika do ekranu głównego aplikackji,
     * natomiast odpowiedź należąca do grupy konfliktu wyświetla odpowiedni komunikat dla użytkownika na ekranie aplikacji.
     *
     * @param address    Adres URL, na który zostaną wysłane dane na serwer aplikacji.
     * @param jsonObject Dane, które mają zostać wysłane na serwer aplikacji.
     * @param task       Rodzaj wydarzenia, który uruchomił metodę (patrz ConnectionTask)
     */
    public void connection(String address, JSONObject jsonObject, String task) {


        try {

            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            Log.i("Pobieranie danych", " ConnectionService - Nawiązanie połączenie z serwerem");
            httpURLConnection.connect();


            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            Log.i("Pobieranie danych", " ConnectionService - zawartość jsonToSend: " + jsonObject.toString());
            osw.write(jsonObject.toString());
            osw.flush();
            osw.close();
            int code = httpURLConnection.getResponseCode();

            // DATA
            if (task.equals(ConnectionTask.Data.name())) {
                if (code == 200) {
                    Log.i("Pobieranie danych", "ConnectionService - dane wysłane na serwer, odpowiedz serwera: " + code);
                    BuildJSON.jsonToSend = new JSONObject();
                    BuildJSON.removeElements();
                } else if (code == 409) {

                    Log.i("TAG", "Dane nie przesłane na serwer");
                }

                //REGISTER
            } else if (task.equals(ConnectionTask.Register.name())) {

                if (code == 201) {
                    Log.i("Rejestracja", "Klasa ConnectionService - użytkownik dodany do bazy, odpowiedz serwera: " + code);
                    //uruchomienie aktywności
                    BuildJSON.jsonId(MainActivity.email);
                    Log.i("email ", " W server " + BuildJSON.jsonId.toString());
                    WelcomeActivity.isLogin = true;
                    Intent dialogIntent = new Intent(this, MainActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(dialogIntent);

                } else if (code == 409) {
                    showToast("Użytkownik juz istnieje w bazie");
                    Log.i("TAG", "Użytkownik istnieje w bazie!");

                } else if (code == 304) {
                    showToast("Bład w serwerze");
                    Toast.makeText(getApplicationContext(), "Bład w serwerze", Toast.LENGTH_LONG).show();
                }
                BuildJSON.jsonRegister = new JSONObject();
                Log.i("TAG", "po remove JSONRegister : " + BuildJSON.jsonRegister);

                // LOGIN
            } else if (task.equals(ConnectionTask.Login.name())) {

                if (code == 200) {
                    Log.i("TAG", "zalogowany");

                    WelcomeActivity.isLogin = true;
                    //uruchomienie aktywności
                    BuildJSON.jsonId(MainActivity.email);
                    Log.i("email ", " W server " + BuildJSON.jsonId.toString());
                    Intent dialogIntent = new Intent(this, MainActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(dialogIntent);
                } else if (code == 409) {
                    showToast("Nieprawidłowe hasło!");
                    Log.i("TAG", "nieprawidłowe hasło");
                } else if (code == 424) {
                    showToast("Nie znaleziono użytkownika w bazie! Zarejestruj się.");
                    Log.i("TAG", "Nie znaleziono użytkownika w bazie! Zarejestruj się");
                } else if (code == 304) {
                    showToast("Bład w serwerze");
                }
                BuildJSON.jsonLogin = new JSONObject();
                Log.i("TAG", "po remove JSONLogin : " + BuildJSON.jsonLogin);


            }


        } catch (MalformedURLException e)

        {
            e.printStackTrace();
        } catch (IOException e)

        {
            e.printStackTrace();
        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pozwala wyświetlić krótki komunikat na ekranie użytkownika. Klasy dziedziczące po IntentService, nie mogą
     * bezpośrednio komunikować się z wątkiem głównym aplikacji, dlatego wiadomość wysyłana jest z innego wątku.
     *
     * @param message Wiadomość wyświetlana na ekranie użytkownika.
     */
    public void showToast(String message) {
        final String msg = message;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}




