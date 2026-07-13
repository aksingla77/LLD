# Snake & Ladder — Low-Level Design

> **Prerequisites:** SOLID Principles, UML Class Diagrams, **Strategy Pattern**, Inheritance & Polymorphism, Enums (awareness)

---

## How We Solve LLD Problems — Quick Recap

Same 5-step framework as the Parking Lot, Rate Limiter, and ATM designs:

| Step   | What                                     | Time in Interview |
| ------ | ---------------------------------------- | ----------------- |
| Step 1 | Clarify Requirements                     | ~5 min            |
| Step 2 | Identify Core Entities (nouns → classes) | ~5 min            |
| Step 3 | Define Attributes, Methods, Enums        | ~5 min            |
| Step 4 | Draw Relationships (UML Class Diagram)   | ~5-10 min         |
| Step 5 | Write Code (class by class)              | ~15-20 min        |

**The design sharpens in three passes** — the single most useful thing to internalize about LLD:

1. **First pass (whiteboard nouns):** you guess attributes and methods per class. Rough, incomplete, some misplaced.
2. **UML pass:** drawing _relationships_ exposes gaps — "who holds whom?", "who calls this?", "where does this data live?"
3. **Code pass:** actually typing it forces the last 20% of correctness — ordering of operations, overshoot handling, who mutates what.

You are _not_ supposed to get it perfect on pass 1. The skill is letting each pass correct the previous one. The companion Excalidraw file (`SnakeAndLadder.excalidraw`) walks through exactly these three passes.

---

## ⭐ The Golden Rule: Stay Confined (Scope Discipline)

> **This is the section that separates a 6/10 from a 9/10 interview.**

Snake & Ladder looks trivial, so candidates over-build: animated boards, GUI, networked multiplayer, persistent leaderboards. Then they run out of time with no working core.

**Your job is to build the _smallest correct model that satisfies the stated requirements_ — and nothing more.**

| Temptation (rabbit hole)                        | Disciplined response                                                               |
| ----------------------------------------------- | ---------------------------------------------------------------------------------- |
| "Let me model a 2-D grid of `Cell` objects."    | No — a 1-D position 1..N is enough. The snakes/ladders are just a `pos → pos` map. |
| "I'll add a `Snake` and `Ladder` with if-else." | Model the shared idea (`CellEffect`) once; subclass for behavior. (Strategy.)      |
| "Let me support GUI / animation / networking."  | Out of scope. Console output proves the logic.                                     |
| "Add save/resume, leaderboards, undo…"          | Park it. Name it as an extension, build the core first.                            |

> **Rule of thumb:** Every class you add must trace back to a **written requirement**. If you can't point to the requirement it serves, delete it.

---

## Step 1: Clarify Requirements

Walk the **happy path out loud** — it becomes your game loop later.

| Question                            | Answer (for this demo)                                    |
| ----------------------------------- | --------------------------------------------------------- |
| Board size?                         | Configurable `N` cells (e.g. 100), positions `0 … N`      |
| How many players?                   | 2 or more, take turns in order                            |
| How does a turn work?               | Roll a die (1-6), move forward that many cells            |
| What are snakes / ladders?          | A jump from one cell to another (down / up)               |
| Any other cell effects?             | Yes — a **Bomb** sends you back to start (extensible)     |
| What happens on overshoot past `N`? | Player stays put; must roll the **exact** number to win   |
| Win condition?                      | First player to land **exactly** on cell `N`              |
| Do snakes/ladders chain?            | Yes — landing on a jump's destination can trigger another |

**Final extracted requirements:**

1. A board of `N` cells; players start at position 0.
2. A fair die (1-6).
3. Multiple players take turns in a fixed rotation.
4. Cells may carry an **effect** — snake (down), ladder (up), bomb (back to start).
5. Overshooting `N` is not allowed — the player stays put.
6. First to land exactly on `N` wins; the game ends.
7. Effects can chain (a jump landing on another jump fires it too).

**Out of scope (say 2-3, then stop):**

- GUI / animation / a real 2-D board rendering.
- Networked / persistent multiplayer, save-resume.
- Weighted dice, power-ups, betting.

---

## Step 2: Identify Core Entities

Nouns in the requirements become candidate classes:

- **Board** — owns `N` and the map of cell → effect; moves a player.
- **Player** — a name and a current position.
- **Dice** — produces a random roll 1-6.
- **CellEffect** — the abstract idea of "something happens on this cell." (**Strategy Pattern**)
- **Snake / Ladder / Bomb** — concrete effects (subclasses of `CellEffect`).
- **Game** — the coordinator: holds players, board, dice; runs the turn loop.

