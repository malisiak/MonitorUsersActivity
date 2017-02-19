package com.example.martyna.Services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.martyna.Utils.BuildJSON;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;

/**
 * Klasa LocationService zawiera metody odpowiedzialne za pobieranie danych na temat lokalizacji użytkownika.
 * LocationService rozszerza klasę Service, która działa w wątku głównym aplikacji, jednakże
 * umożliwia przeprowadzenie operacji w tle oraz implementuje klasy GoogleApiClient.ConnectionCallbacks,
 * GoogleApiClient.OnConnectionFailedListener, LocationListener pozwalające na odczyt lokalizacji.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * Obiekt reprezentujący dane o lokalizacji użytkownika.
     */
    Location mLastLocation;


    /**
     * Obiekt umozliwiający integrację aplikacji z usługą Google Play, umożliwiającą okreslanie lokalizacji użytkownika.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Obiekt określający właściwości połączenia.
     */
    private LocationRequest mLocationRequest;

    /**
     * Obiekt przechowywujący szerokość geograficzną.
     */
    double lat;
    /**
     * Obiekt przechowywujący długość geograficzną.
     */
    double lon;

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Service.
     * Metoda wołana podczas pierwszego utworzenia Service.
     */
    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Service.
     * Metoda wywoływana za każdym kolejnym uruchomieniem Service. Nawiązuje połączenie z GoogleApiClient.
     *
     * @param intent  Obiekt przekazany podczas uruchomienia Service w klasie MainActivity.
     * @param flags   Dodatkowe dane na temat żądania uruchomienia Service.
     * @param startId Identyfikator reprezentujący dane żądanie uruchomienia Service.
     * @return Wartość liczbowa, wskazująca jaką akcję należy podjąć w przypadku przerwania działania Service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        return START_NOT_STICKY;
    }

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Service.
     * Metoda umożliwiająca komunikację z interfejsem użytkownika. Została nadpisana ze względu na rozszerzenie kalsy przez Service.
     * @param intent Obiekt przekazany podczas uruchomienia Service w klasie MainActivity.
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Metoda udostępniana przez interfejs GoogleApiClient.ConnectionCallbacks,
     * pobiera dane na temat lokalizacji i zapisuje je w obiekcie locationJSONArray za pomocą metody locationJSONArray().
     *
     * @param bundle Pakiet danych dostarczanych przez usługę Google Play. Dozwolona jest wartość null.
     */
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(582000); // Powtarzaj co każde 9,7 minuty

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(" yyyy:mm:dd mm:hh:ss");
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
            BuildJSON.locationJSONArray(lat, lon, mLastLocation.getTime(), mLastLocation.getProvider(), mLastLocation.getSpeed());
            Log.i("Pobieranie danych ", "LocationService - wykryto lokalizację: szer.geo" + lat + " dł. geo" + lon);
        }
    }

    /**
     * Abstrakcyjna metoda udostępniana przez klasę Service.
     * W momencie niszczenia Service gwarantuje rozłączenie się z
     * GoogleApiClient.
     */
    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    /**
     *
     * Metoda udostępniana przez interfejs GoogleApiClient.ConnectionCallbacks, wywoływana w przypadku wstrzymania połączenia z GoogleApiClient.
     * Umożliwia wznowienie połączenie.
     *
     * @param i Kod błędu otrzymany po wstrzymaniu połaczenia z GoogleApiClient.
     */
    @Override
    public void onConnectionSuspended(int i) {
        buildGoogleApiClient();
    }


    /**
     * Metoda abstrakcyjna udostępniona przez klasę LocationListener. Przypisuje wartość szerokości i długości geograficznej.
     * @param location Obiekt zawierający informacje na temat szerokości i długości geograficznej.
     */
    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lon = location.getLongitude();

    }

    /**
     * Metoda udostępniana przez interfejs GoogleApiClient.ConnectionCallbacks, wywoływana w przypadku
     * problemów z nawiązaniem polączenie z GoogleApiClient.
     * @param connectionResult Kod błędu otrzymany po błędnym połączeniu z GoogleApiClient.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("TAG", "Nieudane połączenie. Error: " + connectionResult.getErrorCode());
        buildGoogleApiClient();
    }

    /**
     * Nawiązanie połączenie z GoogleApiClient.
     */
    synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }
}
