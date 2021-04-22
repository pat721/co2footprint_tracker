# Co2 Footprint Tracker

Diese Anwendung ist ein Proof of Concept für einen Online Fußabdruckrechner auf einem mobilen Endgerät. Die mobile Applikation sammelt den Datenverbrauch der Benutzer und berechnet einen CO2-Wert auf Grundlage eines gegeben Berechnungsmodels. Zusätzlich wird dieser Wert in Form von plaktiven Equivalenten (wie z.B. Autokilometer, Handyladungen, usw.) darstellen.

Für den Proof of Concept beschränkt sich das Team auf die Implementierung einer **Android  Anwendung**.


## Designentscheidungen
Im folgenden werden grundlegende Designentscheidung für die Architektur und Implementierung der Anwendung aufgeführt.

### NetworkStatsManager
Der NetworkStatsManager ist ein vom Android System bereitgestellter Service. Er ermöglicht den Zugriff auf den Verlauf, so wie auf Statistiken der Netzwerknutzung. Die Nutzungsdaten werden in diskreten Zeitabschnitten, in sogenannten *Buckets*, gesammelt.



### SQLight


### Periodische 

## Requesting permissions
This application requires several runtime permissions as well as the ability to monitor usage stats.  All permissions are requested when the app is first launched and should be accepted by the user.  If you do not accept the permissions at launch then you can request them later through the menu.  **This is just a test app so if you don't accept permissions you may experience unexpected behaviour**


## Package uids
Each package has an associated kernal user-id (or uid) assigned to it, this uid is passed to the Network Stats API to differentiate between packages however it is not unique on the device.  What this means is that **you may see multiple applications reporting the same Rx/Tx statistics because under the covers they share the same uid**.  

I have tried to make this obvious by indicating on the UI where an application has a shared uid and uids are also written to the csv file.

Most other applications like this will group these kind of packages into "System" but I wanted to maintain the granularity of reporting statistics for as many applications or services as possible.
