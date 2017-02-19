package com.example.martyna.enums;

/**
 * Definicja własnego typu danych, których nazwy określają typ danych wysyłanych na serwer aplikacji.
 */
public enum ConnectionTask {

    /**
     * Obiekt Register ze zdefiniowanym adresem URl, na który wysłane zostaną dane na temat rejestracji użytkownka.
     */
    Register("http://hypnos.ds.pg.gda.pl:8080/serwer/serwer/register"),

    /**
     * Obiekt Login ze zdefiniowanym adresem URl, na który wysłane zostaną dane na temat logowania użytkownka.
     */
    Login("http://hypnos.ds.pg.gda.pl:8080/serwer/serwer/login"),

    /**
     * Obiekt Data ze zdefiniowanym adresem URl, na który wysłane zostaną dane pobrane z telefonu.
     */
    Data("http://hypnos.ds.pg.gda.pl:8080/serwer/serwer/activity");

    private String address;

    /**
     * Konstruktor ConnectionTask. Przypisuje adres URL do obiektu.
     * @param address Adres URL, na które zostaną wysłane dane.
     */
    ConnectionTask(String address) {
        this.address = address;
    }


    public String getAddress() {
        return address;
    }

}
