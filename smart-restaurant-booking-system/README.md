# CGI_Internship_Practice_2026

## Lahendus

Lõin "Nutika Restorani Reserveerimissüsteemi".

Selleks kasutasin nõutud Java Spring Boot ja viimast Java LTS versiooni. Frontend raamistikuks kasutasin Reacti koos Axiosega. Andmebaasiks kasutasin PostgreSQL ja LiquiBase tabelite haldamiseks.

- Laud -> (Laud 1, Laud 2, jne)
- Tsoon -> (VIP, Terrass, Sisesaal, Rõdu)
- Eelistus -> (Vaikne nurk, Akna all, Ratastooliga ligipääsetav, Pime nurk)

Broneering:
- Igal laual võib olla mitu broneeringut, kuid need ei tohi ajaliselt kattuda.
- Broneering kestab automaatselt 2 tundi
- Pärast broneeringu lõppu on laud uuesti saadaval
- Backend kontrollib, et broneering ei kattuks olemasoleva broneeringuga

Lisasin ka broneerimise funktsionaalsuse. Lauale peale vajutades on võimalik lauda broneerida valitud kellajaks. Broneerides jälgib backend, et broneeringu algusest kuni lõpuni (2 tundi)
ei kattuks teise broneeringuga.

Soovituste loogika arvestab mitut tegurit, sealhulgas laua tsooni, maksimaalset inimeste arvu, võimalikke eelistusi ning seda, kui hästi laud vastab broneerijate arvule. Kui kasutaja on määranud spetsiifilise eelistuse, näiteks ratastooliga ligipääsetava laua, siis antakse sellele kriteeriumile algoritmis kõrgem prioriteet, kuna selline nõue võib olla broneerija jaoks hädavajalik. Igale lauale antakse skoor vastavalt sellele kui hästi vastab filtreeringule. Kui mitu lauda on sama skooriga siis soovitatakse kõiki.

Laua filtreerimine ja soovitamine toimub täielikult backendi loogikas. Frontend ei tee arvutusi, vaid kuvab backendist saadud tulemused. Filtreerimise realiseerimiseks kasutasin Spring Data JPA Specification patternit.

Lisasin ka exceptionid juhtudeks kui broneering kattub teise broneeringuga või kui filtrite põhjal ei leitud ühtegi vastavat lauda.


## Tööks kulunud aeg ja keerulised aspektid

Selle ülesande lahendamiseks kokku läks umbes 12 tundi.

Terve projekti puhul oli minu jaoks kõige keerulisem osa süsteemi struktuuri läbimõtlemine. Pidin otsustama, kuidas andmebaas kõige mõistlikumalt üles ehitada ning kuidas realiseerida laudade kuvamine, filtreerimine ja soovitamine. Selle puhul panin kõik variandid eraldi kirja ja siis valisin neist kõige paremini tunduvad idee. Kasutasin ka nõrkuste/tugevuste vaatamiseks LLMi abi.

Backendi poolel osutus kõige keerulisemaks tabelite omavaheline sidumine ja broneeringute ajavahemike kontrollimine. Kuna kasutasin eraldi **Reservation tabelit**, tuli lahendada loogika, kuidas tuvastada ajavahemikud, mil laud on juba hõivatud.

Frontendi poolel oli minu jaoks kõige keerulisem veebilehe disaini ja kasutajaliidese ülesehitus. Eriti keeruline oli interaktiivsete elementide loomine, näiteks broneerimise puhul avanev eraldi aken (popup või modal), kus kasutaja saab valida broneeringu aja.

Üleüldiselt kõige keerulisem oli projekti käivitamise juhendi tegemine. Millegipärast ./gradlew BootRun kasutadse ei läinud liquibase käima ja database jäi tühjaks. Kuna ma ise kirjutan projekte, nii et mul on frontend ja backend eraldi projektidena avatud siis töötavad nad palju kergemalt. Database connectioni teen ka tavaliselt intellij IDEAs ära läbi uue datasource tegemise ja siis changelog-master.xml file manuaalse jooksutamisega. Ma ei teadnud kas see on piisav selle projekti puhul, nii et proovisin leida lihtsamat lahendust, et saaks lihtsalt commandidega üles saada.

---

## Projekti käivitamise juhend

---

## 1. Andmebaasi käivitamine (Docker)

Andmebaas töötab PostgreSQL-i konteineris. Käivita see käsuga:

```bash
cd smart-restaurant-booking-system
cd backend
docker compose up -d
```

---

## 2. Backend käivitamine (Spring Boot)

Backend kasutab **Java 25** ja **Spring Boot 4** koos **Gradle**.

```bash
cd smart-restaurant-booking-system
cd backend
./gradlew bootRun
```

---

## 3. Frontend käivitamine (React + Vite)

Frontend kasutab **React**, **TypeScript** ja **Vite**.

### 3.1 Sõltuvuste installimine

```bash
cd smart-restaurant-booking-system
cd frontend
npm install
```

### 3.2 Käivitamine

```bash
npm run dev
```
