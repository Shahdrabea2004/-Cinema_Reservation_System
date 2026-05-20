<div align="center">

# 🎬 Cinema Reservation System

### *A console-based Java application that walks a user through booking a cinema seat — film selection, seat choice, snack add-ons, and a formatted receipt.*

[![Java](https://img.shields.io/badge/Java-SE%208+-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Type](https://img.shields.io/badge/Type-Console%20Application-blue?style=flat-square)]()
[![Paradigm](https://img.shields.io/badge/Paradigm-Procedural%20%2B%20Static%20Methods-green?style=flat-square)]()
[![I/O](https://img.shields.io/badge/I%2FO-java.util.Scanner-yellow?style=flat-square)]()

</div>

---

## 📌 Overview

**Cinema Reservation System** is a single-file Java console application that simulates a cinema booking flow entirely in the terminal. The user is guided step by step through entering personal info, picking a film (with age restrictions enforced), choosing a seat, optionally adding snacks, and receiving a color-formatted receipt with the full cost breakdown.

Everything runs in a single class (`Cinema.java`) using **static fields as shared state** and **static methods** as isolated processing units — a clean procedural design with no external dependencies.

---

## 🏗️ Program Architecture

```
main()
  │
  ├─► TakeInfo()              — collect & validate user data
  ├─► clearConsole()
  ├─► ShowDetailsFilms()      — display available films + prices
  ├─► ChooseFilms()           — select film (with age-gate logic)
  ├─► clearConsole()
  ├─► ChoosePlaces()          — choose a seat from valid options
  ├─► clearConsole()
  ├─► IsWantSnake()           — YES/NO prompt (loop until valid)
  │       │
  │   [if YES]
  │       ├─► ShowDetailsSnacks()   — display snack menu
  │       └─► ChooseSnacks()        — multi-select snacks + tally cost
  │
  ├─► clearConsole()
  └─► Recipt()                — print color-coded receipt
          └─► calculateTotal()     — film price + total snack cost
```

---

## ⚙️ Backend Deep Dive

### Global State — Static Fields

All session data lives as **class-level static variables**. There is no object instantiation — state is shared across every method call through these fields:

```java
static String  Films[]       = {"Film X","Film Y","Film Z","Film F"};
static double  filmPrices[]  = {80, 70, 60, 90};
static char    Places[]      = {'L','F','M','R','D'};
static String  snacks[]      = {"Snack X","Snack Y","Snack Z","Snack F","Snack P"};
static double  snackPrices[] = {80, 70, 60, 90, 50};

// --- mutable session state ---
static int     Agestore       = 0;
static String  namestore;
static String  phonestore;
static int     Choosefilmstore = 0;
static double  totalSnackCost  = 0;
static char    placestore;
static String[] snacksstore        = null;
static double[] snacksstoreprice   = null;
```

**Data flow:** each method writes into these fields. Downstream methods read from them. `calculateTotal()` and `Recipt()` consume the fully-populated state at the end.

---

### Method-by-Method Breakdown

---

#### 1. `TakeInfo()` — User Info Collection

```
Prompt: name → phone → age
```

- Reads `name` and `phone` as full lines via `input.nextLine()`
- **Phone validation loop:** enforces exactly 11 digits — repeats prompt on failure:
  ```java
  while (true) {
      if (phone.length() != 11) { prompt again }
      else break;
  }
  ```
- Reads `age` as `int` via `input.nextInt()`
- Writes results into `namestore`, `phonestore`, `Agestore`

**Validation enforced:**
| Field | Rule |
|---|---|
| `name` | Any non-empty string |
| `phone` | Exactly 11 characters |
| `age` | Valid integer (no range check) |

---

#### 2. `ShowDetailsFilms()` — Film Menu Display

Iterates the `Films[]` and `filmPrices[]` arrays in a synchronized loop and prints a formatted table row per film:

```
| 1 | Film X | 80.0 $  |
__________________________
| 2 | Film Y | 70.0 $  |
```

No input. No state mutation. Pure display.

---

#### 3. `ChooseFilms()` — Film Selection with Age Gate

This is the most logic-dense method:

```
loop:
  prompt for integer (1–4)
  if age < 18 AND choice is 1 or 3  → reject ("You can not choose 1 and 3")
  if choice outside [1,4]            → reject ("This film does not exist")
  else                               → save to Choosefilmstore, break
```

**Age restriction logic:**
```java
if (Agestore < 18 && (chooseFilm == 3 || chooseFilm == 1)) {
    // blocked
}
```

Films 1 and 3 are age-restricted (18+). The restriction is enforced server-side (in the method), not just in the display layer.

**Index mapping:**
- User inputs `1–4` (1-based)
- Stored as-is in `Choosefilmstore`
- Accessed later as `Films[Choosefilmstore - 1]` and `filmPrices[Choosefilmstore - 1]`

---

#### 4. `ChoosePlaces()` — Seat Selection

Accepts a single character from stdin. Validates against the `Places[]` array using a linear scan:

```java
for (int i = 0; i < Places.length; i++) {
    if (Places[i] == choosePlace) {
        invalid = true;   // flag: valid match found
        break;
    }
}
if (!invalid) { prompt again }
else break;
```

> ⚠️ Note: the boolean flag is named `invalid` but semantically acts as `found` — it is `true` when a valid match exists. The logic works correctly despite the naming.

Valid seats: `L`, `F`, `M`, `R`, `D`

Result saved to `placestore`.

---

#### 5. `ShowDetailsSnacks()` — Snack Menu Display

Same pattern as `ShowDetailsFilms()` — iterates `snacks[]` and `snackPrices[]` and prints a table. Note: only the first 4 snacks are shown (loop bound is `Films.length == 4`), even though `snacks[]` has 5 items. Snack 5 (`Snack P`) is accessible via `ChooseSnacks()` but not listed in the menu display.

---

#### 6. `ChooseSnacks()` — Multi-Snack Selection

```
prompt: how many snacks? → NoOfSnacks
allocate: snacksstore[NoOfSnacks], snacksstoreprice[NoOfSnacks]

for i in 0..NoOfSnacks:
    prompt: which snack (1–5)?
    loop until valid (1 ≤ choice ≤ 5)
    snacksstore[i]      = snacks[choice - 1]
    snacksstoreprice[i] = snackPrices[choice - 1]
    totalSnackCost     += snackPrices[choice - 1]
```

- Supports **duplicate snack selections** — the same snack can be added multiple times
- Running total accumulated in `totalSnackCost` (static field, never reset between calls)
- Arrays sized dynamically at runtime: `new String[NoOfSnacks]`

---

#### 7. `IsWantSnake()` — Boolean Prompt with Strict Input

Returns `true` (YES) or `false` (NO). Accepts **only exact uppercase strings**:

```java
if (IsWant.equals("YES")) return true;
else if (IsWant.equals("NO")) return false;
else {
    while (true) {
        // loop forever until "YES" or "NO"
    }
}
```

- Calls `input.nextLine()` after consuming the leftover `\n` from the previous `nextInt()` call — this is a standard Java Scanner pitfall, handled explicitly here.

---

#### 8. `calculateTotal()` — Cost Calculation

```java
public static double calculateTotal() {
    return totalSnackCost + filmPrices[Choosefilmstore - 1];
}
```

Simple: adds accumulated snack cost to the selected film's price. No tax, no discount logic.

**Cost breakdown:**

| Component | Source |
|---|---|
| Film price | `filmPrices[Choosefilmstore - 1]` |
| Snack total | `totalSnackCost` (accumulated in `ChooseSnacks()`) |
| **Grand total** | `filmPrices[...] + totalSnackCost` |

---

#### 9. `Recipt()` — Color-Formatted Receipt Printer

Prints the full booking summary using **ANSI escape codes** from the inner `ConsoleColor` class:

```
MAGENTA  → thank-you message, section dividers
BLUE     → user info (name, phone, age)
YELLOW   → film name + price, seat
GREEN    → "Snacks:" label
CYAN     → individual snack lines
RED      → grand total
```

**Duplicate snack deduplication logic (inline in `Recipt()`):**

The receipt doesn't list duplicates separately — it groups same-name snacks and shows quantity × unit price:

```java
for (int i = 0; i < snacksstore.length; i++) {
    boolean IsDuplicate = false;
    double Quantity = 1;

    // check if already printed (backward scan)
    for (int j = 0; j < i; j++) {
        if (snacksstore[i].equals(snacksstore[j])) {
            Quantity++;
            IsDuplicate = true;
            break;
        }
    }
    if (IsDuplicate) continue;   // skip — already printed earlier

    // count how many more appear ahead
    for (int k = i + 1; k < snacksstore.length; k++) {
        if (snacksstore[i].equals(snacksstore[k])) Quantity++;
    }

    double totalprice = snacksstoreprice[i] * Quantity;
    // print: snack name | units: N | Price: X $
}
```

This is an O(n²) grouping algorithm — for the small snack arrays used here, it is perfectly efficient.

---

### `ConsoleColor` — Inner Class for ANSI Colors

A non-static inner class holding **only public static final String constants**. Each constant is an ANSI escape sequence:

```java
public static final String RED    = "\033[31m";
public static final String RESET  = "\033[0m";
// ... 14 more color/background constants
```

Usage pattern throughout `Recipt()`:
```java
System.out.print(ConsoleColor.RED + "Your reservation cost: " + total + ConsoleColor.RESET);
```

> ⚠️ ANSI colors render correctly in Unix/Linux/macOS terminals and Windows Terminal. They may show as raw escape characters in older CMD windows without ANSI support enabled.

---

### `clearConsole()` — Cross-Platform Terminal Clear

Detects the OS at runtime and runs the appropriate clear command:

```java
if (System.getProperty("os.name").contains("Windows")) {
    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
} else {
    new ProcessBuilder("clear").inheritIO().start().waitFor();
}
```

Called between major steps to keep the terminal uncluttered. Uses `ProcessBuilder` instead of `Runtime.exec()` for cleaner process management. Wrapped in try/catch — failures are silently swallowed via `e.printStackTrace()`.

---

## 🗂️ Project Structure

```
Cinema_Reservation_System/
│
└── Cinema.java          # Entire application — 1 class, 9 methods, ~270 lines
```

Single-file project. No packages, no build files, no dependencies.

---

## 🔄 Full Execution Flow

```
START
  │
  ▼
TakeInfo()
  ├─ read name
  ├─ read phone (loop: must be 11 chars)
  └─ read age
  │
clearConsole()
  │
ShowDetailsFilms()   → print film menu table
  │
ChooseFilms()
  ├─ loop:
  │    read int (1–4)
  │    if age<18 AND (choice==1 OR choice==3) → reject
  │    if choice out of range               → reject
  │    else → Choosefilmstore = choice, break
  │
clearConsole()
  │
ChoosePlaces()
  ├─ loop:
  │    read char
  │    linear scan Places[] for match
  │    if no match → reject
  │    else → placestore = char, break
  │
clearConsole()
  │
IsWantSnake()
  ├─ consume leftover \n
  ├─ read "YES" or "NO" (loop until valid)
  └─ returns boolean
      │
   [true]──► ShowDetailsSnacks() → print snack menu
         └─► ChooseSnacks()
               ├─ read count N
               ├─ allocate snacksstore[N], snacksstoreprice[N]
               └─ loop N times:
                    read int (1–5), loop until valid
                    store snack name + price
                    accumulate totalSnackCost
  │
clearConsole() × 2
  │
Recipt()
  ├─ print user info  (BLUE)
  ├─ print film+seat  (YELLOW)
  ├─ if snacks: deduplicate & print (CYAN)
  └─ calculateTotal() → print grand total (RED)
  │
END
```

---

## 🚀 Setup & Run

### Prerequisites

- JDK 8 or higher
- Any terminal with ANSI color support (Linux, macOS, Windows Terminal)

### Compile & Run

```bash
# Clone
git clone https://github.com/your-username/cinema-reservation-system.git
cd cinema-reservation-system

# Compile
javac Cinema.java

# Run
java Cinema
```

### Sample Session

```
Enter your name: Sara
Enter your phone number: 01012345678
Enter your age: 20

There are films available today:
__________________________
| 1 | Film X | 80.0 $  |
__________________________
| 2 | Film Y | 70.0 $  |
...

(if your age less than 18 you can't choose 1 and 3!)
Enter the Film you want please choose (1:4): 2

Please choose a place (L, F, M, R, D): M

Do you want any snack? YES

Enter the number of Snacks you want (1:5): 2
Enter the Snack you want (1:5): 1
Enter the Snack you want (1:5): 1

--- RECEIPT ---
Name: Sara | Phone: 01012345678 | Age: 20
Film Y : 70.0$  | Place: M
Snacks:
  Snack X | units: 2 | Price: 160.0 $
Your reservation cost: 230.0 $
```

---

## 📋 Data Reference

### Films

| # | Name | Price | Age Restriction |
|---|---|---|---|
| 1 | Film X | $80 | 18+ only |
| 2 | Film Y | $70 | All ages |
| 3 | Film Z | $60 | 18+ only |
| 4 | Film F | $90 | All ages |

### Seats

| Code | Description |
|---|---|
| L | Left |
| F | Front |
| M | Middle |
| R | Right |
| D | Default / Back |

### Snacks

| # | Name | Price |
|---|---|---|
| 1 | Snack X | $80 |
| 2 | Snack Y | $70 |
| 3 | Snack Z | $60 |
| 4 | Snack F | $90 |
| 5 | Snack P | $50 |

---

## 🔒 Known Issues & Improvement Notes

| Issue | Location | Description | Fix |
|---|---|---|---|
| No input type safety | `TakeInfo()`, `ChooseFilms()`, `ChooseSnacks()` | `input.nextInt()` throws `InputMismatchException` if non-integer entered | Wrap with try/catch or use `hasNextInt()` guard |
| `totalSnackCost` never resets | Static field | If `main()` were called again (e.g. in a loop), snack cost would accumulate across sessions | Reset to `0` at start of each session |
| Scanner newline issue | `IsWantSnake()` | `input.nextLine()` call needed to consume leftover `\n` after `nextInt()` — handled, but fragile | Use `input.nextLine()` consistently for all input |
| `invalid` flag misnaming | `ChoosePlaces()` | Variable named `invalid` is `true` when input is **valid** | Rename to `found` or `isValid` |
| `ShowDetailsSnacks()` shows only 4 of 5 snacks | Display loop | Loop bound is `Films.length` (4) instead of `snacks.length` (5) | Change to `snacks.length` |
| ANSI color on Windows CMD | `Recipt()` | Older CMD may not render ANSI codes | Add Windows 10+ ANSI enablement or fallback |
| Passwords/data not persisted | Entire app | All reservations lost when program exits | Add file I/O or DB integration for persistence |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java SE 8+ |
| I/O | `java.util.Scanner` (stdin) |
| Output | `System.out.print` + ANSI escape codes |
| Terminal Control | `ProcessBuilder` (cls / clear) |
| Design Pattern | Procedural / Static method pipeline |
| Dependencies | None (pure Java SE) |

---

## 🚧 Possible Enhancements

- 🔁 **Multi-booking loop** — allow multiple reservations per run
- 💾 **File persistence** — save reservations to a `.txt` or `.csv` file
- 🗃️ **Database integration** — store bookings in SQLite or MySQL
- 🎭 **OOP refactor** — extract `Film`, `Seat`, `Snack`, `Reservation` as separate classes
- 🖥️ **GUI layer** — wrap with JavaFX or Swing for a desktop UI
- 🌐 **Web layer** — expose as a REST API with Spring Boot

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

<div align="center">
  <em>One class. Nine methods. Zero dependencies. Full cinema booking. 🎬</em>
</div>
