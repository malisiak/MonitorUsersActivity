package com.example.martyna.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * W klasie BuildJSON znajdują się metody odpowiedzialne za tworzenie obiektów typu JsonObject.
 */
public class BuildJSON {

    /**
     * Tablica typu JSONArray przechowywująca dane na temat aktywności użytkownika.
     */
    private static JSONArray activityJsonArray = new JSONArray();

    /**
     * Tablica typu JSONArray przechowywująca dane na temat połączeń telefonicznych użytkownika.
     */
    private static JSONArray callJsonArray = new JSONArray();

    /**
     * Tablica typu JSONArray przechowywująca dane na temat wiadomości SMS użytkownika.
     */
    private static JSONArray smsJsonArray = new JSONArray();

    /**
     * Tablica typu JSONArray przechowywująca dane na temat aplikacji używanych przez użytkownika.
     */
    private static JSONArray processJsonArray = new JSONArray();

    /**
     * Tablica typu JSONArray przechowywująca dane na temat lokalizacji użytkownika.
     */
    private static JSONArray locationJsonArray = new JSONArray();

    /**
     * Obiekt typu JsonObject, którego elementami są tablice: activityJsonArray, callJsonArray, smsJsonArray,
     * processJsonArray, locationJsonArray.
     */
    public static JSONObject jsonToSend = new JSONObject();

    /**
     * Obiekt typu JsonObject, który przechowuje informacje na temat rejestracji użytkownika.
     */
    public static JSONObject jsonRegister = new JSONObject();

    /**
     * Obiekt typu JsonObject, który przechowuje informacje na temat logowania użytkownika.
     */
    public static JSONObject jsonLogin = new JSONObject();

    /**
     * Obiekt typu JsonObject, w którym przechowywany jest adres e-mail użytkownika.
     */
    public static JSONObject jsonId = new JSONObject();


