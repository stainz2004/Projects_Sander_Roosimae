## This is a workshop i made for university students to get to know libgdx and AI better.

# 👾 Example AI workshop 👾
Käesolevas projektis on implementeeritud lihtsustatud tehisintellekti loogika. Tegemist on väga elementaarse AI-lahendusega, mida ei käsitleta tavapärases mõistes täisfunktsionaalse tehisintellektina. Mänguprojekti kontekstis eeldatakse keerukama AI kasutamist, näiteks otsimis- või teekonna leidmise algoritmide (nt A* algoritm) rakendamist.

Tegemist on projektiga, milles on mingid funktsionaalsused eemaldatud ning Sina saad need juurde lisada. Töötoa eeldus on, et Sa oled teinud [Kliendi ja serveri töötuba](https://gitlab.cs.taltech.ee/iti0301-2025/kryonet-workshop) 
ja [Example game workshopi](https://gitlab.cs.taltech.ee/iti0301-2025/example-game-workshop).
Pärast materjalidega tutvumist loo isiklik koopia ([*fork*](https://projectdoc.pages.taltech.ee/estonian/praktilised_ulesanded/fork_clone.html)) sellest repositooriumist, mille kallal saad töötada.

Täiesti korrektse ja valmis funktsionaalsustega töötoa koodi leiad [siit](https://gitlab.cs.taltech.ee/iti0301-2026/example-ai-workshop-solution). **NB! Palun tutvu enne töötoaga alustamist esialgse projektiga ja selle READMEga.**

## Materjalid

Töötoa käigus või eraldi tutvu järgnevate materjalidega:

- Tutvu kindlasti nende materjalidega.
    - [AI pathfinding ja liikumine](https://gamedevdoc.pages.taltech.ee/pathfinding/pathfinding.html)
    - [Redblobgames](https://www.redblobgames.com/pathfinding/a-star/introduction.html)
- [libGDX wiki](https://libgdx.com/wiki/) - saate otsida infot klasside, meetodite jne kohta vastavalt vajadusele
- [GameDevDoc](https://gamedevdoc.pages.taltech.ee/index.html) - keskne allikas kogu mänguprojekti juhiste ja dokumentatsiooni jaoks

## Eemaldatud funkstionaalsused

Järgnevad funktsionaalsused on mängust eemaldataud, sinu ülesanne on need tagasi lisada.

### 1. Lisa vastane mängu

Mängu käivitamisel on näha, et ekraanil ei ole ühtegi vastast. Lisa mängu vastane.

<details> 
<summary>💡 Vihje 1</summary> 

Muudatus tuleb teha **serveri klassi** `GameInstance` funktsiooni `run`.
</details>

<details> 
<summary>💡 Vihje 2</summary> 

Vastase lisamiseks on meil samas klassis juba olemas meetod `addEnemy`. Selle kasutamiseks tuleb talle anda vastase koordinaadid ning elude arv. Vastase elude määramiseks saame kasutada juba olemasolevat konstanti `ENEMY_LIVES_COUNT`.
</details> 

<details> 
<summary>🛠 Lahendus</summary> 

```java
addEnemy(100, 100, ENEMY_LIVES_COUNT);
``` 

</details>

<details> 
<summary>✅ Seletus </summary> 

Meil on juba ette tehtud funktsioon `addEnemy`, nii et tuleb ainult enne mängu algust välja kutsuda. 

**NB!** Kui kutsuda `addEnemy` välja mängutsükli sees siis lisatakse vastane iga `run` uuendusel, mida me selles olukorras ei soovi.
</details> 


### 2. Joonista vastane ekraanile

Kuigi vastane on mängu lisatud ja eksisteerib loogiliselt, ei joonistata teda hetkel ekraanile. Lisa vastase joonistamine.

**2.1 Kirjuta `renderEnemies` funktsioonisisu.**

Good to know 😎:

**SpriteBatch** on objekt, mida kasutatakse 2D piltide joonistamiseks ekraanile. See võimaldab joonistada mitu pilti ühe renderdamistsükli jooksul, mis teeb joonistamise kiiremaks ja efektiivsemaks.


<details> 
<summary>💡 Vihje 1</summary> 

Muudatus tuleb teha **kliendi klassi** `Arena`.
</details> 

<details> 
<summary>💡 Vihje 2</summary> 

Vaatame `renderEnemies` meetodi loomist. Vastaste joonistamine toimub sarnaselt `renderBullets` meetodile.

```java 
private void renderEnemies(SpriteBatch spriteBatch) {
    
}
```
</details>

<details> 
<summary>🛠 Lahendus</summary> 

`renderEnemies`:
```java
private void renderEnemies(SpriteBatch spriteBatch) {
    enemies.forEach(enemy -> spriteBatch.draw(
            Sprites.enemyTexture,
            enemy.getX(),
            enemy.getY(),
            ENEMY_WIDTH_IN_PIXELS,
            ENEMY_HEIGHT_IN_PIXELS));
    }
``` 

See funktsioon võimaldab nüüd **Enemy** joonistada. Hetkel ei kutsuta seda funktsiooni aga mitte kusagilt välja, mistõttu vastast tegelikult ei renderdata.

</details>

<details> 
<summary>✅ Seletus </summary> 

`renderEnemies` meetod vastutab kõikide mängus olevate vastaste ekraanile joonistamise eest. Selleks kasutatakse `SpriteBatch` objekti, mis võimaldab efektiivselt joonistada 2D graafikat ühe renderdamistsükli jooksul. Joonistamisel antakse kaasa vastase tekstuur (`Sprites.enemyTexture`), vastase X- ja Y-koordinaadid ning tema laius ja kõrgus pikslites.
</details> 

**2.2 Nüüd tuleb eelnevalt kirjutatud funktsioon ka välja kutsuda.**

<details>
<summary>💡 Vihje 1</summary>

Eelnevalt kirjutatud funktsiooni väljakutsumine toimub samas klassi funktsioonis `render`. Väljakutsumine toimub nagu **Player** ja **Bullet** objektidega.
</details>

<details>
<summary>🛠 Lahendus </summary>

Lisa funktsiooni sisse:
```java
renderEnemies(spriteBatch);
```
</details>

<details> 
<summary>✅ Seletus </summary> 

Kuigi kirjutasime `renderEnemies` sisu siis ei toimund selle kasutamine enne, kui see meetod kutsutakse välja. Seetõttu tuleb `renderEnemies(spriteBatch)` lisada render meetodisse, kus toimub kogu mängu joonistamine.
See tagab, et vastased joonistatakse ekraanile igas renderdamistsüklis samamoodi nagu `Player` ja `Bullet` objektid.

</details> 

### 3. Vastane tuleb nüüd liikuma panna!

Vastane ilmus nüüd ekraanile aga millegipärast ei liigu.

<details>
<summary>💡 Vihje 1</summary>

Muudatus tuleb teha **serveri klassis** `GameInstance` meetodisse `run`. Liikumise loogika toimub sarnaselt `Bullet`'iga.
</details>

<details>
<summary>💡 Vihje 2</summary>

`Enemy` klass sisaldab valmis meetodit `follow`. Rakenda see meetod nii, et iga vastane – käesolevas töötoa näites üks vastane – jälitaks mängijat.
</details>

<details>
<summary>🛠 Lahendus</summary>
Iga vastane jälitab ühte ja sama mängijat.

```java
enemies.forEach(enemy -> enemy.follow(players.get(0)));
```

Selline lahendus on kasutusel selleks, et vastane uuendaks igas mängutsüklis oma liikumist vastavalt mängus toimunud muutustele.

</details>

<details> 
<summary>✅ Seletus </summary> 

Liikumise loogika peab toimuma serveri poolel mängutsükli jooksul. Selleks lisatakse `GameInstance` klassi `run` meetodisse loogika, mis uuendab vastase asukohta igas tsüklis. `Enemy` klassis oli selleks juba valmis meetod `follow`, mis arvutab vastase liikumise mängija asukoha põhjal.

</details> 

### 4. Vastane liigub valele poole ja kohutavalt aeglaselt

Hetkel on vastase liikumisloogikas viga, mille tulemusel liigub ta mängijast täpselt teiselepoole. Samuti on vastase liikumiskiirus liiga madal, mis muudab tema käitumise ebaefektiivseks.

**4.1 Muudame vastase liikumissuunad õigeks!**

<details>
<summary>💡 Vihje 1</summary> 

Muudatus tuleb teha **serveris klassis** `Enemy`. Vigane kood on meetodis `follow`.
</details>

<details> 
<summary>💡 Vihje 2</summary> 

Praeguses seisus on vastase liikumissuund X- ja Y-teljel vastupidine ↕️↔️. Paranda see, et vastane liiguks mõlemal teljel õiges suunas mängija poole.
</details>

<details>
<summary>🛠️ Lahendus</summary> 

Parandame need nii:
```java 
if (xDistance > ALIGNMENT_THRESHOLD) {
    if (player.getX() > currentX) {
        move(Direction.RIGHT, ENEMY_SPEED);
    } else {
        move(Direction.LEFT, ENEMY_SPEED);
    }
} else {
    if (player.getY() > currentY) {
        move(Direction.UP, ENEMY_SPEED);
    } else {
        move(Direction.DOWN, ENEMY_SPEED);
    }
}
``` 
</details>

<details> 
<summary>✅ Seletus </summary> 

Varasemas versioonis oli vastase liikumisloogika vigane, sest X- ja Y-telje liikumissuunad olid omavahel segamini, mistõttu vastane liikus mängijast eemale või vales suunas. Kui X-teljel on vastane juba piisavalt joondatud, siis liigutakse Y-teljel üles või alla vastavalt mängija asukohale.

</details> 

**4.2 Nüüd teeme vastase natuke kiiremaks**

<details>
<summary>💡 Vihje 1</summary>

Muudatus tuleb teha `shared` kaustas `Constants` klassis.
</details>

<details>
<summary>💡 Vihje 2</summary>

Tuleb ära muuta konstant `ENEMY_SPEED`. Muudame selle näiteks 5 korda kiiremaks.
</details>

<details>
<summary>🛠️ Lahendus</summary>
Paneme vastase kiiruseks näiteks 0.75f.

```java
public static final float ENEMY_SPEED = 0.75f;
```
</details>

<details> 
<summary>✅ Seletus </summary> 

Vastase liikumiskiirus on määratud konstandiga `ENEMY_SPEED`, mis asub `Constants` klassis ja mida kasutatakse vastase liikumisloogikas. Muutes selle väärtust, saame mõjutada seda, kui kiiresti vastane igas mängutsüklis liigub. Kuna kiirus on defineeritud konstandina, rakendub muudatus automaatselt kõikidele vastastele kogu mängus.

</details> 

### 5. Lisame vastasele collisionid kuulidega.

Praegu ei tuvastata kokkupõrkeid ei kuulide ja vastaste ega vastase ja mängija vahel. Selle lahendamiseks tuleb implementeerida vastav funktsionaalsus ning hea tava kohaselt lisada ka logisõnumid, et hiljem oleks lihtsam süsteemi käitumist jälgida ja vigu tuvastada.
**5.1 Lisa collisionid kuulide ja vastaste vahel.**

<details>
<summary>💡 Vihje 1</summary>

Lisada tuleb funktsionaalsus serveri klassis `BulletCollisionHandler`.
</details>

<details> 
<summary>💡 Vihje 2</summary> 

Vajalik funktsionaalsus tuleb implementeerida meetodis `handleCollisions`.
Selleks, et tuvastada tabamus usaldusväärselt, tuleb kontrollida, kas vastase hitbox **sisaldab** kuuli koordinaate.

**Kui vastane on kuuli poolt tabatud, tuleb:**

- vähendada vastase elude arvu (selleks on Enemy klassis vastav meetod juba olemas);

- eemaldada kuul mängust, et vältida korduvat tabamust;

- lisada logisõnum, mis aitab arenduse ja silumise käigus paremini jälgida mängus toimuvaid sündmusi (see lihtsustab oluliselt hilisemat probleemide tuvastamist 🙃).
</details>

<details>
<summary>💡 Vihje 3</summary>

Nagu eelnevalt mainitud, on meil vajalikud meetodid ja muutujad juba olemas:

- `Enemy` klassis on meetod `decreaseLives()`, mida saame kasutada vastase elude vähendamiseks.
- Meil on muutuja `bulletsToBeRemoved`, mida kasutatakse ekraanilt välja liikunud kuulide eemaldamiseks. Sinna tuleb lisada ka kuulid, mis on vastast tabanud, et vältida korduvaid tabamusi.
- Logisõnumite jaoks saame kasutada Java meetodit `log.info()`, mis aitab tuvastada, millal Enemy on kuuliga pihta saanud.


</details>

<details>

<summary>🛠️ Lahendus</summary>

```java
if (enemyHitBox.contains(bullet.getX(), bullet.getY())) {
    enemy.decreaseLives();
    bulletsToBeRemoved.add(bullet);
    Log.info("Enemy was hit. " + enemy.getLives() + " lives left.");
}
```
</details>

<details> 
<summary>✅ Seletus </summary> 

Kuulide ja vastaste kokkupõrgete tuvastamine toimub serveri poolel, et mängu loogika oleks usaldusväärne ega sõltuks kliendi joonistamisest. Tabamuse tuvastamiseks kontrollitakse, kas vastase hitbox sisaldab kuuli koordinaate — see on kindel viis veenduda, et kuul on vastasega reaalselt kokku puutunud. Kui tabamus tuvastatakse, vähendatakse vastase elusid, kuul eemaldatakse seejärel mängust, sest vastasel juhul võiks sama kuul järgnevates mängutsüklites vastast korduvalt tabada. Samuti on hea tava lisada sellistes kohtades logisõnum, et paremini arusaada, mis serveris toimub.

</details> 

**5.2 Nüüd lisa collisionid vastase ja mängija vahel.**

<details>
<summary>💡 Vihje 1</summary>

Lisada tuleb funktsionaalsus serveri klassis `EnemyDealtDamageHandler`.
</details>

<details>
<summary>💡 Vihje 2</summary>

Vastase `enemyHitBox` on juba loodud, seega tuleb luua hitbox ka mängijale. Mängija hitboxi saad koostada klassis `HitBoxBuilder` olevate meetoditega.
</details>

<details>
<summary>💡 Vihje 3</summary>

Varasemalt tuvastasime tabamuse nii, et kontrollisime, kas vastase hitbox sisaldab kuuli koordinaate. Nüüd, kui võrdleme kahe hitboxi \(`Rectangle`\) omavahelist kattuvust, kasutame sisseehitatud meetodit `intersects()`. Samuti on siin hea logida iga kattuvus, et pärast paremini arusaada kui midagi ei toimi nagu ta peab.
</details>

<details>
<summary>🛠️ Lahendus</summary>

```java
Rectangle playerHitBox = HitBoxBuilder.constructPlayerHitBox(player);
if (playerHitBox.intersects(enemyHitBox)) {
        player.decreaseLives();
        Log.info("Player was hit. " + player.getLives() + " lives left.");
}
```
</details>

<details> 
<summary>✅ Seletus </summary> 

Seekord selle asemel, et võrrelda üksikuid koordinaate, kasutatakse hitboxe, kuna need annavad täpsema ja realistlikuma tulemuse objektide füüsilise kattuvuse tuvastamisel. Mängija hitbox luuakse eraldi klassis `HitBoxBuilder`, et kogu hitboxide loomise loogika oleks koondatud ühte kohta — kui mängija suurus või kuju muutub, tuleb muudatus teha ainult ühes kohas.

</details> 


### 6. Mängija ja vastane ei saa haiget

Hetkel vastast lastes näeme server logidest, et ta on pihta saanud, aga elusid ei kaota🤔. Tegelikult kui vastane meile vastu läheb siis ei kaota elusid meie ka. Mõlemad on `Character` alamklassid, nii et äkki võib viga seal olla.

<details>
<summary>💡 Vihje 1</summary>

Serveri klassis `Character` funktsioonis `decreaseLives` funktsionaalsus on puud. Sellepärast ei kaota nii vastane kui ka mängija elusid.
</details>

<details>
<summary>💡 Vihje 2</summary>

Soovime, et nii mängija kui ka vastane kaotaksid elusid kokkupõrgete või tabamuste korral, eeldusel, et **neil on veel elusid alles**. Kuna mänguloogika töötab tickrate’i põhiselt, ei ole soovitav, et elud väheneksid igal kaadril või tick’il järjest, sest see põhjustaks tegelase kohese hukkumise.

Selle vältimiseks kasutame tabamuste vahelist jahutusperioodi (hit cooldown). Igal tegelasel on selleks ajamärgis `lastTimeOfReceivingDamage`, samuti on meil kättesaadav `currentTime` ning konstant `HIT_COOLDOWN`.

Elude vähendamine peaks toimuma ainult juhul, kui praegune aeg miinus viimane kahjustuse saamise aeg on suurem kui määratud `HIT_COOLDOWN`. Iga kord, kui tegelane saab kahju, tuleb uuendada ka `lastTimeOfReceivingDamage`, et vältida järjestikuseid tabamusi liiga lühikese aja jooksul.
</details>

<details>
<summary>🛠️ Lahendus</summary>

```java
if (currentTime - lastTimeOfReceivingDamage > HIT_COOLDOWN) {
    if (lives > 0) {
        lives -= 1;
    }
    lastTimeOfReceivingDamage = currentTime;
}
```
</details>

<details> 
<summary>✅ Seletus </summary> 

Kuna mäng töötab väga kiirete mängutsüklitega, võib üks kokkupõrge kesta mitu tick’i järjest. Ilma lisakontrollita kaotaks tegelane elusid igas tsüklis ning sureks peaaegu kohe. Selle vältimiseks kasutatakse **hit cooldown’i**, mis lubab elusid vähendada ainult siis, kui viimasest tabamusest on möödunud piisavalt aega. Selline loogika on koondatud `Character` klassi, sest nii mängija kui ka vastane käituvad elude kaotamisel ühtemoodi.

</details> 

### 7. HUD ei uuenda vastase elusid

Pärast pikka laskmist sureb vastane ära, aga millegipärast HUD ei näita, et ta elusid oli kaotanud.

<details>
<summary>💡 Vihje 1</summary>

Muudatusi peab tegema **kliendi klassis** `Hud`.
</details>

<details>
<summary>💡 Vihje 2</summary>

Meil on olemas meetod `update`, mille ülesandeks on kogu HUD-i oleku uuendamine igal mängutsüklil. Selle meetodi sees kutsutakse välja funktsioon `updateLives`, mis vastutab hetkel mängija elude kuvamise värskendamise eest. Kuna vastased kasutavad sama elude loogikat nagu mängija, saame olemasolevat lahendust laiendada.


</details>


<details>
<summary>💡 Vihje 3</summary>

Meil on võimalik lisada `updateLives`-meetodile täiendav parameeter (nt `enemies`) ja edastada samas `update`-funktsioonis juurde ka `enemyStates` (vastaste elude/olekute loend). Kuna mängus on hetkel alati ainult üks vastane, piisab elude info saamiseks järjendi esimese elemendi kasutamisest. Lisaks on meil olemas enemyLivesLabel, mille sisu saab vastase elude info põhjal uuendada.

See võimaldab huvi korral edastada ja kuvada iga vastase elu samal põhimõttel nagu mängija omi — vältides koodi dubleerimist ja hoides loogika ühtsena.
</details>

<details>
<summary>🛠️ Lahendus</summary>

```java
public void update(GameStateMessage gameStateMessage) {
    updateLives(gameStateMessage.getPlayerStates(), gameStateMessage.getEnemyStates());
    updateTime(gameStateMessage.getGameTime());
    updateGameStatus(gameStateMessage);
}
```

```java
private void updateLives(List<PlayerState> players, List<EnemyState> enemies) {
    localPlayerLivesLabel.setText(players.get(0).getLives());
    enemyLivesLabel.setText(enemies.get(0).getLives());
}
```
</details>

<details> 
<summary>✅ Seletus </summary> 

HUD-i uuendamine toimub `update` meetodis, sest see kutsutakse välja igal mängutsüklil ja selle ülesanne on hoida ekraanil kuvatav info alati ajakohasena. Kuna nii mängijal kui ka vastasel on sama elude loogika, on mõistlik kasutada ühte ühist `updateLives` meetodit ja anda sellele vajalik info parameetritena kaasa. HUD ei arvuta ise midagi, vaid kuvab ainult serverist saadud olekut.

</details> 
