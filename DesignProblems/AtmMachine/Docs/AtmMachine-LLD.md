# ATM Machine — Low-Level Design

> **Prerequisites:** SOLID Principles, UML Class Diagrams, **State Pattern**, Strategy Pattern (awareness), Enums

---

## How We Solve LLD Problems — Quick Recap

Same 5-step framework as the Parking Lot and Rate Limiter designs:

| Step   | What                                     | Time in Interview |
| ------ | ---------------------------------------- | ----------------- |
| Step 1 | Clarify Requirements                     | ~5 min            |
| Step 2 | Identify Core Entities (nouns → classes) | ~5 min            |
| Step 3 | Define Attributes, Methods, Enums        | ~5 min            |
| Step 4 | Draw Relationships (UML Class Diagram)   | ~5-10 min         |
| Step 5 | Write Code (class by class)              | ~15-20 min        |

**The design sharpens in three passes.** This is the single most useful thing to internalize about LLD:

1. **First pass (whiteboard nouns):** you guess attributes and methods per class. Rough, incomplete, some misplaced.
2. **UML pass:** drawing _relationships_ exposes gaps — "who holds whom?", "who calls this?", "where does this data live?"
3. **Code pass:** actually typing it forces the last 20% of correctness — ordering of operations, null handling, who mutates what.

You are _not_ supposed to get it perfect on pass 1. The skill is letting each pass correct the previous one. The companion Excalidraw file (`AtmMachine.excalidraw`) walks through exactly these three passes.

---

## ⭐ Machine Coding Rounds: Code Is the Deliverable (UML Gets Cut)

> **The three passes above assume a whiteboard interview. In a _machine coding_ round, the rules change — and this is the reality most people are underprepared for.**

