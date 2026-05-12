# Dynamischer Json-Formular-Generator

## Projektbeschreibung

Im Rahmen dieses Projekts wurde eine Desktopanwendung zur dynamischen Generierung, Validierung und Speicherung von Formularen auf Basis von JSON-Dateien entwickelt. Ziel der Anwendung ist es, Formulare ohne Änderungen am Quellcode zur Laufzeit aus einer Konfigurationsdatei aufzubauen und die erfassten Daten strukturiert als JSON zu speichern.

Die Anwendung richtet sich an betriebliche Einsatzszenarien, in denen Formulare regelmäßig angepasst oder neu erstellt werden müssen. Durch die Trennung von Formularbeschreibung und Programmlogik kann die Wartbarkeit erhöht und der Anpassungsaufwand deutlich reduziert werden.

## Zielsetzung

Die Anwendung erfüllt insbesondere folgende Anforderungen:

- Einlesen einer Formularbeschreibung aus einer JSON-Datei.
- Dynamische Erzeugung einer grafischen Benutzeroberfläche mit Java Swing.
- Unterstützung mehrerer Feldtypen wie Textfeld, Textbereich, Dropdown, Checkbox, Zahlenfeld und Datumsfeld.
- Validierung von Pflichtfeldern, Datentypen und Enum-Werten.
- Speicherung der Formulardaten in einer Ergebnis-JSON-Datei.
- Wieder-Einlesen gespeicherter Werte zur automatischen Vorbelegung eines Formulars.

## Technologien

Für die Umsetzung wurden folgende Technologien verwendet:

- Java 17
- Java Swing
- Maven
- Gson
- JUnit 5

Die grafische Oberfläche wurde als klassische Desktopanwendung mit Swing umgesetzt, da dieser Technologieeinsatz auch im Projektumfeld vorgesehen ist.

## Projektstruktur

```text
json-form-generator/
├─ pom.xml
├─ src/main/java/
│  ├─ Main.java
│  ├─ model/
│  │  ├─ FormDefinition.java
│  │  └─ FormField.java
│  ├─ factory/
│  │  └─ FieldComponentFactory.java
│  ├─ service/
│  │  ├─ FormJsonService.java
│  │  └─ ResultJsonService.java
│  ├─ validation/
│  │  └─ FieldValidator.java
│  └─ ui/
│     ├─ DynamicFormFrame.java
│     └─ FieldComponentBinding.java
└─ src/test/java/
   ├─ FormValidatorTest.java
   └─ ResultJsonServiceTest.java
```

## Architektur

Die Anwendung ist in mehrere Verantwortungsbereiche unterteilt:

- **model**: Enthält die Datenklassen für Formulardefinition und Formularfelder.
- **ui**: Beinhaltet die grafische Oberfläche und die dynamische Darstellung der Felder.
- **service**: Übernimmt das Lesen und Schreiben von JSON-Dateien.
- **factory**: Erstellt abhängig vom `controlType` die passenden GUI-Komponenten.
- **validation**: Prüft Eingaben hinsichtlich Pflichtfeld, Datentyp und zulässiger Werte.

Durch diese Aufteilung wird eine klare Trennung der Verantwortlichkeiten erreicht. Dies verbessert die Wartbarkeit, Erweiterbarkeit und Testbarkeit der Anwendung.

## Start der Anwendung

### Voraussetzungen

- Java 17 oder höher
- Maven 3.8 oder höher

### Projekt bauen

```bash
mvn clean compile
```

### Tests ausführen

```bash
mvn test
```

### Anwendung starten

Die Anwendung kann direkt über die IDE durch Start der Klasse `Main` ausgeführt werden.

Alternativ kann sie auch über ein konfiguriertes Maven-Plugin oder über die IDE als Java-Anwendung gestartet werden.

## Bedienung

1. Über **„Formular laden“** wird eine JSON-Formulardefinition ausgewählt.
2. Die Anwendung erzeugt daraus automatisch die grafische Eingabemaske.
3. Nach Eingabe der Daten kann über **„Ergebnis speichern“** eine Ergebnis-JSON erzeugt werden.
4. Über **„Ergebnis laden“** können zuvor gespeicherte Werte wieder in das Formular übernommen werden.

## Beispiel für eine Formulardefinition

```json
{
  "formTitle": "Kunden-Feedback",
  "fields": [
    {
      "id": "name",
      "label": "Name",
      "controlType": "textfield",
      "dataType": "string",
      "required": true
    },
    {
      "id": "age",
      "label": "Alter",
      "controlType": "spinner",
      "dataType": "int",
      "required": false
    },
    {
      "id": "category",
      "label": "Kategorie",
      "controlType": "dropdown",
      "dataType": "enum",
      "required": true,
      "options": ["Support", "Vertrieb", "Allgemein"]
    },
    {
      "id": "newsletter",
      "label": "Newsletter",
      "controlType": "checkbox",
      "dataType": "boolean",
      "required": false
    },
    {
      "id": "birthday",
      "label": "Geburtsdatum",
      "controlType": "datefield",
      "dataType": "date",
      "required": false
    },
    {
      "id": "comment",
      "label": "Kommentar",
      "controlType": "textarea",
      "dataType": "string",
      "required": false
    }
  ]
}
```

## Beispiel für eine Ergebnisdatei

```json
{
  "formTitle": "Kunden-Feedback",
  "submittedAt": "2026-05-12",
  "values": {
    "Name": "Max Musterman",
    "Alter": 30,
    "Kategorie": "Allgemein",
    "Newsletter": true,
    "Geburtsdatum": "1996-11-11",
    "Kommentar": "Test komment!"
  }
}
```

## Qualitätssicherung

Zur Qualitätssicherung wurden automatisierte Tests für zentrale Funktionen implementiert:

- Prüfung der Validierungslogik für Pflichtfelder, Ganzzahlen und Enum-Werte.
- Test des Speicherns und erneuten Ladens von Ergebnisdaten.

Damit werden wesentliche fachliche Anforderungen automatisiert überprüft.

## Erweiterungsmöglichkeiten

Die Anwendung kann künftig um weitere Funktionen ergänzt werden, zum Beispiel:

- zusätzliche Feldtypen
- komplexere Validierungsregeln
- Trennung von View und Controller in eine noch strengere MVC-Struktur
- Exportfunktionen
- mehrsprachige Oberflächen
- Anbindung an Datenbanken oder Fachsysteme

## Hinweis zum Projektkontext

Die Anwendung wurde als eigenständige Desktoplösung konzipiert. Nicht Bestandteil des Projekts sind weitergehende Integrationen in Drittsysteme, produktive Rollout-Prozesse oder spätere funktionale Erweiterungen. Diese Abgrenzung entspricht dem definierten Projektumfang.