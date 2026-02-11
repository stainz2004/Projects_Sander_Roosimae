# Team Gossips game

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).


## Gameplay description

Currently, the server is configured to run only one game instance at a time, with a maximum capacity of two players. It is supposed to be a Fireboy Watergirl platformer with monster fights at the end but it is far away from that right now. Currently you can shoot at each other or a monster in our custom made worlds in TiledMap.

- On the title screen, press "Start" to begin a new game or "Exit" to close the application.
- Wait for the second player to join—the game will begin automatically.
- Both players can move using the WASD keys and shoot bullets using the arrow keys.
- Parkour up and help each other on both buttons.
- When both buttons are pressed then the AI monsters spawn
- Theres a monster both players can shoot at currently
- Each bullet hit decreases the monster’s lives by 1. The game ends when one player’s lives reach 0 or the monster dies from several hits.
- Once the game is over, you have to close the game and start again to play it.

## How to run

1. Open the project in an IDE, preferably IntelliJ. While it's possible to run the game from the command line, running multiple clients simultaneously can be tricky.
2. Launch the server by running `ServerLauncher.java` from the `server` package.
3. Launch two clients by running `Lwjgl3Launcher.java` from the `lwjgl3` package.
   - It’s recommended to start the clients by pressing the green triangle at the top of the screen in IntelliJ.
   - To avoid issues when running multiple clients, select "Current file" in the Run Configuration settings.
4. On both clients press the "Start" button to move to the lobby selection screen
5. Join the same lobby on both clients by clicking on the same lobby name
6. Choose a character on both clients by pressing on them
7. Press "Start Game" on both clients
8. Start playing!
9. For correct animations please put the monitor on 60 Hz!!

## Creators of the game

- Getter-Sandra Lipp
- Sander Roosimäe
- Markus Käpp

## Technologies used

- Java
- LibGDX
- Kryonet
- Gradle
- Lombok

## Features

- Multiplayer - can be played with up to 2 people
- Game menu - Start and exit button with lobby screen
- Shooting - you can shoot up, down, left and right
- Jumping and movement functionality
- AI enemy - enemy chases you and kills you if he hits you

## Platforms / main modules

 - core: Main module with the application logic shared by all platforms. Contains mainly client-side logic.
 - lwjgl3: Primary desktop platform using LWJGL3; was called 'desktop' in older docs. For running client side of the game.
 - server: A separate application without access to the core module.
 - shared: A common module shared by core (client) and server platforms. Contains shared constants, messages, etc.
