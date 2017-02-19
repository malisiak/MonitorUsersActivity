package com.example.martyna.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.martyna.Utils.R;

/**
 * Aktywność reprezentująca ekran powitalny aplikacji.
 */
public class WelcomeActivity extends Activity {

    /**
     * Wartość logiczna określająca czy użytkownik jest zalogowany czy nie.
     * Wartość true oznacza uzytkownika zalogowanego do systemu.
     */
    public static boolean isLogin = false;

    /**
     * Przycisk, który wywołuje metodę goToRegistration()
     */
    Button btnRegistartion;

    /**
     * Przycisk, który wywołuje metodę goToLogin()
     */
    Button btnLogin;

    /**
     * Przycisk, który wywołuję metodę goToDescription()
     */
    Button btnDescription;

    /**
     * Pole tekstowe, przechowywujące nazwę apllikacji.
     */
    TextView welcomeTextView;

    /**
     * Pole teksowe przechowywujące tekst zachęty.
     */
    TextView welcomeHintText;

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Activity.
     * Pierwsza metoda, która zostaje wywołana w czasie tworzenia aktywności.
     * Następuje tutaj powiazanie aktywności z widokiem ekranu, dzięki setContentView() oraz
     * zdobycie referencji do obiektów reprezentujacych widgety (przyciski, pola tekstowe itp), które
     * zostały zdefiniowane w pliku XML.
     * Metoda pozwala zdefiniować czy uzytkownik jest zalogowany, jeżeli tak przenosi od razu do aktywności MainActivity.
     *
     * @param savedInstanceState Jeżeli aktywnosć zostanie jeszcze raz utworzona
     *                           obiekt przekazuje dane o stanie poprzedniej aktywności
     */
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        if (isLogin) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        btnRegistartion = (Button) findViewById(R.id.btnRegistartion);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnDescription = (Button) findViewById(R.id.btnDescription);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        welcomeHintText = (TextView) findViewById(R.id.welcomeHintText);
    }

    /**
     * Metoda wywoływana po wciśnięciu przez użytkownika klawisza 'Rejestruj'.
     * Umozliwia utworzenie nowej aktywności RegistrationActivity.
     *
     * @param view Obiekt reprezentujący przycisk btnRegistration,
     *             przechwytuje zdarzenie klikniecia w przycisk
     */
    public void goToRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * Metoda wywoływana po wciśnięciu przez użytkownika klawisza 'Zaloguj'.
     * Umozliwia utworzenie nowej aktywności loginActivity.
     *
     * @param view Obiekt reprezentujący przycisk btnlogin,
     *             przechwytuje zdarzenie klikniecia w przycisk
     */
    public void goToLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * Metoda wywoływana po wciśnięciu przez użytkownika klawisza 'Jak Działam'.
     * Umozliwia utworzenie nowej aktywności DescriptionActivity.
     *
     * @param view Obiekt reprezentujący przycisk btnDescription,
     *             przechwytuje zdarzenie klikniecia w przycisk
     */
    public void goToDescription(View view) {
        Intent i = new Intent(this, DescriptionActivity.class);
        startActivity(i);
    }

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Activity.
     * Metoda wywoływana, kiedy użytkownik wciśnie klawisz wstecz.
     * Nadpisanie metody uniemozliwia cofanie się do poprzedniej aktywności z użyciem
     * wyżej wspomnianego klawisza.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