> **Why not model each `Cell` as an object?** Tempting, but a cell is just an index. A `HashMap<Integer, CellEffect>` (only the _interesting_ cells) is far leaner than `N` cell objects, 90% of which are empty.

---

## Step 3: Define Attributes, Methods, Enums — Making Design Decisions

We go **bottom-up**: smallest building blocks first, then compose upward.

### 3.1 Dice — trivial, but keep it a class

```java
public class Dice {
    public int roll() { return (int)(Math.random() * 6) + 1; }
}
```

> **Why a class for one line?** So the roll logic is injectable and swappable — a fixed/loaded die for testing, or multiple dice later. This is dependency injection at its simplest: `Game` receives a `Dice`, it doesn't hard-code `Math.random()`.

### 3.2 Player — data only

```java
public class Player {
    public String name;
    public int currPos;          // starts at 0
    public void setPos(int newPos) { this.currPos = newPos; }
}
```

The player is a **data holder**. It does not know about the board or the rules — those live in `Board`/`Game`.

### 3.3 CellEffect — the heart of the design (Strategy Pattern)

**Decision fork — how do we model snakes, ladders, bombs?**

- **Tempting (junior):** one `Cell` class with a `type` field and a big `if/else` (`if SNAKE … else if LADDER … else if BOMB …`) inside `apply()`. Every new effect edits that method — an [O(effects)] tangle that violates **Open/Closed**.
- **Better (Strategy):** an abstract `CellEffect` with an `apply(Player)` method; each effect is its **own class**. A new effect = a new class, zero edits to existing code.

```java
public abstract class CellEffect {
    public int start;                 // the cell this effect sits on
    public int end;                   // where it sends the player
    public CellEffect(int start, int end) { this.start = start; this.end = end; }
    public void apply(Player p) { }   // overridden by each concrete effect
}
```

```java
public class Snake  extends CellEffect {           // start > end (go down)
    public Snake(int start, int end)  { super(start, end); }
    @Override public void apply(Player p) { p.setPos(end); }
}
public class Ladder extends CellEffect {           // start < end (go up)
    public Ladder(int start, int end) { super(start, end); }
    @Override public void apply(Player p) { p.setPos(end); }
}
public class Bomb   extends CellEffect {           // always back to 0
    public Bomb(int start) { super(start, 0); }
    @Override public void apply(Player p) { p.setPos(end); }
}
```

**Decision fork — does `apply` take the position, or the whole `Player`?**

- **`int apply(int pos)`** — the effect is a pure function `pos → pos`; the caller updates the player. Easiest to test, no side effects.
- **`void apply(Player p)`** (chosen here) — the effect mutates the player directly. More flexible for effects that touch _more_ than position later (skip-a-turn, lose-a-life).

> **Key subtlety — never store the `Player` as a field on `CellEffect`.** A cell effect belongs to a _cell_, not a player: cell 17's snake bites _everyone_. The player is passed **as a method parameter** each time the effect fires, not injected in the constructor. Intrinsic data (start/end) → constructor field; per-invocation data (the player) → method parameter.

### 3.4 Board — owns cells and movement

```java
public class Board {
    int n;
    HashMap<Integer, CellEffect> cellEffects;

    public Board(int n) { this.n = n; this.cellEffects = new HashMap<>(); }
    public void addEffect(CellEffect effect) { cellEffects.put(effect.start, effect); }
    public int  getWinningPos() { return n; }
    // movePlayer — see Step 5
}
```

> **Two bugs a first pass always ships here:** (1) forgetting to `new HashMap<>()` → `NullPointerException`; (2) making the winning position `n*n` instead of `n`. Both are caught the instant you _run_ it — which is why a runnable `main` is part of the deliverable.

---

## Step 4: Draw Relationships (UML) — Where Pass-1 Gets Corrected

```
Game ──has-a──> List<Player>
Game ──has-a──> Board
Game ──has-a──> Dice
Game ──tracks──> currentPlayerIndex, gameOver

Board ──has──> HashMap<Integer, CellEffect>

CellEffect (abstract) <|-- Snake, Ladder, Bomb      // Strategy Pattern
CellEffect.apply(Player)  ──mutates──> Player.currPos
```

**The questions UML exposes (and pass-1 always misses):**

1. **"Who drives the turn rotation?"** Not `Main`, not the `Player`. The `Game` holds the player list, so _it_ owns whose turn it is — via a rotating index: `currentPlayerIndex = (currentPlayerIndex + 1) % players.size()`.
2. **"Who applies the effect after a move?"** The `Board.movePlayer` — because the board owns the effect map. `Game` just says "move this player N steps"; the board handles landing on a snake/ladder/bomb.
3. **"Where does the overshoot rule live?"** In `Board.movePlayer` — it's a _movement_ rule, so it belongs with movement, not in `Game`.

