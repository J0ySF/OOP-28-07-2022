package uniud.joysf;

import java.time.Duration;

/**
 * Sensori
 * L’azienda E-barche realizza e vende soluzioni di strumentazione elettronica e informatica
 * per barche a vela che consente all'equipaggio di avere sott'occhio tutti i dati relativi allo
 * stato della barca, della navigazione e poter agire su qualche dispositivo. In particolare, gli
 * strumenti consentono di visualizzare la posizione geografica data dal GPS (latitudine e
 * longitudine, come ad es. 45.3456 gradi N e 13.5432 gradi E), di proiettarla su una mappa
 * geografica/nautica, di visualizzare la direzione di movimento della barca (fornita dalla
 * bussola, in gradi bussola: 0 per il Nord, fino a 359), velocità di movimento (fornita dal
 * solcometro), velocità e direzione del vento, inclinazione longitudinale (beccheggio, in gradi
 * rispetto all'orizzontale: es -5 gradi~ inclinata verso poppa di 5 gradi)e laterale (rollio, in
 * gradi rispetto all'orizzontale: es. -5 gradi ~ inclinata 5 gradi a sinistra), profondità del mare
 * (in metri), distanza e direzione bussola rispetto a un eventuale punto di riferimento (come
 * ad es. il porto di Trieste). Il timone consente di sapere che angolo ha rispetto all'asse
 * longitudinale della barca (es. -5 gradi ~ pala del timone orientata 5 gradi a sx). Esiste
 * anche l'autopilota che, se attivato su un certo valore di gradi bussola, mantiene quella
 * direzione di moto della barca. E il motore che, se acceso, funziona a marcia avanti o
 * indietro con un certo numero di giri; valori che possono essere interrogati.
 * Ogni dispositivo (scandaglio, anemometro, accelerometri per l'inclinazione, GPS, bussola,
 * solcometro) espone degli endpoint che consentono di "leggere" i valori, che vengono
 * aggiornati automaticamente con una certa frequenza (ad es. 5Hz, 5x al secondo).
 * Serve progettare e realizzare una API che consenta di periodicamene interrogare ciascun
 * dispositivo, raccogliere i valori aggiornati e metterli a disposizione ai client dell'API, sia per
 * quanto riguarda i valori "istantanei" che delle medie mobili (ad es. la media della velocità
 * negli ultimi 2 secondi), e fornisce endpoint per accendere/spegnere il motore, accelerare o
 * rallentare, andare avanti o indietro; e endpoint per attivare/disattivare l'autopilota su una
 * certa direzione.
 * Progettare e implementare l'API almeno per queste operazioni:
 * * extractValue(device, ....) che espone il valore attuale di una certa grandezza gestita dal
 * dispositivo (es. latitudine del GPS)
 * * movingAverage(device, ...., N_seconds) per calcolare la media mobile di una certa
 * grandezza
 * * sendCommand(device, cmd, ...) per spedire un comando (es "accenditi", "acceletera a
 * 2000 giri") a un dispositivo
 * * configureDevice(...) che consente di aggiungere, o rimuovere, un dispositivo dalla
 * barca. Se aggiunto può essere manipolato come sopra, se tolto non potrà più venir usato.
 */

public class Main {
    public static void main(String[] args) throws ControlPanel.DeviceNotFoundException, Device.VariableDoesNotExistException, Device.InvalidCommandException {
        ControlPanel controlPanel = new ControlPanel(new Frequency(5, Frequency.Unit.SECOND));
        DeviceToken gps = controlPanel.addDevice(GPS::new);
        Value instant = controlPanel.extractValue(gps, "latitiude");
        Value average = controlPanel.movingAverage(gps, "longitude", Duration.ofSeconds(30));
        controlPanel.sendCommand(gps, new SimpleCommand("power off"));
        controlPanel.removeDevice(gps);
    }
}