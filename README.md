# Library Management System

Managing a library's books, members, and borrow records by hand is tedious and error-prone — this project automates the whole cycle in a single desktop application. Built with core Java and Swing, it demonstrates a full OOP-driven system with persistent storage, role-based access, and automated fine tracking.

## Technologies Used
- **Java (Core)** — language and business logic
- **Swing (AWT/Javax Swing)** — GUI: JFrame, JPanel, JTable, CardLayout, GridBagLayout, BoxLayout
- **Java Serialization (ObjectOutputStream/ObjectInputStream)** — persisting Books, Members, Admins, and BorrowRecords to `.dat` files
- **Java Streams API** — filtering, searching, and aggregating collections (search, reports, grouping)
- **java.time (LocalDate, ChronoUnit)** — due dates, overdue detection, fine calculation
- **File I/O** — CSV export, text-based report generation, notification logging

## Key Features
- **Role-based login** — separate Admin and Member dashboards from a single authentication flow
- **Book catalogue management** — add, edit, delete, and search books by title/author/ISBN/genre
- **Borrow / Return / Renew workflow** — availability checks, 14-day loan periods, and renewal rules
- **Automatic fine calculation** — Rs. 5/day late fee, fine-limit enforcement blocking further borrowing
- **Member self-service** — registration, borrowing, renewal, and viewing personal history/profile
- **Reports & analytics** — overdue report and library summary (top borrowed books, active/overdue counts) exported to text files
- **Persistent data layer** — all entities survive app restarts via serialized `.dat` files, with auto-incrementing IDs recovered on load

## Process
1. Designed the domain model first — an abstract `User` class extended by `Admin` and `Member`, plus `Book` and `BorrowRecord` classes.
2. Defined a `LibraryOperations` interface to formalize core actions (checkout, return, renew, fine calculation).
3. Built a `FileHandler` layer to isolate persistence logic (save/load lists, CSV export, logging) from business logic.
4. Implemented `LibraryController` as the central service layer connecting UI actions to data operations.
5. Layered the GUI last — `LoginFrame` → `RegisterFrame` → role-specific dashboards (`AdminDashboard`, `MemberDashboard`) using `CardLayout` for tabbed navigation.
6. Iterated on styling with a shared `Theme` class for consistent dark-mode UI across all screens.

## What I Learned
- Structuring a multi-class Java application around **separation of concerns** (model, controller, persistence, UI)
- Practical use of **inheritance and polymorphism** (abstract `User`, overridden `displayInfo()`)
- Working with **Swing layout managers** (BoxLayout, GridBagLayout, CardLayout) to build a real multi-screen app
- Handling **object serialization** for lightweight persistence without a database
- Using **Java Streams** for concise filtering, searching, and report generation
- Designing **business rules** (fine limits, loan periods, overdue logic) that mirror real-world constraints

## Conclusion
This project strengthened my understanding of core Java, OOP design, and building complete, functional desktop applications from scratch. It's a solid foundation I can extend later with a real database or a web-based interface.

## Run the Project