---

## Step 5: Write Code — The Last 20% of Correctness

### 5.1 Board.movePlayer — overshoot + effect chaining

```java
public void movePlayer(Player p, int steps) {
    int newPos = p.currPos + steps;
    if (newPos > n) {                                   // 1. overshoot: stay put
        System.out.println("  overshoot: needs exactly "
            + (n - p.currPos) + ", stays at " + p.currPos);
        return;
    }
    p.setPos(newPos);
    while (cellEffects.containsKey(newPos)) {           // 2. apply effect(s), chained
        cellEffects.get(newPos).apply(p);
        if (newPos == p.currPos) break;                 //    no move? stop (avoid infinite loop)
        newPos = p.currPos;                             //    landed elsewhere → check again
    }
}
```

> **Why the `while` loop and the `break`?** A ladder can dump you onto a snake's mouth. The loop lets effects chain. The `break` guards against an effect that maps a cell to _itself_ (or a bomb sending you to 0 where nothing waits) — without it you'd spin forever.

### 5.2 Game — the turn loop

```java
public void play() {
    while (!gameOver) playTurn();
}

public void playTurn() {
    Player player = players.get(currentPlayerIndex);     // whose turn
    int steps = dice.roll();
    board.movePlayer(player, steps);                     // move + effects
    System.out.println(player.name + " rolled " + steps + " -> pos " + player.currPos);

    if (player.currPos == board.getWinningPos()) {       // exact-landing win
        System.out.println(player.name + " wins!");
        gameOver = true;
        return;
    }
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();   // rotate
}
```

> **Win check — `Board` or `Game`?** Either is defensible. Here `Game` asks `board.getWinningPos()` and compares — the board owns the _number_, the game owns the _rule_. Keeps the magic number (`n`) inside `Board`.

### 5.3 Main — setup + demo (a first-class artifact)

`Main` only wires things together and kicks off the game — it knows nothing about _how_ a turn works.

```java
Board board = new Board(100);
Dice  dice  = new Dice();

board.addEffect(new Snake(17, 6));   board.addEffect(new Ladder(3, 22));
board.addEffect(new Snake(54, 34));  board.addEffect(new Ladder(11, 40));
// … more snakes / ladders …
board.addEffect(new Bomb(37));       // back to 0

Game game = new Game(board, dice);
game.addPlayer(new Player("Akshit"));
game.addPlayer(new Player("Tiya"));
game.play();
```

> **Convention that prevents bugs:** snakes have `start > end` (down), ladders `start < end` (up). A `Ladder(0, 0)` or a jump onto cell 0 is a foot-gun — it does nothing and can loop. Keep effects meaningful.

---

## Running It

Use the VS Code **Run | Debug** CodeLens above `main` (the Java extension auto-compiles), or from a terminal:

```powershell
cd "<repo root: the LLD folder>"
javac -d out (Get-ChildItem -Recurse -Filter *.java -Path DesignProblems\SnakeAndLadder).FullName
java -cp out DesignProblems.SnakeAndLadder.Main
```

Sample output:

```
Akshit rolled 3 -> pos 22
  ...
Tiya rolled 5 -> pos 40
Akshit rolled 6 -> pos 37 (bomb) -> 0
  overshoot: needs exactly 2, stays at 98
Tiya wins!
```

> `-d out` keeps compiled `.class` files in an `out/` folder instead of polluting the source tree. Add `out/` and `*.class` to `.gitignore`.

---

## Design Patterns Used

| Pattern      | Where                              | Why                                                                          |
| ------------ | ---------------------------------- | ---------------------------------------------------------------------------- |
| **Strategy** | `CellEffect` + `Snake/Ladder/Bomb` | Each effect is a class; adding one is O(1) and edits nothing existing (OCP). |
| **(DI)**     | `Game` receives `Board` and `Dice` | Swap in a loaded die / different board without touching `Game`.              |

---

## Extension Points (name them, don't build them)

- **New effects:** `Teleport`, `SkipTurn`, `MineField` — each is one new `CellEffect` subclass.
- **`apply(Player)` already supports non-positional effects** (skip-a-turn needs a `skipTurns` field on `Player`).
- **Dice variants:** a `Dice` interface with `NormalDice` / `CrookedDice`.
- **Win rules:** exact-landing vs. "reach or exceed" — swap the comparison in `getWinningPos` usage.
