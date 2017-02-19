package com.example.martyna.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.martyna.enums.ActivityType;
import com.example.martyna.Utils.BuildJSON;
import com.example.martyna.Utils.RecognizedActivity;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Date;

/**
 * Klasa ActivityRecognizedService zawiera metody odpowiedzialne za pobranie danych z akcelerometru, określenie aktywności użytkownika
 * oraz zapisanie aktywności do obiektu actvityJsonArray.
 * ActivityRecognizedService rozszerza klasę IntentService, która wykonuje pracę w wątku roboczym oraz
 * umożliwia przeprowadzenie operacji w tle. Wymaga nadpisania metody onHandleIntent.
 */
public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }


    /**
     * Metoda odbiera listę rozpoznanych aktywności i przekazuje czynność o największym prawdopodobieństwie wystąpienia
     * metodzie handleDetectedActivities().
     * @param intent Obiekt typu Intent zawierający listę rozpoznanych aktywności.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            Log.i("Pobieranie danych", "ActivityRecognizedService: - pobrano listę aktywności");
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getMostProbableActivity());
        }
    }


    /**
     * Metoda przypisuje typom aktywności, wyrażonych w liczbach, odpowiednie nazwy. Następnie uruchamia metodę stateOfActivity()
     * przekazując w parametrze daną nazwę rozpoznanej czynności.
     * @param activity Aktywność o największym prawdopodobieństwie.
     */
    private void handleDetectedActivities(DetectedActivity activity) {

        Log.i("Pobieranie danych", "ActivityRecognizedService: wybrano najprawdopodobniejszą aktywności");

        switch (activity.getType()) {

            case DetectedActivity.ON_FOOT: {
                Log.e("ActivityRecogition", "Prawdopodobienstwo On Foot: " + activity.getConfidence());
                stateOfActivity(ActivityType.OnFoot);
                break;
            }
            case DetectedActivity.IN_VEHICLE: {
                Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                stateOfActivity(ActivityType.InVehicle);
                break;
            }

            case DetectedActivity.RUNNING: {
                Log.e("ActivityRecogition", "Running: " + activity.getConfidence());
                stateOfActivity(ActivityType.Running);
                break;
            }
            case DetectedActivity.STILL: {
                Log.e("Pobieranie danych", "ActivityRecognizedService: Wykryto: Bezruch, prawdopodobieństwo: " + activity.getConfidence());
                stateOfActivity(ActivityType.Still);
                break;
            }
            case DetectedActivity.TILTING: {
                Log.e("ActivityRecogition", "Tilting: " + activity.getConfidence() + " data" + new Date());
                stateOfActivity(ActivityType.Tilting);
                break;
            }
            case DetectedActivity.WALKING: {
                Log.e("ActivityRecogition", "Walking: " + activity.getConfidence() + " data" + System.currentTimeMillis());
                stateOfActivity(ActivityType.Walking);

                break;
            }
            case DetectedActivity.UNKNOWN: {
                Log.e("ActivityRecogition", "Unknown: " + activity.getConfidence() + System.currentTimeMillis());
                stateOfActivity(ActivityType.Unknown);
                break;
            }
        }
    }
    //}


    /**
     * Metoda zapisuje rozpoznane aktywności do obiektu activityJsonArray.
     * @param currentActivity Nazwa aktywności (patrz ActivityType)
     */
    public void stateOfActivity(ActivityType currentActivity) {


        // utworzenie aktywnosci
        if (RecognizedActivity.firstActivity == null) {
            RecognizedActivity.firstActivity = new RecognizedActivity(currentActivity.name(), System.currentTimeMillis());

           // Log.i("TAG", "utworzenie pierwszej akt " + RecognizedActivity.firstActivity.getType());
            return;
        }
        if (RecognizedActivity.firstActivity.getType().equals(currentActivity.name())) {
            // aktywnosc nie zmienila sie
            // druga aktywnosc zyla tylko przez 3 min, obecna aktywnosc == pierwszej
            if (RecognizedActivity.secondActivity != null) {
                RecognizedActivity.secondActivity = null;
                return;
            }
        }
        // aktywnosc jest inna
        else {
            // utworzenie drugiej aktywnosci
            if (RecognizedActivity.secondActivity == null) {

                RecognizedActivity.secondActivity = new RecognizedActivity(currentActivity.name(), System.currentTimeMillis());
                return;

            }
            // druga aktywnosc istnieje, obecnaAktywnosc jest inna niz pierwszaAktywnosc
            else {
                if (RecognizedActivity.secondActivity.getType().equals(currentActivity.name())) {
                    // drugaAktywnosc jest zgodna z obecna -> aktywnosc trwa dluzej niz 6min; pierwsza aktywnosc do zapisu druga aktywnosc staje sie pierwsza

                    RecognizedActivity.firstActivity.setEndTimeActivity(System.currentTimeMillis());
                    RecognizedActivity.firstActivity.setTotalTimeActivity(RecognizedActivity.firstActivity.getStartTimeActivity(), RecognizedActivity.firstActivity.getEndTimeActivity());
                    // firstActivity.send();
                    BuildJSON.activityJSONArray(RecognizedActivity.firstActivity.getType(), RecognizedActivity.firstActivity.getStartTimeActivity(), RecognizedActivity.firstActivity.getTotalTimeActivity());


                    RecognizedActivity.firstActivity = RecognizedActivity.secondActivity;
                } else {
                    // druga aktywnosc nie rowna sie obecnejAktywnosci ani pierwsza aktywnosc nie rowna sie obecnejAktywnsoci -> 3 rozne aktywnosci; pierwsza i druga idzie do zapsiu, obecna aktywnsc staje sie pierwsza
                    RecognizedActivity.firstActivity.setEndTimeActivity(System.currentTimeMillis());
                    RecognizedActivity.firstActivity.setTotalTimeActivity(
                            RecognizedActivity.firstActivity.getStartTimeActivity(),
                            RecognizedActivity.firstActivity.getEndTimeActivity());
                    // firstActivity.send();
                    BuildJSON.activityJSONArray(RecognizedActivity.firstActivity.getType(),
                            RecognizedActivity.firstActivity.getStartTimeActivity(),
                            RecognizedActivity.firstActivity.getTotalTimeActivity());
                    Log.i("TAG", "3 rozne aktywnosci");


                    RecognizedActivity.secondActivity.setEndTimeActivity(System.currentTimeMillis());
                    RecognizedActivity.secondActivity.setTotalTimeActivity(RecognizedActivity.secondActivity.getStartTimeActivity(), RecognizedActivity.secondActivity.getEndTimeActivity());


                    //second.send();
                    Log.i("TAG", "wysylam druga aktywnosc");
                    BuildJSON.activityJSONArray(RecognizedActivity.secondActivity.getType(), RecognizedActivity.secondActivity.getStartTimeActivity(), RecognizedActivity.secondActivity.getTotalTimeActivity());

                    RecognizedActivity.firstActivity = new RecognizedActivity(currentActivity.name(), System.currentTimeMillis());
                    RecognizedActivity.secondActivity = null;
                }
            }
        }
    }


}