# Wheather App  ☀️ ☁️ ⚡ 
Aceasta aplicatie afiseaza informatii meteorologice despre un oras, utilizand informatii obtinute de la un API.
Aplicatia de fata a fost dezvoltata dupa modelul Model-View-Controller(MVC). Interfata grafica dezvoltata are o singura
fereastra, aceasta fereastra contine doua comboBox-uri ce permite utilizatorului selectia unei tari si a unui oras in 
functie de tara selectata. Lista de orase este citita dintr-un fisier ce contine o lista cu numele oraselor 
si o serie de detalii despre fiecare oras in parte.

## Utilizare aplicatiei 💻 

Pentru a utiliza aplicatia trebuie sa avem o serie de fisiere de configurare.
Primul fisier este conf.json si are urmatoare structura:
```
{
  "apikey":"b7f04de2595bb2d357442a8a9bac4686", 
  "base_uri":"https://api.openweathermap.org",
  "end_point":"data/2.5/weather",
  "init_file":"src/main/resources/conf.txt",
  "type_request": "cc",
  "timeout":2000
}
``` 

, unde init_file este calea catre fisierul ce contine lista cu orase.Parametrul type_request reprezinta un parametru
ce il va primi metoda GET pentru a identifica orasul. Are urmatoarele valorile posibiele
```
ll- dupa longitudine si latitudine
cc- dupa numele orasului si al tarii
id- dupa id-ul utilizat de API pentru a identifica un oras
```

Cel de al doilea fisier este cel care contine lista de orase.Un exemplu de fiser este urmatorul.
```
ID	nm	lat	lon	countryCode
3169070	Rome	41.894741	12.4839	IT
3173435	Milan	45.464272	9.18951	IT
3172395	Napoli	40.833328	14.25	IT
```
Prima linie va fi mereu cea din exemplu, iar pe urmatoarele linii vor fi informatii despre orase.Este obligatoriu ca intre fiecare element sa fie un tab.
Daca fisierul nu are aceasta structura aplicatia nu se va putea utiliza.

Alte doua fisiere mai sunt logHistory.txt si logEvent.txt. in logHistory.txt vor fi logate toate momentele cand aplicatia va afisa detalii meteorologice
despre un oras. Iar in logEvent.txt vor fi logate evenimetele din aplicatie spre exemplu erorile ce pot aparea din cauza conexiunii la internet sau alte cauze.


Efectiv utilizarea aplicatie consta in selectatarea din cele doua comboBox-uri un oras si o tara, iar in momentul cand se alege orasul aplicatia face un call catre API 
si daca totul are loc cu succes vor fi afisate detalii meteorologice.

## Testarea aplicatiei

Testarea aplicatie consta in rularea testelor unitare implementate cu ajutorul librariilor JUnit si Mockito utilizand IDE-ul IntelliJ .  

## Team
Rosca Stefan 👨‍🎓
