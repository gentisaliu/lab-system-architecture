## Einleitung
Der Versuch befasste sich sowohl mit den Grundlagen der Threadsynchronisation in Java als auch mit Scheduling Strategien im Allgemeinen. 
In den einzelnen Teilversuchen galt es jeweils ein Schedulingmodell zu implementieren, welches eine Anzahl an CPU Last erzeugenden 
Threads managen sollte. Der Fortschritt der Lastthreads war graphisch anzuzeigen.

## Source
Der Quellcode ist folgendermaßen aufgebaut: Ein Objekt der Klasse `Launcher` dient zum
Starten der einzelnen Teilversuche und zeigt den Zustand der Lastthreads graphisch an. Sie
instanziiert entsprechend der Parameter mehrere `ScheduledThreadN` Objekte sowie ein
Objekt von Typ `SchedulerN` und registriert die Lastthreads beim Scheduler. Hierbei wird
im Scheduler für jeden Thread ein `Tcb` angelegt.
Die Klassen `SchedulerX` leiten sich alle von der Klasse `Scheduler` ab, diese ist ein `Thread`
und besitzt folgende wichtige Methoden und Variablen:

- Der Methode `registerThread` wird beim Aufruf ein Objekt vom Typ `ScheduledThread`,
sowie eine Integer-Variable `time`, welche die Größe der Zeitscheibe für diesen Thread
festlegt, übergeben. Sie erzeugt dann ein Objekt der Klasse `Tcb`, welches sie an
die `LinkedList` `tcbList` anhängt und ruft anschließend die Methode `start` des
`ScheduledThread`s auf.
- In der `run` Methode des `Scheduler`s findet das eigentliche Scheduling statt, sie
unterscheidet sich von Versuch zu Versuch und wird daher weiter unten erläutert.

Die `ScheduledThreadX` Klassen leiten sich alle aus `ScheduledThread` ab, sie sind Erben der Klasse `Thread` und besitzen neben einer Referenz auf ihren `Tcb` folgende wichtige
Methoden:

- Die Klassenmethode `scheduleLoop` dient der Synchronisation mit dem `Scheduler`,
hier wird auch die Laufbedingung überprüft. Sie muss dementsprechend regelmäßig
aufgerufen werden.
-  Die Klassenmethode `scheduleExit` muss der `ScheduledThread` vor Verlassen seiner
`run`-Methode aufrufen, um ein Erfassen der Zustandsänderung seitens des Schedulers
sicherzustellen.

Die Verschiedenen `Tcb`-Klassen dienen als Lock, darüber hinaus enthalten sie informationen über den Zustand der `ScheduledThread`s und eine Referenz auf den zugeordneten
`Thread`. Von Interesse sind hier vor allem folgende Variablen:

- die Boolean-Variable `mayRun` wird vom `ScheduledThread` in seiner `ScheduleLoop`
Methode überprüft, nur wenn sie _wahr_ ist, darf der Thread arbeiten.
- die Boolean-Variable `done` zeigt an, ob ein Thread seine Arbeit bereits abgeschlossen hat.
- die Integer-Variable `time` hat je nach Versuch eine unterschiedliche Bedeutung. In
Versuch 3.1 und 3.2 legt sie die Dauer der Timeslice fest, in Versuch 3.3 die Anzahl
an Slices und in 3.4 wird sie nicht verwendet.

Das Scheduling funktioniert in allen Versuchen nach folgendem Schema: Die Synchronisation zwischen den `ScheduledThread` Objekten und dem `Scheduler` erfolgt über die
`Tcb`s: zu Beginn warten alle `ScheduledThreads` auf ein `notify`-Event an ihrem `Tcb`. Der
`Scheduler` iteriert nun über alle `Tcb`s, er setzt jeweils `Tcb.mayRun` auf `true`, generiert
ein `notify`-Event und schläft dann entsprechend der Versuchsanweisung. Dann setzt
er `mayRun` wieder auf `false` und wartet auf eine Bestätigung des `ScheduledThread`s.
Dies geschieht bis die Abbruchedingung des Schedulers erfüllt ist (alle `ScheduledThread`s
haben ihre Arbeit abgeschlossen).

## Versuch 3.1
### Implementierung
#### Die Klasse `ScheduledThread1`
Die Klasse `ScheduledThread1` erweitert die `KlasseScheduledThread` um die Methode
`work`, welche zur Lasterzeugung eine Primzahlberechnung durchführt. In der `run`-Methode wird nun entsprechend der Integer-Variable `max`, eine `for`-Schleife durchlaufen, bei
jedem Durchlaufen des Schleifenrumpfes muss neben der Lasterzeugung die Methode
`scheduleLoop` Aufgerufen werden. In der Methode `ScheduleLoop` wird dann in einer
`while`-Schleife die Laufbedingung überprüft. Ist der Wert der Variable `mayRun` `false`, so
wird ein `notify`-Event generiert, um dem `Scheduler` das Ende der Arbeit mitzuteilen,
danach wartet der `ScheduledThread` selbst auf einen `notify`-Event, um seine Arbeit
fortzusetzen.

