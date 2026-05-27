# Calorie Adventure

Welcome to **Calorie Adventure**—a high-fidelity, polished, 2D retro pixel-art game built entirely in vanilla Java (Swing/Java2D)! 

You control a cute, chubby cat on a mission to eat healthy, get fit, and avoid the temptations of falling junk food. The game features smooth physics-based lane transitions, retro starry backdrops, dynamic collision particles, and a progressive difficulty engine that speeds up as you score higher!

---

## Gameplay & Mechanics

### 1. The Goal
Help our tabby cat eat the healthiest foods possible to raise your score while avoiding high-calorie junk foods. Catch power-up dumbbells to activate shields and boost your score!

### 2. Smooth Movement
Movement is key! Instead of snapping instantly between lanes, the cat slides fluidly using **linear interpolation (lerp)**, tilting dynamically in the direction of travel with a cute vertical bobbing animation.

### 3. Retro 3-Life System
- You start with **3 Lives**, shown as beautiful pixel-art red hearts in the top-left HUD.
- Hitting a piece of junk food subtracts one heart.
- After taking damage, you get a brief **blinking recovery window (1.2s)** of invincibility to safely reposition!
- The game only ends when all 3 hearts are lost.

### 4. Power-Up: Dumbbells & Gold Shields
- Colliding with a falling dumbbell grants a direct **+50 point boost** and activates a **double points multiplier** for subsequent catches.
- It grants **complete invincibility against all junk food for 15 seconds**!
- A glowing golden dashed energy ring rotates around the cat to indicate the active shield. Hitting junk food safely defleshes it with gold particle bursts and awards a +5 bonus!
- A timer progress bar in the HUD keeps track of the remaining duration of your shield.

### 5. Progressive Difficulty Scaling
The longer you survive and the more you catch, the harder the game gets:
- **Speed Increase**: The falling speed of all items dynamically scales using: `speedMultiplier = 1.0 + (score / 150)`.
- **Spawn Frequency**: The delay between item spawns shortens progressively from 2.0 seconds down to a rapid 0.6 seconds!

---

## Food Varieties

Every single falling item is procedurally rendered using high-fidelity pixel-art drawing algorithms:

| Healthy Foods (+10 Pts) | Junk Foods (-1 Life) |
| :--- | :--- |
| 🍎 **Apple**: Red orb with green leaf & outline | 🍔 **Burger**: Layered gold bun, green lettuce & cheese |
| 🥦 **Broccoli**: Dark green fluffy crown & stem | 🍕 **Pizza**: Triangular cheese slice with pepperoni |
| 🥕 **Carrot**: Diagonal orange root with green leaves | 🍩 **Donut**: Pink frosting ring with multi-color sprinkles |
| 🍌 **Banana**: Curved crescent with dark tips | 🍟 **Fries**: Golden sticks sticking out of red box |
| 🫐 **Blueberries**: Clustered blue berries with highlights | 🧁 **Cupcake**: Pink frosting dome on brown wrapper |
| 🥗 **Salad**: Healthy leafy greens inside slate bowl | 🍫 **Chocolate**: Exposed squares on silver/red wrapper |

---

## User Interface & Welcoming Screens

- **Gradient Start Screen**: Beautiful logo **CALORIE ADVENTURE** changing color hues dynamically with floating silhouette backdrops.
- **Hall of Heroes (High Scores)**: A sleek top score overlay panel that highlights exactly where your name sits, displaying a pulsing neon marker and `◄ YOU!` indicator showing your real-time calculated rank!
- **How to Play Tab**: An integrated instructions board explaining rules, lives, controls, and dumbbell powers.
- **QUIT GAME Option**: Allows players to exit the game cleanly and quickly.

---

## Controls

- **Slide Left**: `A` or `Left Arrow`
- **Slide Right**: `D` or `Right Arrow`
- **Select Option / Close Menu Tabs**: `Enter` / `Space` / `Mouse Click`
- **Return to Main Menu**: `ESC`

---

## How to Compile & Run

### Prerequisites
Make sure you have Java JDK (version 8 or higher, JDK 23 recommended) installed.

### Compilation
Open your terminal inside the project directory and run:
```bash
javac -d bin src/*.java
```

### Run
Launch the start menu welcome screen by executing:
```bash
java -cp bin Main
```

---

## Project Structure

```
├── README.md               # Game Documentation
├── highscores.txt          # Saved Player score registry
├── src/
│   ├── Main.java           # Entry point (Launches Welcome screen)
│   ├── MenuWindow.java     # Welcome menu, Instructions, & Hall of Heroes Overlay
│   ├── GameWindow.java     # Game frame lifecycle manager
│   ├── GamePanel.java      # Gameplay loop, HUD, Particles, Physics & Rendering
│   ├── Player.java         # Cat physics, Lerped sliding & Cat drawing fallback
│   ├── GameObject.java     # Base abstract falling game object
│   ├── HealthyFood.java    # Procedural 6 healthy food pixel drawings
│   ├── UnhealthyFood.java  # Procedural 6 junk food pixel drawings
│   ├── Dumbbell.java       # Power-up plate designs, gold sparks & gravity
│   └── SoundPlayer.java    # Optional audio clips manager
└── bin/                    # Compiled JVM bytecode binaries (.class files)
```

Enjoy playing **Calorie Adventure**! Raise your score, dodge the junk, and help Catto lead a healthy, fit lifestyle!