    /**
     * Metoda tworzy obiekt typu JSONObject, w którym umieszcza informacje dotyczące typu aktywności (typy zostały określone w ActivityType),
     * czasu, kiedy aktywność rozpoczęła się oraz czasu trwania aktywności. Następnie obiekt umieszczony zostaje w
     * tablicy activityJsonArray.
     *
     * @param type              Typ aktywności (patrz ActivityType)
     * @param startTimeActivity Czas kiedy aktywność rozpoczęła się.
     * @param totalTime         czas trwania aktywności.
     */
    public static void activityJSONArray(String type, long startTimeActivity, long totalTime) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("activityType", type);
            jsonObject.put("dataStart", startTimeActivity);
            jsonObject.put("totalTime", totalTime);
            Log.i("TAG", "json: " + jsonObject.toString());

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        activityJsonArray.put(jsonObject);
        Log.i("TAG", "json: " + activityJsonArray.toString());
    }


    /**
     * Metoda tworzy obiekt typu JSONObject, w którym umieszcza informacje dotyczące typu połączenia telefonicznego (typy zostały określone w CallType),
     * czasu trwania rozmowy, imienia rozmówcy oraz daty, kiedy odbyła się rozmowa. Następnie obiekt umieszczony zostaje w
     * tablicy callJsonArray.
     *
     * @param callType   Typ połączenia telefonicznego (patrz CallType).
     * @param duration   Czas trwania rozmowy.
     * @param callerName Imię osoby, z którą została przeprowadzona rozmowa.
     * @param date       Data rozmowy.
     */
    public static void callJSONArray(String callType, long duration, String callerName, long date) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("callType", callType);
            jsonObject.put("duration", duration);
            jsonObject.put("callerName", callerName);
            jsonObject.put("date", date);

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        callJsonArray.put(jsonObject);
    }

    /**
     * Metoda tworzy obiekt typu JSONObject, w którym umieszcza informacje dotyczące typu wiadomości SMS (typy zostały określone w SMSType),
     * imienia osoby, do/od której została wysłana wiadomość, oraz daty, kiedy wysłany/odebrany został sms. Następnie obiekt umieszczony zostaje w
     * tablicy smsJsonArray.
     *
     * @param smsType    Typ wiadomości SMS (patrz SMSType).
     * @param personName Imie osoby, do/od której została wysłana wiadomość.
     * @param date       Data wysłania/odebrania wiadomości.
     */
    public static void smsJSONArray(String smsType, String personName, long date) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("smsType", smsType);
            jsonObject.put("personName", personName);
            jsonObject.put("date", date);

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        smsJsonArray.put(jsonObject);
    }

    /**
     * Metoda tworzy obiekt typu JSONObject, w którym umieszcza informacje dotyczące nazwy używanej przez uzytkownika aplikacji (nazwy zostały określone w ProcessType),
     * czasu używania aplikacji oraz daty korzystania z aplikacji. Następnie obiekt umieszczony zostaje w
     * tablicy processJsonArray.
     *
     * @param processName      Nazwa aplikacji (patrz ProcessType).
     * @param timeInForeground Czas używania aplikacji.
     * @param date             Data korzystania z aplikacji.
     */
    public static void processJSONArray(String processName, long timeInForeground, long date) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("procesName", processName);
            jsonObject.put("timeInForeground", timeInForeground);
            jsonObject.put("date", date);

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        processJsonArray.put(jsonObject);
    }

    /**
     * Metoda tworzy obiekt typu JSONObject, w którym umieszcza informacje dotyczące szerokości i długości geograficznej, daty wykrycia lokalizacji użytkownika,
     * nazwy dostawcy, oraz prędkości z jaką poruszał się użytkownik.
     * Następnie obiekt umieszczony zostaje w
     * tablicy locationJsonArray.
     *
     * @param latitude  Szerokość geograficzna.
     * @param longitude Długość geograficzna
     * @param date      Data, wykrycia lokalizacji użytkownika.
     * @param provider  Dostawca, określający lokalizację (GPS, Network).
     * @param speed     Prędkość z jaką porusza się użytkownik.
     */
    public static void locationJSONArray(double latitude, double longitude, long date, String provider, float speed) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("date", date);
            jsonObject.put("provider", provider);
            jsonObject.put("speed", speed);

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationJsonArray.put(jsonObject);

    }

    /**
     * W metodzie utworzony zostaje obiekt typu JsonObject, który przechowuje adres e-mail użytkownika.
     *
     * @param email Adres e-mail użytkownika.
     */
    public static void jsonId(String email) {
        try {
            jsonId.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * W metodzie utworzony zostaje obiekt typu JsonObject, który przechowuje imię, adres e-mail oraz hasło użytkownika.
     *
     * @param name     Imię użytkownika.
     * @param email    Adres e-mail użytkownika
     * @param password Hasło użytkownika.
     */
    public static void makeJsonRegister(String name, String email, String password) {

        try {
            jsonRegister.put("name", name);
            jsonRegister.put("email", email);
            jsonRegister.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * W metodzie utworzony zostaje obiekt typu JsonObject, który przechowuje adres e-mail oraz hasło użytkownika.
     *
     * @param email    Adres e-mail użytkownika
     * @param password Hasło użytkownika.
     */
    public static void makeJsonLogin(String email, String password) {

        try {
            jsonLogin.put("email", email);
            jsonLogin.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Metoda tworzy instancję jsonToSend typu JSONObject, której elementami są tablice activityJSONArray, processJsonArray, callJsonArray, smsJsonArray, locationJsonArray oraz obiekt jsonId.
     * Obiekt jsonToSend jest wysyłany do serwer aplikacji.
     *
     * @return Zwraca obiekt jsonToSend, w którym są dane pobrane z telefonu użytkownika.
     */
    public static JSONObject bulidJSONToSend() {

        try {
            jsonToSend.put("user", jsonId);
            jsonToSend.put("activities", activityJsonArray);
            jsonToSend.put("process", processJsonArray);
            jsonToSend.put("calls", callJsonArray);
            jsonToSend.put("sms", smsJsonArray);
            jsonToSend.put("location", locationJsonArray);

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonToSend;
    }

    /**
     * Metoda tworzy nowe referencje do tablic activityJSONArray, callJsonArray, smsJsonArray, processJsonArray, locationJsonArray,
     * wyniku czego te same dane nie będą wysyłane kilkaktornie do serwera aplikacji.
     */
    public static void removeElements() {
        Log.i("Remove", "remove element act " + activityJsonArray.length() + " loc " + locationJsonArray.length() + " pro " + processJsonArray.length());

        if (activityJsonArray.length() > 0) {
            activityJsonArray = new JSONArray();
        }
        if (callJsonArray.length() > 0) {
            callJsonArray = new JSONArray();
        }
        if (smsJsonArray.length() > 0) {
            smsJsonArray = new JSONArray();
        }
        if (processJsonArray.length() > 0) {
            processJsonArray = new JSONArray();
        }
        if (locationJsonArray.length() > 0) {
            locationJsonArray = new JSONArray();
        }
    }

    /**
     * Metoda sprawdza czy tablice activityJSONArray, callJsonArray, smsJsonArray, processJsonArray, locationJsonArray
     * zawierają elementy.
     *
     * @return Metoda zwraca wartość logiczną true, jeżeli tablice są puste oraz false - gdy tablice zawieraja elementy.
     */
    public static boolean isJsonArrayEmpty() {

        if (activityJsonArray.length() == 0 && callJsonArray.length() == 0 &&
                smsJsonArray.length() == 0 && processJsonArray.length() == 0 && locationJsonArray.length() == 0) {
            Log.i("BuildJSON", "Usunięcia danych z obikety JsonToSend");
            return true;
        }

        return false;
    }


}