#### Die Klasse `Scheduler`
Die Klasse `Scheduler` arbeitet als Scheduler sowohl für Versuch 3.1 als auch 3.2. Sie
gewährt jedem `ScheduledThread` die im `Tcb` festgelegte Zeit, somit müssen für Versuch
3.1 alle `ScheduledThread`s mit der gleichen Zeitscheibe beim `Scheduler` registriert werden.

### Ergebnisse
Lief auf dem Testsystem nur der Scheduler, so gab es keinerlei auffälliges Verhalten, die
Threads schritten wie erwartet gleichmäßig voran. Sobald jedoch neben dem Experiment noch andere CPU-intensive Prozesse liefen, gab es interessantere Beobachtungen zu
machen. Da der Aufruf von `sleep` im Scheduler nicht garantiert, dass dieser, nach dem
Schlafen, direkt wieder CPU-Zeit zugewiesen bekommt, kam es vor, dass das Setzen von
`tcb.mayRun` auf `false` seitens des Schedulers verzögert wurde. Dies machte sich dann in
abweichendem Fortschritt bei den einzelnen `ScheduledThread`s zu bemerken.

## Versuch 3.2
### Implementierung
Die Implementierung unterscheidet sich nicht von der für Versuch 3.1 verwendeten.
### Ergebnisse
Wie zu erwarten, schreiten Anwenderthreads mit einer größeren Zeitscheibe entsprechend schneller voran.

## Versuch 3.3
### Implementierung
#### Die Klasse `TcbWithIo`
Die Klasse `TcbWithIo` erweitert die Klasse `Tcb`. Wichtige neue Variablen sind `io` und `maxIo`.

#### Die Klasse `ScheduledThreadRandIo`
Die Klasse `ScheduledThreadRandIo` erweitert die Klasse `ScheduledThread1` dahingehend, dass bei jedem Aufwecken durch den Scheduler eine gewisse Chance besteht, dass
der Thread mit einer Pseudoausgabe beginnt. Hierzu wird die Variable `ioChance` verwendet: ist sie kleiner als eine Zufallszahl aus dem Intervall (0,1), so wird `tcb.io` auf einen
pseudozuf¨alligen Wert aus dem Intervall [0, `tcb.maxIo`] gesetzt.

#### Die Klasse `Scheduler3`
Die Klasse `Scheduler3` erweitert die Klasse `Scheduler`. Die Methode `setQuantum` legt die
Dauer einer Zeitscheibe fest, der Scheduler schläft bei jeweils `quantum * Tcb.slices`.
Nach jedem Quantum wird überprüft, ob der Thread mit pseudo I/O beschäftigt ist. Ist
dies der Fall, so kommt direkt der nächste an die Reihe. Trifft der Scheduler auf einen
Prozess mit `Tcb.io` > 0, so dekrementiert er die Variable und fährt sofort mit dem nächsten
Thread fort.

#### Ergebnisse
Es ist zu beobachten, dass wenn die IO Threads eine Pseudo-Ausgabe produzieren, die
rechenintensiven Threads schneller fortschreiten.

## Versuch 3.4
### Implementierung
#### Die Klasse `Scheduler4`
Die Klasse `Scheduler4` erweitert die Klasse `Scheduler3` dahingehend, dass sie mit Hilfe der
Variable `TcbWithIo.type` das Konzept des Multilevel Feedbacks umsetzt. Zu Anfang gilt
für alle Threads `Tcb.type = 0`. Beginnt ein Thread während der Zeitscheibe mit einer
Pseudoausgabe, so bleibt er vom Typ 0 (interaktiv). Braucht er diese jedoch auf so gilt er
nun als Typ 1 Thread (ausgeglichen), braucht er beim nächsten Mal wieder seine gesamte
Zeitscheibe ohne Pseudoausgabe auf so gilt er als Typ 2 Thread (rechenintensiv). Threads
vom Typ n haben jeweils 1 - n Zeitscheiben zur Verfügung, beginnen sie währenddessen
mit einer Pseudoausgabe gelten sie sofort wieder als Interaktiv. Der Scheduler lässt immer nur Threads der höchsten Priorität (0,1,2) arbeiten, erst wenn kein Typ 0 Thread mehr
die CPU benötigt, kommen die Typ 1 Threads an die Reihe.

### Ergebnisse
Alles wie erwartet, keine Vorkommnisse.
