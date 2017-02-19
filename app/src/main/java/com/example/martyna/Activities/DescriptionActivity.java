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
 * Aktywność reprezentująca ekran opisu systemu.
 */
public class DescriptionActivity extends Activity {

    /**
     * Pole tekstowe, w którym znajduje się opis działania systemu.
     */
    TextView description;
    /**
     * Przycisk, który wywoluje metodę goToWelcomeActivity().
     */
    Button back;

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Activity.
     * Pierwsza metoda, która zostaje wywołana w czasie tworzenia aktywności.
     * Następuje tutaj powiazanie aktywności z widokiem ekranu, dzięki setContentView() oraz
     * zdobycie referencji do obiektów reprezentujacych widgety (przyciski, pola tekstowe itp), które
     * zostały zdefiniowane w pliku XML.
     *
     * @param savedInstanceState Jeżeli aktywnosć zostanie jeszcze raz utworzona
     *                           obiekt przekazuje dane o stanie poprzedniej aktywności
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        description = (TextView) findViewById(R.id.descriptionTextView);
        back = (Button) findViewById(R.id.btnLinkToWelcomeScreen);
    }

    /**
     * Metoda wywoływana, gdy zostanie wciśnięty przycisk 'powrót do menu',
     * umożliwia powrót do ekranu powitalnego
     *
     * @param view Obiekt reprezentujący przycisk back,
     *             przechwytuje zdarzenie klikniecia w przycisk
     */
    public void backToWelcome (View view){
        Intent i = new Intent(this, WelcomeActivity.class);
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
    }

}
