
<h1 align="center">Maker Projekt: Warmduscher-App mit dem Raspberry Pi</h1>

<p align="center">
  <img src="thserver/docs/github/media/app-icon-1024x1024.png" alt="angular-logo" width="120px" height="120px"/>
  <br>
  <i>Ein kleines DYI-Maker Projekt, basierend auf dem Raspberri Pi, das die Daten der Erd-Sonde Wärmepumpe ausliest und in einer App zur Verfügung stellt. </i> <p align="center">
  <br>
</p>

### Was 
#### Technischer Aufbau
- Wir haben eine Erdsonden basierte Wärmepumpe von [Heim AG](https://www.heim-ag.ch/).
- Die Wärme-Pumpe wird durch einen Mikrokontroller von [Carel](https://www.carel.com/programmable-controls) betrieben. 
- Der Kontroller ist mittels [Modbus](https://en.wikipedia.org/wiki/Modbus) Protokoll über TCP/IP via LAN auslesbar.
- Das Auslesen übernimmt ein kleiner Raspberri Pi (Zero v2)
- Auf dem Raspberry läuft eine Java ([Spring Boot](https://spring.io/projects/spring-boot)) Applikation, welche die Wärme-Pumpe Daten ausliest und in einer lokalen [Postgres](https://www.postgresql.org/) DB abspeichert.
- Als Client kommt eine [Angular](https://angular.io/) basierte Web-Applikation zum Einsatz. 
- Als [Progressive Web App](https://en.wikipedia.org/wiki/Progressive_web_application) kann die Applikation im Desktop browser oder auf dem Smartphone aufgerufen werden. 
- Es sind verschiedene aktuelle und historische Daten abrufbar:
  - Aktuelle und histrorische Boiler Temperatur
  - Die Temperatur der Sole (Erdsonde) beim Eintritt und Austritt aus dem Erdreich
  - Temperatur des Heizkreislaufs
  - Umgebungstemperatur
  - Analysen über den historischen Verbrauch des Warmwassers pro Wochentag oder Stunde des Tages

### Warum?

- Warum nicht? :blush:
- Es gab den Moment als jemand im Haus Baden oder Duschen wollte, aber der Boiler war kalt. Mit der App kann man nun bequem vor der Dusche prüfen, ob genug warmes Wasser vorhanden ist. Keine kalten Duschen mehr...
- Grundsätzlich bin ich neugierig. Es interessiert mich wie sich die Erdsonde verhält. Es wird nun möglich sein folgende Fragen zu beantworten:
  - Kühlt sich die Erdsonde aus? Ich hab von Sonden gehört die sich über die Jahre abkühlten. 
  - Die Langzeit Statistiken der App zeigen die Temperatur der Erdsonde präzise an. Ebenfalls ist die Differenz zwischen Ein- und Austritt ablesebar, also wie gut die Erdsonde arbeitet.
  - Einblick in das Geschehen vom Boiler. 

### Demo

#### Live System
Eine <b>Demo</b> der App gibt es live hier <b> [https://mindalyze.com/pi11](https://mindalyze.com/pi11) </b> 

#### Demo Video
https://user-images.githubusercontent.com/6230096/154520493-6d321ba8-47d9-4cea-9c2b-32f8d8aef16d.mp4


### Getting Started
  Bitte Nachfragen.  

### Haftungsausschluss und Lizenz
Die Heizungs-App ist als Open-Source verfügbar: <a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License</a>.<br>
<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">
    <img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png" />
</a>

Die Benützung und Installation erfolgt auf eigenes Risiko.
