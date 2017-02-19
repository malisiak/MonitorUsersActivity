package com.example.martyna.Utils;

/**
 *  Klasa reprezentuje aktywności.
 */
public class RecognizedActivity {

    /**
     * Obiekt reprezentujący pierwszą wykrytą aktywność.
     */
    public static RecognizedActivity firstActivity;

    /**
     * Obiekt reprezentuje następna wykrytą aktywność.
     */
    public static RecognizedActivity secondActivity;


    /**
     * Zmienna określa typ aktywności (patrz ActivityType)
     */
    private String type;


    /**
     * Zmienna przechowuje datę rozpoczęcia aktywności.
     */
    private long startTimeActivity;

    /**
     * Zmienna przechowuje datę zakończenia aktywności.
     */
    private long endTimeActivity;


    /**
     * Zmienna przechowywująca czas trwania aktywności.
     */
    private long totalTimeActivity;


    /**
     * Konstruktor RecognizedActivity.
     * @param type Typ aktywności (patrz ActivityType).
     * @param startTimeActivity Czas rozpoczęcia aktywności.
     */
    public RecognizedActivity(String type, long startTimeActivity){
        this.type = type;
        this.startTimeActivity = startTimeActivity;
    }

    public String getType() {
        return type;
    }

    public long getStartTimeActivity() {
        return startTimeActivity;
    }


    public long getEndTimeActivity() {
        return endTimeActivity;
    }

    public void setEndTimeActivity(long endTimeActivity) {
        this.endTimeActivity = endTimeActivity;
    }

    public long getTotalTimeActivity() {
        return totalTimeActivity;
    }

    public void setTotalTimeActivity(long startTimeActivity, long endTimeActivity) {
        this.totalTimeActivity = (endTimeActivity - startTimeActivity) /1000;
    }
}
