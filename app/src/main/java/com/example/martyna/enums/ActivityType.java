package com.example.martyna.enums;

/**
 * Definicja własnego typu danych, określających nazwy, wykrytych przez ActivityRecognizedAPI,
 * aktywności wykonywanych przez użytkownika.
 */
public enum ActivityType {

    /**
     * Aktywność: W pojezdzie.
     */
    InVehicle,

    /**
     * Aktywność: Na nogach, użytkownik może iść lub biec.
     */
    OnFoot,

    /**
     * Aktywność: Bieganie.
     */
    Running,

    /**
     * Aktywność: Chodzenie.
     */
    Walking,

    /**
     * Aktywność: Bezruch.
     */
    Still,

    /**
     * Aktywność: Wstrząsy urządzenia.
     */
    Tilting,

    /**
     * Aktywność: Nieznana.
     */
    Unknown


}
