# 🎓 STUDIUM: Study Companion App
> **A Real-Time Collaborative Workspace for Students**
> Developed as part of our CS102 group project for Fall 2025.

---
<p align="center">
  <img width="500" height="500" alt="Studium Logo" src="https://github.com/user-attachments/assets/af417683-7e63-430e-a8ca-abc42d05a59d" />
</p>

---
## Overview
**Studium** is an interactive office application designed to enhance students’ motivation and assist them in planning.  It combines personal productivity tools like a calendar and task manager with social features like real-time friend tracking and creating group study sessions.

## Key Features
- **Customized Offices:** Organize your personal offices by earning currency through solo or group sessions and redesign your office anytime you want.
- **Real-Time Sync:** Instant updates across clients using a custom WebSocket protocol.
- **Interactive Event Planner:** Manage your schedule with color-coded events and priority levels determined by the user.
- **Habit Tracking:** Track of your daily-habits over a monthly period.
- **Socialization:** Search for peers according to their departures or names, send friend requests, add them as friends and see who is online.
- **Task Tracking:** Dedicated service to manage your academic to-do lists.
- **Gamification:** Earn virtual currency (Solo/Group) based on your study performance and then buy new items to your office.
- **Online Messaging:** Stay connected with your friends through real-time messaging at any time.

---

## Technologies
The following technologies are utilized to build our Java ecosystem:

* **JavaFX:** Powers the rich Graphical User Interface (GUI) for a responsive desktop experience.
* **WebSockets:** Enables real-time, bi-directional communication between the client, server and data layers.
* **PostgreSQL:** Handles reliable persistent data storage via **JDBC**.
* **BCrypt:** Ensures user security by hashing passwords and decoding hashes during authentication.
* **Google Cloud Integration:**
    * **Google OAuth:** Provides secure and modern user authentication.
    * **Google Calendar API:** Allows users to synchronize their academic schedules effortlessly.
* **JavaMail API:** Manages secure email authentication and verification during the registration process.
* **Maven:** Used for dependency management and project build automation.

---

## Getting Started
Studium is designed as a single-page, scrollable application for ease of use. Here is how you can interact with the platform:

### 1. Personal Workspace (The Office)
* **Customize:** Personalize your virtual office to create a motivating workspace. Go to the market section, click the item that you want to buy and choose one of the possible places. You can later put the purchased item into your storage by right-clicking the item.  
* **Navigation:** From here, you can seamlessly scroll or navigate to the Planner and Socialization sections.

### 2. Planning & Productivity (The Planner)
* **Event Management:** Create and organize events with specific titles, descriptions, and dates. After you click "add event" button you can enter relevant information and save the event by clicking "confirm" button. 
* **Habit Tracking:** Monitor your daily routines over a monthly period to maintain consistency. Click the add buton, enter the name of your habit and by pressing "enter" button on the keyboard you can save the habit. Each day in a month, you can mark the day if you complete your habit. 
* **Task List:** Keep track of your academic to-do items and check them off as you progress. Click "add task" button and enter the relevant information to the relevant places on the task pop-up. You can mark the saved tasks by clicking the little square near each task. You can also see the daily completion proportion and see the weekly to-do-lists. 

### 3. Collaboration (Socialization)
* **Real-Time Messaging:** Chat with friends instantly via the integrated messaging system. First add them as friends and then click the  "send message" button on the messaging box. 
* **Group Study Sessions:** Create or join study rooms to collaborate with other users in real-time.
* **Peer Discovery:** Search for other students by entering their names or departments.
  
### 4. Register & Log in
* **Register:** You can click the register button on the login page and enter your mail and you'll receive a verification code. After you enter this code, you will enter your personal data for the application(username, password etc.). Then, you will be forwarded to the login page again. You can also register with your Google account by clicking register with Google button. You'll be asked to choose your e-mail and then you will be registered.  
* **Log-in:** You can enter your saved username and password to log in. 
---

### Prerequisites
- **JDK 17** or higher
- **Maven 3.6+**
- **PostgreSQL**
  
---
### Team
* **Group1- CS102 (Fall 2025/2026) - Team bugHunters**
- Begüm Göktaş
- Delfin Eryılmaz
- Gülşen Mercan
- Ali Mersin

### Installation & Execution
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/boylebirkisi/CS102-Studium-App-Group-Project.git](https://github.com/boylebirkisi/CS102-Studium-App-Group-Project.git)
   cd CS102-Studium-App-Group-Project
