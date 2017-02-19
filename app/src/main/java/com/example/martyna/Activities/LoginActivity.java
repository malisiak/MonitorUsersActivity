package com.example.martyna.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.martyna.Services.ConnectionService;
import com.example.martyna.enums.ConnectionTask;
import com.example.martyna.Utils.BuildJSON;
import com.example.martyna.Utils.IsNetworkConnection;
import com.example.martyna.Utils.R;
/**
 * Aktywność reprezentująca ekran logowania do systemu.
 */
public class LoginActivity extends Activity {


    /**
     * Pole edycyjne, w którym przechowywany jest adres email użytkownika.
     */
    EditText emailEdit;
    /**
     * Pole edycyjne, w którym przechowywana jest hasło użytkownika.
     */
    EditText passwordEdit;
    /**
     * Przycisk wywołujący metodę login().
     */
    Button btnLogin;
    /**
     * Przycisk wywołujący metodę backToWelcome.
     */
    Button btnLinkToWelcomeScreen;
    /**
     * Pole tekstowe, które umozliwia wyswietlenie komunikatów błędu np. nie uzupełnienia pól edycyjnych.
     */
    TextView info;


    /**
     * Abstrakcyjna metoda udostępniana przez klasę Activity.
     * Pierwsza metoda, która zostaje wywołana w czasie tworzenia aktywności.
     * Następuje tutaj powiazanie aktywności z widokiem ekranu, dzięki setContentView() oraz
     * zdobycie referencji do obiektów reprezentujacych widgety (przyciski, pola tekstowe itp), które
     * zostały zdefiniowane w pliku XML.
     * @param savedInstanceState Jeżeli aktywnosć zostanie jeszcze raz utworzona
     *                           obiekt przekazuje dane o stanie poprzedniej aktywności.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        emailEdit = (EditText) findViewById(R.id.emailEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToWelcomeScreen = (Button) findViewById(R.id.btnLinkToWelcomeScreen);
        info = (TextView) findViewById(R.id.info);
    }

    /**
     * Metoda wołana, gdy zostanie wciśnięty przycisk 'powrót do menu',
     * umożliwia powrót do ekranu powitalnego.
     * @param view Obiekt reprezentujący przycisk btnLinkToWelcomeScreen,
     *             przechwytuje zdarzenie klikniecia w przycisk.
     */
    public void backToWelcome (View view){
        Intent i = new Intent(this, WelcomeActivity.class);
        startActivity(i);
    }

    /**
     *
     * Metoda wywoływana kiedy użytkownik wcisnie klawisz Zaloguj.
     * Metoda umozliwia pobranie danych z pól edycyjnych - emailEdit i passwordEdit,
     * zapisanie danych do obiektu typu JsonObject oraz
     * uruchomienie metod z klasy ConnectionService, odpowiedzialnej za nawiązanie połączenie z serwerem aplikacji.
     * Ponadto w instanji Intent przekazane są dodatkowe informacje na temat typu wysyłanych danych -
     * zmienna typu enum Login wskazuje na wysłanie informacji dotyczących logowania użytkownika.
     * @param view Obiekt reprezentujący przycisk bntLogin,
     *             przechwytuje zdarzenie klikniecia w przycisk.
     */
    public void login (View view){
         if(isEmpty()&& IsNetworkConnection.isNetworkConnection(getApplicationContext())){

            String email = emailEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();
            MainActivity.email = email;
            BuildJSON.makeJsonLogin(email, password);
            Intent i= new Intent(this, ConnectionService.class);
            i.putExtra("task", ConnectionTask.Login.name());
            this.startService(i);
        }
    }

    /**
     * Metoda sprawdza czy wszystkie pola edycyjne zostały uzupełnione.
     * @return Zwraca wartość logiczną true - jeżeli pola zostały uzupełnione oraz false w przeciwnym wypadku.
     */
    public boolean isEmpty(){
        Log.i("TAG", "isEmpty");
        if(!(emailEdit.getText().toString().trim().length()>0 && passwordEdit.getText().toString().trim().length()>0)){
            info.setText("NIE UZUPELNILES WSZYSTKICH POL!");
            info.setTextColor(Color.parseColor("#b21c58"));
            return false;
        }
        else{
            info.setText("");
            return true;
        }
    }

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Activity.
     * Metoda wywoływana, kiedy użytkownik wciśnie klawisz wstecz.
     * Nadpisanie metody uniemozliwia cofanie się do poprzedniej aktywności z użyciem.
     * wyżej wspomnianego klawisza.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
