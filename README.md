# BeQuiet
Die Android-App soll die Lautstärke und Notification-Responses deines Smartphones managen das es nie wieder klingelt, wenn es das nicht soll. 
Dies wird erreicht durch konfigurierbare Regeln, die in Kraft Treten wenn:
- man sich in einem vordefinierten Bereich befindet
- das Smartphone sich mit einem vordefinierten Wlan verbindet

## Technische Schulden
Android erlaubt nur "ein paar Mal in der Stunde" ein Update der Location im Hintergrund zu machen. [Link](https://developer.android.com/about/versions/oreo/background-location-limits)
Das hat zu Folge das unsere Regel oft nur verzögert aktiviert / deaktiviert werden können. Diese 
[LocationListener](app/src/main/java/com/example/bequiet/model/LocationListener.java) werden von Android aufgerufen.

Eine ähnliche Beschränkung besteht für WLAN-Namen (SSIDs). Diese können nur ausgelesen werden wenn der User 
bei "Location" erlaubt das die App IMMER auf die Location zugreifen darf. Ja, "Location", nicht irgendeine Wlan / Network Permission.

Der Android Fragment-Manager arbeitet nur mit R.id., nicht aber mit Referenzen. Dies führt zu einem Problem in unserer RecyclerView auf dem
Home-Screen, denn wir wissen erst beim setzen der einzelnen Elemente in einer Zelle ob wir das 
[WlanRuleFragment](app/src/main/java/com/example/bequiet/view/fragments/WlanRuleFragment.java) oder
[SelectAreaFragment](app/src/main/java/com/example/bequiet/view/fragments/SelectAreaFragment.java) benutzen möchten.
<br/>Problem: Da über die Zellen IDs doppelt vergeben werden (für jedes gleichartige UI-Element)
können wir keine Fragments in der RecyclerView anzeigen.
<br/>Lösung: Wir generieren eigene IDs für unsere Fragment Container 
und zeigen so die Fragmente an
<br/>Herausforderung: Fragmente müssen von uns gemanaged werden und nicht von Android.

## Architektur
Wir benutzen das MVP-Pattern. Im Rückblick auf das Projekt: Nur für die Home-Page macht es Sinn, aber auch nicht sonderlich viel, aufgrund der überschaubaren Anzahl an Funktionen.

Entry-Point der App is der [LoadingScreen](app/src/main/java/com/example/bequiet/view/loading/LoadingScreen.java) 
der sich um die Permissions kümmert und [LocationListenerRegistrer](app/src/main/java/com/example/bequiet/model/LocationListenerRegisterer.java) aufruft um unsere LocationListeners bei Android zu registrieren, falls 
das nicht schon über den [Boot-Up-Brodcast Receiver](app/src/main/java/com/example/bequiet/model/receivers/BrodcastReceiver.java) passiert ist. Auch wird ein Receiver bei Android 
registriert der den WLAN-Status beobachtet. (Wenn nicht schon über den Boot-Up Receiver erledigt.)

Hauptseite ist die [HomePageActivity](app/src/main/java/com/example/bequiet/view/home/HomePageActivity.java). Sie zeigt alle Regeln aus der
Datenbank an, und löst den Intent aus um eine neue Regel erstellen zu können.

Die [AddRuleActivity](app/src/main/java/com/example/bequiet/view/edit/AddRuleActivity.java) ist die Seite auf der man alle relevanten Informationen
für eine Regel eingibt.

## Contributors
- Niklas Kleiser, Mat-Nr: 35579
- David metzler, Mat-Nr: 35582