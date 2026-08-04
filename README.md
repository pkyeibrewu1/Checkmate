# 🛡️ CheckMate – Restaurant Order & Exit Verification System

CheckMate is a full-stack Spring Boot application and real-time web dashboard designed to combat industry-wide **dine-and-dash incidents**. By pairing live floor management with an unguessable single-use exit verification protocol, CheckMate ensures customers can only leave the venue after their bill has been fully settled and validated.

---

## 🎯 Problem Statement & Solution

Dine-and-dash incidents cost independent venues thousands of dollars annually and directly penalize waitstaff. Standard Point of Sale (POS) tools handle ordering and payment processing, but lack a venue-exit enforcement mechanism. 

**CheckMate bridges this gap:**
1. Servers manage seating and process table payments directly on a dynamic floor map.
2. Upon payment, the system issues a customer receipt featuring a randomized single-use exit pass.
3. Security scans or enters the pass at the exit gate.
4. Valid passes trigger instant clearance and **automatically reset the table state** back to available.

---

## ✨ Features

* **Interactive Floor Dashboard**: Real-time visual tracking of table states (`Available`, `Occupied`, `Paid / Pending Exit`).
* **Dynamic Menu & Order Management**: Add appetizers, entrees, drinks, and desserts with real-time bill calculations.
* **Unguessable Exit Security Tokens**: Generates randomized 6-character alphanumeric receipt tokens (e.g., `CM-8F2K9P`) to prevent pass guessing or code forgery.
* **Automated Table Recycling**: Validated exit scans automatically clear and release the assigned table without requiring manual intervention.
* **Kitchen Ticket Generation**: Printable kitchen order tickets displaying itemized food choices without exposing bill totals.
* **Privacy-Conscious Security View**: Exit gate validation screens keep financial details hidden from security staff.
* **Automatic Data Persistence**: Maintains session state across application restarts using Java Object Serialization.

---

## 🛠️ Built With

* **Backend**: Java 21, Spring Boot, Spring Web (REST APIs)
* **Frontend**: HTML5, JavaScript (Fetch API), Bootstrap 5, CSS3
* **Containerization & Deployment**: Docker, Render Cloud Hosting
* **Persistence**: Java Object Serialization (`java.io.Serializable`)
* **Build Tool**: Apache Maven

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK) 21** or higher installed.
* **Apache Maven** installed (or build using Docker).

---

### Local Setup & Execution

1. **Clone the repository**:
   ```bash
   git clone [https://github.com/pkyeibrewu1/Checkmate.git](https://github.com/pkyeibrewu1/Checkmate.git)
   cd Checkmate