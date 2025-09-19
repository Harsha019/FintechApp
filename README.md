# FintechApp

DAY 1
This is a simple Android project built using Kotlin with basic architecture setup.

###  Project Overview

- The **basic setup** of the project is complete.
- The project follows **MVVM (Model-View-ViewModel)** architecture for clean separation of concerns.
- **Room Database** is used for local data storage.
- **Koin** is used for Dependency Injection (DI) since the project is a single-page application and requires lightweight DI management.

### Technologies Used

- **Kotlin**
- **MVVM Architecture**
- **Room Database**
- **Koin for Dependency Injection**
- **Android Jetpack components**

### How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Harsha019/FintechApp.git

###  Day 2
- Designed and created the **main demo screen** UI in XML (Payments, Account Info, Consent Center).  
- Took initial design inspiration from **Uizard AI → Figma export** by giving my ideas as prompt to it .  
- Added **Navigation Component** setup (Activity + NavGraph + MainFragment).  
- Screen is now ready to handle **dynamic data flow** from JSON (work in progress).  
- Next step: integrate **API parsing and AccessManager logic** for cooling period and permissions.

### Day 3
- Integrated **dynamic data flow** from a **mock API response** into the UI.  
- Parsed mock JSON and displayed the data dynamically on the **Main Demo Screen** (Payments, Account Info, Consent Center).  
- Currently, the data is flowing correctly into the UI.  
- **Conditions and AccessManager checks (cooling period, role-based permissions) are not yet added** – this will be implemented in the next step.  
- Next step: Add **cooling period checks, role-based access control, and module permission logic** on top of the dynamic data flow.