In a machine coding round you are typically given **60–120 minutes** and asked to produce **working, runnable, extensible code**. Crucially, you are often **told to skip the formal UML diagram** (or you simply won't have time for one). The evaluator runs your code and reads it — that's the score. A beautiful class diagram earns nothing if `main` doesn't run.

**So where does the "UML pass" go? Into your head.** The relationship/ownership/stateful decisions that a diagram _forced you to confront_ (Pass 2) still have to be answered — you just answer them **mentally, on the rough sheet, while writing code**. Nothing about the thinking disappears; only the artifact does. This means:

- **The rough pass now carries the weight of two passes.** All the "who holds whom / who's stateful / who mutates" work must surface _without_ the diagram prompting it.
- **You can't rely on the diagram to catch your gaps.** You have to have the checklist _internalized_ so the gaps surface from memory.
- **Your `main`/demo is part of the deliverable.** It proves the design works end-to-end — treat it as a first-class artifact, not an afterthought.

### The Rough-Pass Checklist (the questions UML _would_ have forced)

Before you type a class, answer these on your rough sheet in a few words each. This is the diagram, compressed into a mental habit:

| #   | Question (ask it for every class)                        | ATM example answer                                 |
| --- | -------------------------------------------------------- | -------------------------------------------------- |
| 1   | **Ownership** — who holds an instance of whom?           | `ATM` holds inventory, bankingService, state, card |
| 2   | **Back-references** — does the child need the parent?    | each state needs `ATM` (constructor injection)     |
| 3   | **Who is stateful?** where does session data live?       | `ATM` (context); states are behavior-only          |
| 4   | **Who mutates what?** where's the point of no return?    | `BankingService` debits; `AtmInventory` dispenses  |
| 5   | **Operation ordering** — reversible before irreversible? | cash-check → debit → dispense                      |
| 6   | **Extension points** — what's likely to change?          | dispensing policy (Strategy later), new states     |

If you can answer these six from memory, you don't _need_ the diagram — you've absorbed what it was going to tell you.

### How To Get Better At Coding Straight From the Rough Pass

This is a trainable skill, not talent. Deliberate practice:

1. **Skeleton-first, then fill.** Write all interfaces + class names + method signatures so it _compiles_ (empty bodies / `throw new UnsupportedOperationException()`), then implement. This front-loads the Pass-2 relationship decisions into 10 minutes of typing instead of a diagram.
2. **Reverse-check against the 6-question checklist** after every problem. Where did you get ownership or ordering wrong? That's the muscle to train.
3. **Time-box ruthlessly.** Practice with a 90-minute timer. The constraint _is_ the skill — it forces you to spend design time only where it changes the code.
4. **Redo the same problem 2–3 times.** The second attempt is where you internalize the pattern (State here) so it comes out as reflex, not deliberation.
5. **Build a personal pattern library.** Once "context stateful / states stateless / constructor-inject the context" is reflex, State-based problems (ATM, vending machine, elevator, order lifecycle) all collapse to the same 15 minutes.
6. **Always write a runnable `main` that demonstrates the happy path + one failure path.** If it runs, you've _proven_ the relationships are wired correctly — which is exactly what the diagram was supposed to verify.

> **Rule of thumb:** In machine coding, _compiling, runnable, well-separated code_ beats a perfect diagram every time. Get to a compiling skeleton fast, then flesh it out — never leave the whole thing uncompiled until the final minute.

---

## ⭐ The Golden Rule: Stay Confined (Scope Discipline)

> **This is the section that separates a 6/10 from a 9/10 interview — read it twice.**

LLD interviews are 45 minutes. Candidates fail not because they can't design, but because they **wander**. They start modeling the physical keypad, the receipt printer, the network protocol to the bank, encryption of the PIN, multi-currency support… and run out of time with a half-built core.

**Your job is to build the _smallest correct model that satisfies the stated requirements_ — and nothing more.** Here's how to stay on the rails:

| Temptation (rabbit hole)                              | Disciplined response                                                                      |
| ----------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| "Should I model `CardReader`, `Keypad`, `Screen`?"    | No — unless the interviewer asks for hardware. Model _behavior_, not physical parts.      |
| "Let me add a `Transaction` class hierarchy."         | Only if ≥3 operations _and_ they'll grow. For 3 fixed ops, methods on the state are fine. |
| "Should I use a real DB / persistence layer?"         | No — a `BankingService` with in-memory maps is the agreed 'fake backend'.                 |
| "Let me handle multi-currency, cheque deposits, etc." | Out of scope unless stated. Say it out loud, then move on.                                |
| "I'll build a full Strategy for note dispensing."     | Start with one greedy method. Mention Strategy as a _future_ extension, don't build it.   |

**Three phrases that keep you confined (use them):**

1. _"I'll assume X for now — is that a fair scope?"_ → converts a rabbit hole into a 5-second clarification.
2. _"That's a valid extension; I'll note it and keep the core first."_ → parks gold-plating without dismissing it.
3. _"For these requirements, a simpler approach is enough; here's when I'd upgrade it."_ → shows you know the pattern **and** know restraint. This scores the most points.

> **Rule of thumb:** Every class you add must trace back to a **written requirement**. If you can't point to the requirement it serves, delete it. Design is as much about what you _leave out_ as what you put in.

---

## Step 1: Clarify Requirements

Start by walking the **happy path out loud** — it doubles as your state machine later.

| Question                              | Answer (for this demo)                             |
| ------------------------------------- | -------------------------------------------------- |
| What operations must the ATM support? | Check balance, withdraw, deposit                   |
| How does a session start?             | Insert card → validate → enter PIN                 |
| PIN retries?                          | Max 3 attempts, then card is retained/ejected      |
| Can the user cancel?                  | Yes, at any point (eject card)                     |
| Does the ATM hold physical cash?      | Yes — an inventory of notes (100/200/500)          |
| When can cash be withdrawn?           | Account has balance **AND** ATM can make the notes |
| Does the ATM talk to a real bank?     | No — a `BankingService` stands in as the backend   |

**Final extracted requirements:**

1. Insert a card and validate it.
2. Enter PIN (max 3 attempts).
3. Operations: check balance, withdraw, deposit.
4. After an operation: do another, or take the card back.
5. Cancellation at any point.
6. A withdrawal limit (per transaction).
7. Cash is dispensed only if **the account has funds** and **the ATM has the notes**.
8. ATM holds an inventory of notes in denominations 100 / 200 / 500.

**Out of scope (say 2-3, then stop):**

- Physical hardware (card reader, keypad, printer, screen).
- Real banking backend / persistence / network.
- Multi-currency, cheque deposits, fund transfers.

> Don't list ten out-of-scope items. Name the 2-3 that are _ambiguous enough to derail you_ if left unclarified — hardware and the "real bank" are the classic ATM traps.

---

## Step 2: Identify Core Entities

Nouns in the requirements become candidate classes:

- **Card** — identifies the customer (just a number; no PIN, no balance on it).
- **Account** — holds the balance; belongs to the bank, not the card.
- **BankingService** — the fake backend: validates cards/PINs, moves money.
- **AtmInventory** — the physical cash: a map of denomination → count.
- **Denomination** — the fixed note values (enum).
- **ATM** — the machine itself; the _coordinator_ / context.

And from the _behavior_ side (the happy path is a sequence of gated steps):

- **ATMState** — the current phase of the session (Idle, CardInserted, PinEntered…). This is the **State Pattern**.

> **Why is a card just a number?** A tempting first pass puts `pin` and `balance` on `Card`. But in reality a card is only an _identifier_ — the PIN and balance live on the **bank's** side. Putting secrets on the card object is both unrealistic and a security smell. This is a pass-1 mistake that the UML pass (Step 4) naturally corrects.

---

## Step 3: Define Attributes, Methods, Enums — Making Design Decisions

We go **bottom-up**: smallest building blocks first, then compose upward.

### 3.1 Denomination — Enum over raw ints

**Option 1 (Tempting):** use raw `int` values `100, 200, 500` everywhere.
**Option 2 (Better):** an enum — makes illegal values unrepresentable and gives each note a value.

```java
public enum Denomination {
    HUNDRED(100), TWO_HUNDRED(200), FIVE_HUNDRED(500);
    private final int value;
    Denomination(int value) { this.value = value; }
    public int getValue() { return value; }
}
```

> **Rule of thumb:** When a field has a small, fixed set of valid values, use an enum. It turns a runtime bug ("someone passed 250") into a compile-time impossibility.

### 3.2 Card & Account — Who owns the balance?

```java
class Card    { int cardNumber; }
class Account { int accountNumber; int balance; getBalance(); deposit(amt); withdraw(amt); }
```

**Decision fork — where does `withdraw()` logic live?**

- **Tempting:** ATM reaches into `account.withdraw()` directly.
- **Better:** ATM asks a **`BankingService`**. The ATM should never contain banking rules — it's a coordinator, not a bank. This keeps the ATM ignorant of _how_ money moves (SRP).

### 3.3 BankingService — the fake backend

```java
class BankingService {
    Map<Card, Account> cardAccountMap;
    Map<Card, Integer> cardPinMap;
    boolean validateCard(Card);
    boolean validatePin(Card, int pin);
    void    withdraw(Card, int amount);   // delegates to Account
    void    deposit(Card, int amount);
    int     getBalance(Card);
}
```

This is the boundary between "the machine" and "the bank." In-memory maps stand in for a database — **do not** build a real persistence layer.

### 3.4 AtmInventory — and the note-dispensing algorithm

The inventory owns the note map, so _it_ answers "can I make this amount?" and "dispense it" — not the ATM.

**The algorithm: greedy, largest-denomination-first**, done in **two phases**:

- **Phase 1 — `computePlan(amount)`:** figure out the note breakdown **without mutating**. Returns `null` if the exact amount can't be made.
- **Phase 2 — `dispenseNotes(amount)`:** recompute, throw if impossible, and _only then_ subtract from the map.

```java
public boolean canDispense(int amount) { return computePlan(amount) != null; }

public Map<Denomination,Integer> dispenseNotes(int amount) throws Exception {
    Map<Denomination,Integer> plan = computePlan(amount);
    if (plan == null) throw new Exception("Cannot dispense amount: " + amount);
    for (var e : plan.entrySet())
        inventory.put(e.getKey(), inventory.get(e.getKey()) - e.getValue());
    return plan;
}
```

> **Why two phases?** If you decrement the real map _while_ computing and then discover you can't finish, you've corrupted the inventory. Compute a plan first; commit only on success. This "validate, then mutate" pattern shows up everywhere in good design.

**Decision fork — do we need a `DispenseStrategy` (Strategy Pattern)?**

- **No — not for these requirements.** One rule (greedy) → one method.
- You'd extract a `DispenseStrategy` interface _only_ if you needed multiple interchangeable policies (minimize notes, spread denominations, oldest-stock-first). Building it now is over-engineering.
- **Interview line:** _"I'll keep dispensing as a single greedy method; if we needed multiple policies I'd extract a `DispenseStrategy` and inject it — but that'd be premature here."_

> **Greedy caveat to state aloud:** greedy isn't always correct with limited stock (e.g. need 300 but only a 200 and no 100s). For 100/200/500 with amounts in multiples of 100 it's safe _as long as enough 100s exist_. Naming this assumption earns credibility.

### 3.5 ATMState — the State Pattern

This is the heart of the problem.

**Decision fork — how do we model the phases (Idle → CardInserted → PinEntered)?**

- **Tempting (junior):** one giant `enum State` + a `switch`/`if-else` in every ATM method. Every method has to check "what state am I in?" and every new state edits every method. It's an [O(states × operations)] tangle.
- **Better (State Pattern):** each phase is its **own class** implementing a common interface. An operation invalid in the current phase simply rejects. Adding a state = adding a class, touching nothing else (Open/Closed).

```java
interface IAtmState {
    void insertCard(Card card);
    void enterPin(int pin);
    void checkBalance();
    void withdraw(int amount);
    void deposit(int amount);
    void ejectCard();
}
```

**Kill boilerplate with an abstract base.** Most operations are invalid in most states. An abstract base defaults every method to "reject," so each concrete state overrides _only_ what it allows:

```java
abstract class AAtmState implements IAtmState {
    protected Atm atm;                       // back-reference to the context
    AAtmState(Atm atm) { this.atm = atm; }
    private void reject(String m) { System.out.println(m); }
    public void insertCard(Card c){ reject("not allowed at this stage"); }
    public void enterPin(int p)   { reject("not allowed at this stage"); }
    // ...all default to reject
}
```

---

## Step 4: Draw Relationships (UML) — Where Pass-1 Gets Corrected

Drawing the arrows forces the questions code will otherwise punish you for:

```
ATM ──has-a──> AtmInventory
ATM ──has-a──> BankingService
ATM ──has-a──> IAtmState (current)     // the state can change at runtime
ATM ──has-a──> Card (current session)

IAtmState <|-- AAtmState (abstract) <|-- IdleState, CardInsertedState, PinEnteredState
AAtmState ──back-reference──> ATM      // ⭐ the crucial arrow pass-1 misses

BankingService ──has──> Card, Account
```

**The two questions UML exposes (and pass-1 always misses):**

1. **"How does a state _do_ anything?"** A state must reach the inventory, the bank, and trigger the next transition. → Every concrete state needs a **back-reference to the `ATM`** (injected via constructor). Without drawing the arrow, you don't notice this until the code won't compile.

2. **"Who is stateful — the states or the context?"**
   - The **context (`ATM`) is stateful.** It holds `currentCard`, `pinAttempts`, `currentState`, `inventory`, `bankingService` — data that must **survive** a transition.
   - The **state objects are (ideally) stateless** — they hold only the back-reference and encapsulate _behavior_. Because a state object is swapped out and discarded on transition, any data stored inside it would be lost. So session data can never live in a state.

   > **Rule of thumb:** _States encapsulate behavior; the context encapsulates data._ If a piece of data must outlive a transition (like the PIN-attempt counter), it belongs on the context.

---

## Step 5: Write Code — The Last 20% of Correctness

### 5.1 ATM — a pure coordinator

The user talks to the `ATM`, never to a state. Every ATM operation is a **one-line delegation**:

```java
class Atm {
    Card currentCard;
    AtmInventory inventory;
    IAtmState currentState;
    BankingService bankingService;

    void setState(IAtmState s) { this.currentState = s; }

    void insertCard(Card c) { currentState.insertCard(c); }
    void enterPin(int pin)  { currentState.enterPin(pin); }
    void withdraw(int amt)  { currentState.withdraw(amt); }
    // deposit, checkBalance, ejectCard … all delegate

    void addCard(Card c)  { currentCard = c; }
    void resetCard()      { currentCard = null; }   // context clears its own data
}
```

> **Who transitions?** The **current state** decides the next state (`atm.setState(new PinEnteredState(atm))`), not the ATM. This keeps the ATM trivial and the transition logic co-located with the phase that knows about it.

> **Who clears session data on eject/cancel?** The **state decides _when_** (an ejected card ends the session), but the **context owns _what_ to clear** — expose `atm.resetCard()` (and later `resetSession()`), and have the state call it. If you later add `selectedAccount`, you update _one_ method instead of every terminal state.

### 5.2 The states

```java
class IdleState extends AAtmState {
    void insertCard(Card card) {
        atm.addCard(card);
        if (atm.bankingService.validateCard(card))
            atm.setState(new CardInsertedState(atm));   // valid → await PIN
        else { atm.resetCard(); atm.setState(new IdleState(atm)); }
    }
}

class CardInsertedState extends AAtmState {
    void enterPin(int pin) {
        if (atm.bankingService.validatePin(atm.currentCard, pin))
            atm.setState(new PinEnteredState(atm));
        else System.out.println("Invalid pin - please try again");  // ← attempt counter goes here
    }
    void ejectCard() { atm.resetCard(); atm.setState(new IdleState(atm)); }
}
```

### 5.3 Withdraw — the operation-ordering trap

This is where code exposes what a diagram can't. Naive withdraw debits the account and forgets the cash — the customer loses money with no notes out. The fix is a **guarded ordering**:

```java
void withdraw(int amount) {
    if (!atm.inventory.canDispense(amount)) {          // 1. read-only cash check FIRST
        System.out.println("ATM cannot dispense " + amount);
        return;                                         //    bail before touching the account
    }
    try {
        atm.bankingService.withdraw(atm.currentCard, amount);   // 2. debit (may throw)
        Map<Denomination,Integer> notes = atm.inventory.dispenseNotes(amount); // 3. hand out cash
        System.out.println("Dispensed: " + notes);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
}
```

> **Why cash-check before debit?** The read-only check can fail and recover cleanly (nothing mutated). If you debited first and dispensing failed, the account is already lighter with no cash out. **Do the reversible checks before the irreversible mutation.** This ordering question is the classic ATM interview probe.

### 5.4 PIN attempts (the deferred requirement)

The counter lives on the **context** (`atm.pinAttempts`), not on any state — because it must survive across repeated `enterPin` calls. On the 3rd failure: eject/retain the card, reset the counter, return to `IdleState`. On success: reset the counter. This is the textbook demonstration of "data lives on the context."

---

## Running It

```powershell
cd "<repo root: the LLD folder>"
javac -d out (Get-ChildItem -Recurse -Filter *.java -Path DesignProblems\AtmMachine).FullName
java -cp out DesignProblems.AtmMachine.Main
```

Expected output for a 1300 withdrawal from a 5000 balance:

```
Card inserted
Pin entered
Current balance: 5000
Dispensed: {FIVE_HUNDRED=2, TWO_HUNDRED=1, HUNDRED=1}
Current balance: 3700
Card ejected
```

> `-d out` keeps compiled `.class` files in an `out/` folder instead of polluting the source tree. Add `out/` and `*.class` to `.gitignore`.

---

## Design Patterns Used

| Pattern            | Where                                     | Why                                                                       |
| ------------------ | ----------------------------------------- | ------------------------------------------------------------------------- |
| **State**          | `IAtmState` + concrete states             | Each phase is a class; invalid ops reject; adding a phase is O(1).        |
| **(Template-ish)** | `AAtmState` abstract base with `reject()` | Default all operations to "reject"; states override only what they allow. |
| Strategy (_noted_) | _Not built_ — dispensing is one method    | Mentioned as the future extension point, deliberately not implemented.    |

---

## Interview Cheat-Sheet

1. **Walk the happy path out loud first** — it _is_ your state machine.
2. **State Pattern**, not an enum + `switch`. Abstract base to kill reject-boilerplate.
3. **Context is stateful, states are stateless.** Session data (card, PIN attempts) lives on the ATM.
4. **States hold a back-reference to the context** (constructor injection) and decide their own transitions.
5. **Validate before mutate**: cash-check → debit → dispense. Never take money you can't pay out.
6. **Two-phase dispensing**: compute a plan, commit only on success.
7. **Enum for denominations.** Greedy dispensing; name the assumption.
8. **Stay confined.** Every class ties to a requirement. Park gold-plating with a sentence; don't build it.

> The interviewer isn't grading your class diagram — they're grading your _judgment_: what you include, what you defer, and whether you can explain **why**.
