# CO2 Footprint Tracker

Diese Anwendung ist ein Proof of Concept für einen Online Fußabdruckrechner auf einem mobilen Endgerät. Die mobile Applikation sammelt den Datenverbrauch der Benutzer und berechnet einen CO2-Wert auf Grundlage eines gegeben Berechnungsmodels. Zusätzlich wird dieser Wert in Form von plaktiven Equivalenten (wie z.B. Autokilometer, Handyladungen, usw.) darstellen.

Für den Proof of Concept beschränkt sich das Team auf die Implementierung einer **Android  Anwendung**.

## Projekt Struktur
	co2footprint_tracker
	├── app
	│	└── src/main
	|		├── java/de/htwg/co2footprint_tracker
	|		|	├── database
	|		|	├── enums
	|		|	├── helpers
	|		|	├── model
	|		|	├── services
	|		|	└── utils
	|		└── res
	└── gradle/wrapper



| Folder                            | Description                                                  |
| --------------------------------- | ------------------------------------------------------------ |
| root                              | contains necessary parts for initializing and building the app |
| app                               | contains all necessary parts directly regarding the functionality of the app e.g. source code, build files |
| src/main                          | contains the main source code, viewing components and android manifest |
| java/de/htwg/co2footprint_tracker | contains only source code which belonges to logic and functionality of the app |
| database                          | contains source code that only belongs for managing databases |
| enums                             | contains source code for establishing own datatypes          |
| helpers                           | Contains source code for helper methods.<br />These helper methods, in contrast to utils, also contain advanced logic and functionalities |
| model                             | Contains source code for data transfer objects between classes, databases, views etc. |
| services                          | Contains source code for services that are run in background and do tasks for fullfilling the main app goal |
| utils                             | Contains source code for simple helpers e.g. string converters, constants etc. |
| res                               | Contains resource files and static content used by the source code e.g. bitmaps, fonts, etc. |
| gradle/wrapper                    | Contains files for gradle                                    |


## Designentscheidungen
Im folgenden werden grundlegende Designentscheidung für die Architektur und Implementierung der Anwendung aufgeführt.

### NetworkStatsManager
Der NetworkStatsManager ist ein vom Android System bereitgestellter Service. Er ermöglicht den Zugriff auf den Verlauf, so wie auf Statistiken der Netzwerknutzung. Die Nutzungsdaten werden in diskreten Zeitabschnitten, in sogenannten *Buckets*, gesammelt.

### SQLight


### Periodische 

## Requesting permissions
This application requires several runtime permissions as well as the ability to monitor usage stats.  All permissions are requested when the app is first launched and should be accepted by the user.  If you do not accept the permissions at launch then you can request them later through the menu.  **This is just a test app so if you don't accept permissions you may experience unexpected behaviour**


## Package UIDs
Each package has an associated kernal user-id (or uid) assigned to it, this uid is passed to the Network Stats API to differentiate between packages however it is not unique on the device.  What this means is that **you may see multiple applications reporting the same Rx/Tx statistics because under the covers they share the same uid**.  

I have tried to make this obvious by indicating on the UI where an application has a shared uid and uids are also written to the csv file.

Most other applications like this will group these kind of packages into "System" but I wanted to maintain the granularity of reporting statistics for as many applications or services as possible.