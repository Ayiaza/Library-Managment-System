package library.managment.system;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
public class LibraryManagementSystem{
    public static void main(String[] args) {
        try 
        { 
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
        }
        catch (Exception ignored) {}
        UIManager.put("OptionPane.background",        new Color(30, 30, 55));
        UIManager.put("Panel.background",             new Color(30, 30, 55));
        UIManager.put("OptionPane.messageForeground", new Color(230, 230, 240));
        UIManager.put("Button.background",            new Color(99, 102, 241));
        UIManager.put("Button.foreground",            Color.WHITE);
        UIManager.put("TextField.background",         new Color(45, 45, 75));
        UIManager.put("TextField.foreground",         new Color(230, 230, 240));
        UIManager.put("TextField.caretForeground",    new Color(230, 230, 240));
        UIManager.put("ComboBox.background",          new Color(45, 45, 75));
        UIManager.put("ComboBox.foreground",          new Color(230, 230, 240));
        UIManager.put("TabbedPane.background",        new Color(30, 30, 55));
        UIManager.put("TabbedPane.foreground",        new Color(230, 230, 240));
        UIManager.put("ScrollPane.background",        new Color(30, 30, 55));
        SwingUtilities.invokeLater(() -> 
        {
            Library_Controller controller = new Library_Controller();
            new Login_Frame(controller).setVisible(true);
        });
    }
}
//User (abstract base)
abstract class User implements Serializable 
{
    private static final long serialVersionUID = 1L;
    protected String userId;            protected String name;
    protected String email;             protected String password;
    protected String role; 
    public User(String userId, String name, String email, String password, String role)
    {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }
    public String get_User_Id()  { return userId; }
    public void set_Name(String n){ this.name = n; }
    public String get_Name(){return name; }
    public void set_Email(String e){  this.email = e;}
    public String get_Email(){ return email; }
    public void set_Password(String p){ this.password = p;}
    public String get_Password(){ return password; }
    public String ge_tRole(){return role; }
    public boolean Login(String email, String password) 
    {
        return this.email.equals(email) && this.password.equals(password);
    }
    public abstract void Display_Info();
    public String toString() 
    {
        return "ID: " + userId + " | Name: " + name + " | Email: " + email + " | Role: " + role;
    }
}
// Admin 
class Admin extends User 
{
    private static final long serialVersionUID = 1L;
    private int adminLevel;          private String managedBranch;
    public Admin(String userId, String name, String email, String password) 
    {
        super(userId, name, email, password, "ADMIN");
        this.adminLevel    = 1;
        this.managedBranch = "Main Branch";
    }
    public Admin(String userId, String name, String email, String password,int adminLevel, String managedBranch)
    {
        super(userId, name, email, password, "ADMIN");
        this.adminLevel    = adminLevel;
        this.managedBranch = managedBranch;
    }
    public void set_Admin_Level(int l){ this.adminLevel = l; }
    public int get_Admin_Level() { return adminLevel; }
    public void set_Managed_Branch(String b) {this.managedBranch = b; }
    public String get_Managed_Branch() {return managedBranch; }
    public void Display_Info() 
    {
        System.out.println("=== ADMIN INFO ===");
        System.out.println("ID: " + userId + " | Name: " + name + " | Branch: " + managedBranch);
    }
    public void Display_Alert() 
    { 
        System.out.println("Admin alert.");
    }
}
//  Member 
class Member extends User 
{
    private static final long serialVersionUID = 1L;
    private String  studentId;         private List<String> borrowedBookIds;
    private double   fineAmount;       private boolean  active;
    public Member(String userId, String name, String email, String password, String studentId) 
    {
        super(userId, name, email, password, "MEMBER");
        this.studentId      = studentId;
        this.borrowedBookIds = new ArrayList<>();
        this.fineAmount     = 0.0;
        this.active         = true;
    }
    public void set_Student_Id(String id){this.studentId = id; }
    public String get_Student_Id()  {return studentId; }
    public void set_BorrowedBookIds(List<String> ids) { this.borrowedBookIds = ids; }
    public List<String> get_Borrowed_Book_Ids() { return borrowedBookIds; }
    public void set_Fine_Amount(double f){  this.fineAmount = f; }
    public double get_Fine_Amount() { return fineAmount; }
    public void set_Active(boolean a){ this.active = a; }
    public boolean is_Active() { return active; }
    public void Add_Fine(double amount){ this.fineAmount += amount; }
    public void Clear_Fine(){  this.fineAmount = 0.0; }
    public void Borrow_Book(String bookId) 
    {
        if (!borrowedBookIds.contains(bookId)) borrowedBookIds.add(bookId);
    }
    public void Return_Book(String bookId) 
    { 
        borrowedBookIds.remove(bookId); 
    }
    public void Display_Info() 
    {
        System.out.println("=== MEMBER INFO ===");
        System.out.println("ID: " + userId + " | Name: " + name + " | Student ID: " + studentId + " | Fine: Rs." + String.format("%.2f", fineAmount) + " | Books: " + borrowedBookIds.size() + " | Active: " + active); 
    }
    public String toString() 
    {
        return "ID: " + userId + " | Name: " + name + " | Email: " + email + " | Fine: Rs." + String.format("%.2f", fineAmount) + " | Books: " + borrowedBookIds.size() + " | Status: " + (active ? "Active" : "Inactive");
    }
}
//Book
class Book implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private String bookId;              private String title;
    private String author;              private String isbn;
    private int    year;                private String genre;
    private int    totalCopies;         private int    availableCopies;
    public Book(String bookId, String title, String author, String isbn,int year, String genre, int totalCopies) 
    {
        this.bookId= bookId;
        this.title= title;
        this.author= author;
        this.isbn= isbn;
        this.year= year;
        this.genre= genre;
        this.totalCopies= totalCopies;
        this.availableCopies= totalCopies;
    }
    public Book(String bookId, String title, String author, int year, String genre) 
    {
        this(bookId, title, author, "N/A", year, genre, 1);
    }
    public void set_Title(String t){ this.title = t; }
    public void set_Author(String a){ this.author = a; }
    public void set_Isbn(String i){ this.isbn = i; }
    public void set_Year(int y){ this.year = y; }
    public void set_Genre(String g){ this.genre = g; }
    public void set_Total_Copies(int c){ this.totalCopies = c; }
    public void set_Available_Copies(int c){ this.availableCopies = c; }
    public String get_Book_Id(){ return bookId; }
    public String get_Title()  { return title; }
    public String get_Author(){ return author; }
    public String get_Isbn() { return isbn; }
    public int get_Year() { return year; }
    public String get_Genre(){ return genre; }
    public int get_Total_Copies(){ return totalCopies; }
    public int get_Available_Copies(){ return availableCopies; }
    public boolean Is_Available(){ return availableCopies > 0; }
    public boolean CheckOut() 
    {
        if (availableCopies > 0) 
        { 
            availableCopies--; return true; 
        }
        return false;
    }
    public void Return_Item() 
    {
        if (availableCopies < totalCopies) availableCopies++; 
    }
    public String toFileString() 
    {
        return bookId + "," + title + "," + author + "," + year + "," + genre+ "," + isbn + "," + totalCopies + "," + availableCopies;
    }
    public String get_Details() 
    {
        return "Book[" + bookId + "] \"" + title + "\" by " + author+ " | ISBN: " + isbn + " | Year: " + year+ " | Genre: " + genre + " | Copies: " + availableCopies + "/" + totalCopies; 
    }
    public String toString() 
    {
        return title + " - " + author + " (" + year + ") [" + genre + "]"+ " | Available: " + availableCopies + "/" + totalCopies;
    }
}
//  BorrowRecord 
class BorrowRecord implements Serializable 
{
    private static final long serialVersionUID = 1L;
    public enum Status { BORROWED, RETURNED, OVERDUE }
    private String    recordId;             private String    memberId;
    private String    bookId;               private LocalDate borrowDate;
    private LocalDate returnDate;           private double    fineAmount;
    private Status    status;               private LocalDate dueDate;
    private static final int    LOAN_DAYS    = 14;
    private static final double FINE_PER_DAY = 5.0; 
    public BorrowRecord(String recordId, String memberId, String bookId) 
    {
        this.recordId   = recordId;
        this.memberId   = memberId;
        this.bookId     = bookId;
        this.borrowDate = LocalDate.now();
        this.dueDate    = borrowDate.plusDays(LOAN_DAYS);
        this.fineAmount = 0.0;
        this.status     = Status.BORROWED;
    }
    public void set_Return_Date(LocalDate d) { this.returnDate = d; }
    public void set_Fine_Amount(double f)    { this.fineAmount = f; }
    public void set_Status(Status s)        { this.status = s; }
    public String get_Record_Id(){ return recordId; }
    public String get_Member_Id(){ return memberId; }
    public String get_Book_Id(){ return bookId; }
    public LocalDate get_Borrow_Date(){ return borrowDate; }
    public LocalDate get_DueDate(){ return dueDate; }
    public LocalDate get_Return_Date(){ return returnDate; }
    public double get_Fine_Amount(){ return fineAmount; }
    public Status get_Status(){ return status; }
    public boolean Is_Overdue() 
    {
        return status == Status.BORROWED && LocalDate.now().isAfter(dueDate);
    }
    public double Calculate_Fine() 
    {
        if (!Is_Overdue()) return 0.0;
        long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        return daysLate * FINE_PER_DAY;
    }
    public double Process_Return() 
    {
        this.returnDate = LocalDate.now();
        if (LocalDate.now().isAfter(dueDate)) 
        {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            this.fineAmount = daysLate * FINE_PER_DAY;
            this.status = Status.OVERDUE;
        } else {
            this.fineAmount = 0.0;
            this.status = Status.RETURNED;
        }
        return this.fineAmount;
    }
    public void Renew() 
    { 
        this.dueDate = LocalDate.now().plusDays(LOAN_DAYS); 
    }
    public String toString() 
    {
        return "Record[" + recordId + "] Member:" + memberId + " Book:" + bookId+ " | Borrowed:" + borrowDate + " Due:" + dueDate+ " | Status:" + status + (fineAmount > 0 ? " Fine:Rs." + String.format("%.2f", fineAmount) : "");
    }
}
//  INTERFACES
interface Library_Operations 
{
    boolean CheckOut(String bookId, String memberId);
    boolean Return_Item(String recordId);
    boolean Renew_Item(String recordId);
    double  Calculate_Fine(String recordId);
}
//  FILE HANDLER  
class File_Handler 
{
    private static final String DATA    = "data/";
    private static final String BOOKS_FILE   = DATA + "books.dat";
    private static final String MEMBERS_FILE = DATA+ "members.dat";
    private static final String ADMINS_FILE  = DATA + "admins.dat";
    private static final String RECORDS_FILE = DATA + "records.dat";
    private static final String CSV_EXPORT   = DATA + "books_data.txt";
    static 
    {
        File dir = new File(DATA);
        if (!dir.exists()) dir.mkdirs();
    }
    @SuppressWarnings("unchecked")
    private static <T> void saveList(List<T> list, String filepath) 
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(filepath)))) 
        {
            oos.writeObject(list);
        } catch (IOException e) {
            System.err.println("Error saving to " + filepath + ": " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    private static <T> List<T> loadList(String filepath) 
    {
        File f = new File(filepath);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(filepath)))) 
        {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from " + filepath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    //Books
    public static void Save_Books(List<Book> books) 
    {
        saveList(books, BOOKS_FILE);
        Export_Books_Csv(books);
    }     
    public static List<Book> Load_Books() 
    { 
        return loadList(BOOKS_FILE); 
    }
    private static void Export_Books_Csv(List<Book> books) 
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_EXPORT))) 
        {
            for (Book b : books) pw.println(b.toFileString());
        } catch (IOException e) {
            System.err.println("CSV export error: " + e.getMessage()); 
        }
    }
    //  Members
    public static void Save_Members(List<Member> members)
    { 
        saveList(members, MEMBERS_FILE); 
    }
    public static List<Member> Load_Members() 
    { 
        return loadList(MEMBERS_FILE); 
    }
    //  Admins
    public static void Save_Admins(List<Admin> admins) 
    { 
        saveList(admins, ADMINS_FILE); 
    }
    public static List<Admin>  Load_Admins() 
    { 
        return loadList(ADMINS_FILE); 
    }
    // Borrow Records 
    public static void Save_Records(List<BorrowRecord> records) 
    { 
        saveList(records, RECORDS_FILE);  
    }
    public static List<BorrowRecord> Load_Records()   
    { 
        return loadList(RECORDS_FILE); 
    }
    //  Reports 
    public static void Export_Report(String content, String filename) 
    {
        File reportsDir = new File(DATA + "reports/");
        if (!reportsDir.exists()) reportsDir.mkdirs();
        String path = DATA + "reports/" + filename;
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, true))) 
        {
            pw.println(content);
            System.out.println("Report saved to " + path);
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
        }
    }
    public static void Log_Notification(String message) 
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA + "notifications.log", true))) 
        {
            pw.println("[" + java.time.LocalDateTime.now() + "] " + message);
        } catch (IOException e) {
            System.err.println("Error logging notification: " + e.getMessage());
        }
    }
    public static String get_Data() 
    { 
        return DATA; 
    }
}
//  ID GENERATOR
class Id_Generator 
{
    private static final AtomicInteger bookSeq   = new AtomicInteger(1000);
    private static final AtomicInteger memberSeq = new AtomicInteger(2000);
    private static final AtomicInteger recordSeq = new AtomicInteger(3000);
    public static String nextBookId(){ return "BK"  + bookSeq.incrementAndGet(); }
    public static String nextMemberId(){ return "MB"  + memberSeq.incrementAndGet(); }
    public static String nextRecordId() 
    {
        return "RC" + recordSeq.incrementAndGet() + "-" + LocalDate.now().getYear();
    }
    public static void i_Book_Seq(int max)   
    { 
        if (max >= bookSeq.get())bookSeq.set(max); 
    }
    public static void i_Member_Seq(int max) 
    { 
        if (max >= memberSeq.get()) memberSeq.set(max);
    }
    public static void i_Record_Seq(int max) 
    { 
        if (max >= recordSeq.get()) recordSeq.set(max); 
    }
}
//  LIBRARY CONTROLLER  
class Library_Controller implements Library_Operations 
{
    private List<Book>books;
    private List<Member> members;
    private List<Admin>  admins;
    private List<BorrowRecord> records;
    private static final double FINE_LIMIT = 500.0; 
    public Library_Controller() 
    {
        books   = File_Handler.Load_Books();
        members = File_Handler.Load_Members();
        admins  = File_Handler.Load_Admins();
        records = File_Handler.Load_Records();
        books  .forEach(b -> Id_Generator.i_Book_Seq(Parse_Num(b.get_Book_Id(),   2)));
        members.forEach(m -> Id_Generator.i_Member_Seq(Parse_Num(m.get_User_Id(),   2)));
        records.forEach(r -> Id_Generator.i_Record_Seq(Parse_Num(r.get_Record_Id(), 2)));
        if (admins.isEmpty()) Seed_Default_Admin();
    }
    private int Parse_Num(String id, int skip) 
    {
        try{ 
            return Integer.parseInt(id.substring(skip).split("-")[0]); 
        }catch (Exception e) { 
            return 0; 
        }
    }
    private void Seed_Default_Admin() 
    {
        admins.add(new Admin("A001", "Admin", "admin@library.com", "admin123"));
        File_Handler.Save_Admins(admins);
    }
    // Authentication 
    public User Authenticate(String email, String password) 
    {
        for (Admin a  : admins)  if (a.Login(email, password))  return a;
        for (Member m : members) if (m.Login(email, password))  return m;
        return null;
    }
    public boolean Validate_Login(String id) 
    { 
        return id != null && !id.isEmpty(); 
    }
    //Book Operations 
    public boolean Add_Book(String title, String author, String isbn, int year, String genre, int copies)  
    {
        String id = Id_Generator.nextBookId();
        books.add(new Book(id, title, author, isbn, year, genre, copies));
        File_Handler.Save_Books(books);
        return true;
    }
    public boolean Remove_Book(String bookId)  
    {
        boolean removed = books.removeIf(b -> b.get_Book_Id().equals(bookId));
        if (removed) File_Handler.Save_Books(books);
        return removed;
    }
    public boolean Update_Book(String bookId, String title, String author, String isbn, int year, String genre, int copies)
    {
        for (Book b : books) {
            if (b.get_Book_Id().equals(bookId)) {
                b.set_Title(title); b.set_Author(author); b.set_Isbn(isbn);
                b.set_Year(year);   b.set_Genre(genre);   b.set_Total_Copies(copies);
                File_Handler.Save_Books(books);
                return true; 
            }
        }
        return false;
    }
    public List<Book> get_All_Books()  
    { 
        return Collections.unmodifiableList(books); 
    }
    public List<Book> Search_Books(String query) 
    {
        String q = query.toLowerCase();
        return books.stream().filter(b -> b.get_Title().toLowerCase().contains(q)|| b.get_Author().toLowerCase().contains(q)|| b.get_Isbn().toLowerCase().contains(q)|| b.get_Genre().toLowerCase().contains(q)).collect(Collectors.toList());
    }
    public List<Book> get_Books_By_Genre(String genre) 
    {
        return books.stream()
                .filter(b -> b.get_Genre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }
    public Book Find_Book(String bookId)  
    {
        return books.stream().filter(b -> b.get_Book_Id().equals(bookId)).findFirst().orElse(null);
    }
    public List<String> get_Genres() 
    {
        return books.stream().map(Book::get_Genre).distinct().sorted().collect(Collectors.toList());
    }
    // Member Operations
    public String Register_Member(String name, String email, String password, String studentId) 
    {
        for (Member m : members)
            if (m.get_Email().equalsIgnoreCase(email)) return null; 
        String id = Id_Generator.nextMemberId();
        members.add(new Member(id, name, email, password, studentId));
        File_Handler.Save_Members(members);
        return id;
    }
    public boolean Update_Member(String userId, String name, String email)
    {
        for (Member m : members) {
            if (m.get_User_Id().equals(userId)) {
                m.set_Name(name); m.set_Email(email);
                File_Handler.Save_Members(members);
                return true;
            }
        }
        return false;
    }
    public boolean Deactivate_Member(String userId) 
    {
        for (Member m : members) {
            if (m.get_User_Id().equals(userId)) {
                m.set_Active(false);
                File_Handler.Save_Members(members);
                return true;
            }
        }
        return false;
    }
    public List<Member> get_All_Members() 
    { 
        return Collections.unmodifiableList(members); 
    }
    public Member Find_Member(String userId)
    {
        return members.stream().filter(m -> m.get_User_Id().equals(userId)).findFirst().orElse(null);
    }
    // Borrow / Return / Renew 
    public boolean CheckOut(String bookId, String memberId) 
    {
        Book   book   = Find_Book(bookId);
        Member member = Find_Member(memberId);
        if (book == null || member == null)return false;
        if (!member.is_Active()) return false;
        if (member.get_Fine_Amount() > FINE_LIMIT) return false;
        if (!book.CheckOut()) return false;       
        BorrowRecord record = new BorrowRecord(Id_Generator.nextRecordId(), memberId, bookId);
        records.add(record);
        member.Borrow_Book(bookId);
        File_Handler.Save_Books(books);
        File_Handler.Save_Members(members);
        File_Handler.Save_Records(records);
        return true;
    }
    public boolean Return_Item(String recordId) 
    {
        for (BorrowRecord r : records) {
            if (r.get_Record_Id().equals(recordId)
                    && r.get_Status() != BorrowRecord.Status.RETURNED) {
                double fine   = r.Process_Return();
                Book   book   = Find_Book(r.get_Book_Id());
                Member member = Find_Member(r.get_Member_Id());
                if (book   != null) book.Return_Item();
                if (member != null) {
                    member.Return_Book(r.get_Book_Id());
                    member.Add_Fine(fine);}
                File_Handler.Save_Books(books);
                File_Handler.Save_Members(members);
                File_Handler.Save_Records(records);
                return true;
            }
        }
        return false;
    }
    public boolean Renew_Item(String recordId) 
    {
        for (BorrowRecord r : records) {
            if (r.get_Record_Id().equals(recordId)
                    && r.get_Status() == BorrowRecord.Status.BORROWED
                    && !r.Is_Overdue()) {
                Book book = Find_Book(r.get_Book_Id());
                if (book != null && book.get_Available_Copies() > 0) {
                    r.Renew();
                    File_Handler.Save_Records(records);
                    return true;
                } 
            }
        }
        return false; 
    }
    public double Calculate_Fine(String recordId) 
    {
        return records.stream()
                .filter(r -> r.get_Record_Id().equals(recordId))
                .findFirst()
                .map(BorrowRecord::Calculate_Fine)
                .orElse(0.0);
    }
    public boolean Pay_Fine(String memberId, double amount) 
    {
        Member m = Find_Member(memberId);
        if (m == null) return false;
        double paid = Math.min(amount, m.get_Fine_Amount());
        m.set_Fine_Amount(m.get_Fine_Amount() - paid);
        File_Handler.Save_Members(members);
        return true;
    }
    //  Records / Reports
    public List<BorrowRecord> get_All_Records()    
    { 
        return Collections.unmodifiableList(records); 
    }
    public List<BorrowRecord> get_Active_Records()
    {
        return records.stream().filter(r -> r.get_Status() == BorrowRecord.Status.BORROWED).collect(Collectors.toList());
    }
    public List<BorrowRecord> get_Overdue_Records() 
    {
        return records.stream().filter(BorrowRecord::Is_Overdue).collect(Collectors.toList()); 
    }
    public List<BorrowRecord> getMemberRecords(String memberId) 
    {
        return records.stream().filter(r -> r.get_Member_Id().equals(memberId)).collect(Collectors.toList()); 
    }
    public List<BorrowRecord> Search_Records(String query)
    {
        String q=query.toLowerCase();
        return records.stream().filter(r->
        r.get_Record_Id().toLowerCase().contains(q) ||
        r.get_Member_Id().toLowerCase().contains(q) ||
        r.get_Book_Id().toLowerCase().contains(q) ||
        r.get_Status().toString().toLowerCase().contains(q) ).collect(Collectors.toList());
    }
    public String Generate_Overdue_Report() 
    {
        StringBuilder s1 = new StringBuilder();
        s1.append("OVERDUE BOOKS REPORT \n");
        s1.append("Generated: ").append(LocalDate.now()).append("\n\n");
        List<BorrowRecord> overdue = get_Overdue_Records();
        if (overdue.isEmpty()) {
            s1.append("No overdue books at this time.\n");
        } else {
            for (BorrowRecord r : overdue) {
                Member m = Find_Member(r.get_Member_Id());
                Book   b = Find_Book(r.get_Book_Id());
                s1.append("Record : ").append(r.get_Record_Id()).append("\n");
                s1.append("Member : ").append(m != null ? m.get_Name() : r.get_Member_Id()).append("\n");
                s1.append("Book   : ").append(b != null ? b.get_Title() : r.get_Book_Id()).append("\n");
                s1.append("Due    : ").append(r.get_DueDate()).append("\n");
                s1.append("Fine   : Rs.").append(String.format("%.2f", r.Calculate_Fine())).append("\n"); }}
        String report = s1.toString();
        File_Handler.Export_Report(report, "overdue_" + LocalDate.now() + ".txt");
        return report;
    }
    public String Generate_Summary_Report() 
    {
        StringBuilder s2 = new StringBuilder();
        s2.append(" LIBRARY SUMMARY REPORT  \n");
        s2.append("Generated: ").append(LocalDate.now()).append("\n\n");
        s2.append("📚 Total Books    : ").append(books.size()).append("\n");
        s2.append("👥 Total Members  : ").append(members.size()).append("\n");
        s2.append("📋 Total Records  : ").append(records.size()).append("\n");
        s2.append("🔴 Overdue Books  : ").append(get_Overdue_Records().size()).append("\n");
        s2.append("🔵 Active Borrows : ").append(get_Active_Records().size()).append("\n\n");
        Map<String, Long> borrowCount = records.stream()
                .collect(Collectors.groupingBy(BorrowRecord::get_Book_Id, Collectors.counting()));
        s2.append("📊 Top 5 Most Borrowed:\n");
        borrowCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> {
                    Book b = Find_Book(e.getKey());
                    s2.append("   ").append(b != null ? b.get_Title() : e.getKey())
                      .append(" - ").append(e.getValue()).append(" time(s)\n");
                });
        String report = s2.toString();
        File_Handler.Export_Report(report, "summary_" + LocalDate.now() + ".txt");
        return report;
    }
    public void Run_Notification_Check() 
    { }
}
//  GUI LAYER 
class Theme
{
    static final Color BG_DARK   = new Color(18, 18, 35);
    static final Color ACCENT    = new Color(99, 102, 241);
    static final Color PANEL_BG  = new Color(30, 30, 55);
    static final Color SIDE_BG   = new Color(22, 22, 42);
    static final Color TEXT_MAIN = new Color(230, 230, 240);
    static final Color TEXT_MUTED= new Color(150, 150, 180);
    static final Color SUCCESS   = new Color(34, 197, 94);
    static final Color WARNING   = new Color(251, 191, 36);
    static final Color DANGER    = new Color(239, 68, 68);
    static final Color TABLE_ROW = new Color(38, 38, 65);
    static final Color TABLE_ALT = new Color(33, 33, 58);
}
//  LOGIN FRAME
class Login_Frame extends JFrame 
{
    private final Library_Controller controller;
    private JTextField    emailField;
    private JPasswordField passwordField;
    private JLabel         statusLabel;
    public Login_Frame(Library_Controller controller) 
    {
        this.controller = controller;
        UI();
    }
    private void UI() 
    {
        setTitle("Advanced Library Management System – Login");
        setSize(480, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);
        JPanel header = new JPanel();
        header.setBackground(Theme.BG_DARK);
        header.setBorder(new EmptyBorder(40, 30, 10, 30));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel iconLabel = new JLabel("📖", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel titleLabel = new JLabel("Library Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Theme.TEXT_MAIN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subLabel = new JLabel("Advanced Edition", SwingConstants.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(Theme.TEXT_MUTED);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(iconLabel);
        header.add(Box.createVerticalStrut(10));
        header.add(titleLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(subLabel);
        JPanel card = new JPanel();
        card.setBackground(Theme.PANEL_BG);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        emailField    = Styled_Field();
        passwordField = new JPasswordField();
        Style_PassField(passwordField);
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Theme.DANGER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton loginBtn = Accent_Button("Sign In");
        loginBtn.addActionListener(e -> Do_Login());
        passwordField.addActionListener(e -> Do_Login());
        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        regPanel.setBackground(Theme.PANEL_BG);
        JLabel regQ = Styled_MutedLabel("New member?");
        JLabel regLink = Clickable_Label("Register here", Theme.ACCENT);
        regLink.addMouseListener(new MouseAdapter() {
            public void Mouse_Clicked(MouseEvent e) 
            { 
                Open_Register(); 
            }
        });
        regPanel.add(regQ);
        regPanel.add(regLink);
        card.add(Field_Label("Email Address")); card.add(Box.createVerticalStrut(6));
        card.add(emailField);                  card.add(Box.createVerticalStrut(16));
        card.add(Field_Label("Password"));      card.add(Box.createVerticalStrut(6));
        card.add(passwordField);               card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);                    card.add(Box.createVerticalStrut(12));
        card.add(statusLabel);                 card.add(Box.createVerticalStrut(12));
        card.add(regPanel);
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Theme.BG_DARK);
        wrapper.setBorder(new EmptyBorder(0, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        wrapper.add(card, gbc);
        root.add(header, BorderLayout.NORTH);
        root.add(wrapper, BorderLayout.CENTER);
        setContentPane(root);
    }
    private void Do_Login() 
    {
       String email = emailField.getText().trim();
        String pass  = new String(passwordField.getPassword());
        if (email.isEmpty() || pass.isEmpty()) {
            Show_Status("Please enter email and password.", false); return;}
        User user = controller.Authenticate(email, pass);
        if (user == null) {
            Show_Status("Invalid email or password.", false);
            passwordField.setText("");
        } else {
            Show_Status("Login successful!", true);
            SwingUtilities.invokeLater(() -> {
                setVisible(false);
            if (user instanceof Admin)
                new Admin_Dashboard(controller, (Admin) user).setVisible(true);
            else
                new Member_Dashboard(controller, (Member) user).setVisible(true);
            dispose();
            });
        }
    }
private void Open_Register() 
{
        new Register_Frame(controller, this).setVisible(true);
        setVisible(false);
}
    private void Show_Status(String msg, boolean success) 
    {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? Theme.SUCCESS : Theme.DANGER);
    }
    //  Widget helpers 
    private JLabel Field_Label(String text)
    {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    static JTextField Styled_Field() 
    {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(new Color(45, 45, 75));
        f.setForeground(Theme.TEXT_MAIN);
        f.setCaretColor(Theme.TEXT_MAIN);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 100), 1),
                new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return f;
    }
    static void Style_PassField(JPasswordField f) 
    {  
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(new Color(45, 45, 75));
        f.setForeground(Theme.TEXT_MAIN);
        f.setCaretColor(Theme.TEXT_MAIN);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 100), 1),
                new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    }
    static JButton Accent_Button(String text) 
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(Theme.ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.addMouseListener(new MouseAdapter() {
            public void Mouse_Entered(MouseEvent e) 
            {
                btn.setBackground(Theme.ACCENT.darker()); 
            }
            public void Mouse_Exited(MouseEvent e)  
            { 
                btn.setBackground(Theme.ACCENT); 
            }
        });
        return btn;
    }
    private JLabel Styled_MutedLabel(String text)
    {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.TEXT_MUTED);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }
    private JLabel Clickable_Label(String text, Color color) 
    {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return l;
    }
}
//  REGISTER FRAME
class Register_Frame extends JFrame 
{
    private final Library_Controller controller;
    private final JFrame  parent;
    private JTextField nameField, emailField, studentIdField;
    private JPasswordField passField, confirmPassField;
    private JLabel  statusLabel;
    public Register_Frame(Library_Controller controller, JFrame parent) 
    {
        this.controller = controller;
        this.parent     = parent;
        UI();
    }
    private void UI() 
    {
        setTitle("Register – New Member");
        setSize(480, 630);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new WindowAdapter() 
        {
            public void Window_Closed(WindowEvent e) 
            { 
                parent.setVisible(true); 
            }
        });
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);
        JPanel header = new JPanel();
        header.setBackground(Theme.BG_DARK);
        header.setBorder(new EmptyBorder(30, 30, 10, 30));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Create Member Account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Theme.TEXT_MAIN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);
        JPanel card = new JPanel();
        card.setBackground(Theme.PANEL_BG);
        card.setBorder(new EmptyBorder(24, 36, 24, 36));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        nameField       = Login_Frame.Styled_Field();
        emailField      = Login_Frame.Styled_Field();
        studentIdField  = Login_Frame.Styled_Field();
        passField       = new JPasswordField();    Login_Frame.Style_PassField(passField);
        confirmPassField= new JPasswordField();    Login_Frame.Style_PassField(confirmPassField);
        card.add(lbl("Full Name"));          card.add(Box.createVerticalStrut(4)); card.add(nameField);       card.add(Box.createVerticalStrut(12));
        card.add(lbl("Email Address"));      card.add(Box.createVerticalStrut(4)); card.add(emailField);      card.add(Box.createVerticalStrut(12));
        card.add(lbl("Student / Roll No.")); card.add(Box.createVerticalStrut(4)); card.add(studentIdField);  card.add(Box.createVerticalStrut(12));
        card.add(lbl("Password"));           card.add(Box.createVerticalStrut(4)); card.add(passField);       card.add(Box.createVerticalStrut(12));
        card.add(lbl("Confirm Password"));   card.add(Box.createVerticalStrut(4)); card.add(confirmPassField);card.add(Box.createVerticalStrut(20));
        JButton regBtn = Login_Frame.Accent_Button("Register");
        regBtn.addActionListener(e -> Do_Register());
        card.add(regBtn);
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Theme.DANGER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10)); card.add(statusLabel);
        JLabel backLink = new JLabel("← Back to Login", SwingConstants.CENTER);
        backLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backLink.setForeground(Theme.ACCENT);
        backLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        backLink.addMouseListener(new MouseAdapter() 
        {
            public void Mouse_Clicked(MouseEvent e) 
            { 
                dispose(); 
            }
        });
        card.add(Box.createVerticalStrut(8)); card.add(backLink);
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Theme.BG_DARK);
        wrapper.setBorder(new EmptyBorder(0, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        wrapper.add(card, gbc);
        root.add(header, BorderLayout.NORTH);
        root.add(wrapper, BorderLayout.CENTER);
        setContentPane(root);
    }
    private void Do_Register() 
    {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String sid   = studentIdField.getText().trim();
        String pass  = new String(passField.getPassword());
        String conf  = new String(confirmPassField.getPassword());
        if (name.isEmpty() || email.isEmpty() || sid.isEmpty() || pass.isEmpty()) {
            Show_Status("All fields are required.", false); return;
        }
        if (!email.contains("@")) { Show_Status("Enter a valid email.", false); 
        return; 
        }
        if (!pass.equals(conf)){ Show_Status("Passwords do not match.", false); 
        return; 
        }
        if (pass.length() < 6){ Show_Status("Password must be at least 6 characters.", false); 
        return; 
        }
        String id = controller.Register_Member(name, email, pass, sid);
        if (id == null) Show_Status("Email already registered.", false);
        else            
            Show_Status("Registration successful! Your ID: " + id, true);
    }
    private void Show_Status(String msg, boolean success) 
    {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? Theme.SUCCESS : Theme.DANGER);
    }
    private JLabel lbl(String text) 
    {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
//  ADMIN DASHBOARD
class Admin_Dashboard extends JFrame 
{
    private final Library_Controller controller;
    private final Admin admin;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    public Admin_Dashboard(Library_Controller controller, Admin admin) 
    {
        this.controller = controller;
        this.admin = admin;
        UI();
        controller.Run_Notification_Check();
    }
    private void UI() 
    {
        setTitle("Advanced Library Management System – Admin Dashboard");
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);
        root.add(Build_Sidebar(), BorderLayout.WEST);
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG_DARK);
        contentPanel.add(Build_HomePanel(),    "HOME");
        contentPanel.add(Build_BooksPanel(),   "BOOKS");
        contentPanel.add(Build_MembersPanel(), "MEMBERS");
        contentPanel.add(Build_IssuePanel(),   "ISSUE");
        contentPanel.add(Build_RecordsPanel(), "RECORDS");
        contentPanel.add(Build_ReportsPanel(), "REPORTS");
        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);
        Show_Card("HOME");
    }
    // Sidebar 
    private JPanel Build_Sidebar() 
    {
        JPanel side = new JPanel();
        side.setBackground(Theme.SIDE_BG);
        side.setPreferredSize(new Dimension(205, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(20, 0, 20, 0));
        JLabel logo = new JLabel("📖 LMS", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Theme.TEXT_MAIN);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(new EmptyBorder(0, 0, 4, 0));
        JLabel adminLbl = new JLabel("Admin: " + admin.get_Name(), SwingConstants.CENTER);
        adminLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        adminLbl.setForeground(Theme.TEXT_MUTED);
        adminLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 50, 80));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        side.add(logo); side.add(adminLbl);
        side.add(Box.createVerticalStrut(14));
        side.add(sep);
        side.add(Box.createVerticalStrut(14));
        String[][] nav = { {"🏠", "Dashboard",  "HOME"},{"📚", "Books",      "BOOKS"}, {"👥", "Members",    "MEMBERS"},{"📤", "Issue Book", "ISSUE"},{"📋", "Records",    "RECORDS"},{"📊", "Reports",    "REPORTS"},};
        for (String[] item : nav) side.add(Nav_Btn(item[0] + "  " + item[1], item[2]));
        side.add(Box.createVerticalGlue());
        side.add(Logout_Btn());
        return side;
    }
    private JButton Nav_Btn(String text, String card)
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Theme.TEXT_MUTED);
        btn.setBackground(Theme.SIDE_BG);
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(e -> Show_Card(card));
        btn.addMouseListener(new MouseAdapter() {
            public void Mouse_Entered(MouseEvent e) { 
                btn.setBackground(new Color(35,35,65)); 
                btn.setForeground(Theme.TEXT_MAIN);
            }
            public void Mouse_Exited(MouseEvent e)  {
                btn.setBackground(Theme.SIDE_BG);      
                btn.setForeground(Theme.TEXT_MUTED); 
            }
        });
        return btn;
    }
    private JButton Logout_Btn() 
    {
        JButton btn = new JButton("🚪  Logout");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Theme.DANGER);
        btn.setBackground(Theme.SIDE_BG);
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(e -> {
            dispose(); 
            new Login_Frame(controller).setVisible(true); 
        });
        return btn;
    }
    private void Show_Card(String name) 
    { 
        cardLayout.show(contentPanel, name); 
    }
    private JLabel Page_Header(String title) 
    {
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(Theme.TEXT_MAIN);
        l.setBorder(new EmptyBorder(0, 0, 20, 0));
        return l;
    }
    // HOME 
    private JPanel Build_HomePanel()
    {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("Welcome back, " + admin.get_Name() + " 👋"), BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(2, 3, 16, 16));
        cards.setBackground(Theme.BG_DARK);
        cards.add(Stat_Card("📚", "Total Books",    String.valueOf(controller.get_All_Books().size()),   Theme.ACCENT));
        cards.add(Stat_Card("👥", "Total Members",  String.valueOf(controller.get_All_Members().size()), Theme.SUCCESS));
        cards.add(Stat_Card("📋", "Total Records",  String.valueOf(controller.get_All_Records().size()), Theme.WARNING));
        cards.add(Stat_Card("🔵", "Active Borrows", String.valueOf(controller.get_Active_Records().size()), new Color(59,130,246)));
        cards.add(Stat_Card("🔴", "Overdue Books",  String.valueOf(controller.get_Overdue_Records().size()), Theme.DANGER));
        cards.add(Stat_Card("📁", "Genres",         String.valueOf(controller.get_Genres().size()),    new Color(168,85,247)));
        p.add(cards, BorderLayout.CENTER);
        return p;
    }
    private JPanel Stat_Card(String icon, String label, String value, Color accent) 
    {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.PANEL_BG);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        JLabel ico = new JLabel(icon + "  " + label);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        ico.setForeground(Theme.TEXT_MUTED);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 36));
        val.setForeground(accent);
        JPanel left = new JPanel(new BorderLayout()); left.setBackground(Theme.PANEL_BG);
        left.add(ico, BorderLayout.NORTH); left.add(val, BorderLayout.CENTER);
        JPanel bar = new JPanel(); bar.setPreferredSize(new Dimension(4, 0)); bar.setBackground(accent);
        card.add(bar, BorderLayout.WEST); card.add(left, BorderLayout.CENTER);
        return card;
    }
    //BOOKS
    private JPanel Build_BooksPanel() 
    {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("📚 Book Catalogue"), BorderLayout.NORTH);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setBackground(Theme.BG_DARK);
        JTextField searchField = new JTextField(18); Style_TextField(searchField);
        JButton searchBtn= Small_Btn("Search",Theme.ACCENT);
        JButton addBtn= Small_Btn("+ Add Book",Theme.SUCCESS);
        JButton editBtn= Small_Btn("Edit",Theme.WARNING);
        JButton deleteBtn= Small_Btn("Delete",Theme.DANGER);
        JButton refreshBtn = Small_Btn("↻ Refresh",Theme.TEXT_MUTED);
        toolbar.add(searchField); toolbar.add(searchBtn); toolbar.add(addBtn);
        toolbar.add(editBtn); toolbar.add(deleteBtn); toolbar.add(refreshBtn);
        String[] cols = {"Book ID","Title","Author","ISBN","Year","Genre","Available","Total"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) 
        {
            public boolean isCellEditable(int r, int c) 
            { 
                return false; 
            }};
        JTable table = Styled_Table(model);
        Refresh_BookTable(model);
        searchBtn .addActionListener(e -> { 
            model.setRowCount(0); controller.Search_Books(searchField.getText().trim()).forEach(b -> model.addRow(Book_Row(b))); 
        });
        refreshBtn.addActionListener(e -> Refresh_BookTable(model));
        addBtn.addActionListener(e ->{ 
            Show_AddBook_Dialog(); Refresh_BookTable(model); 
        });
        editBtn   .addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a book first."); 
            return; 
            }
            Book b = controller.Find_Book((String) model.getValueAt(row, 0));
            if (b != null) { 
                Show_EditBook_Dialog(b); 
                Refresh_BookTable(model); 
            }
        });
        deleteBtn .addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a book first."); 
            return; 
            }
            String id = (String) model.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete book " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                controller.Remove_Book(id); Refresh_BookTable(model);
            }
        });
        p.add(toolbar, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }
    private void Refresh_BookTable(DefaultTableModel model) 
    { 
        model.setRowCount(0);
        controller.get_All_Books().forEach(b -> model.addRow(Book_Row(b)));
    }
    private Object[] Book_Row(Book b) 
    {
        return new Object[]{b.get_Book_Id(), b.get_Title(), b.get_Author(), b.get_Isbn(),b.get_Year(), b.get_Genre(), b.get_Available_Copies(), b.get_Total_Copies()};
    }
    private void Show_AddBook_Dialog() 
    {
        JTextField t = new JTextField(), a = new JTextField(), isbn = new JTextField();
        JTextField yr = new JTextField(), g = new JTextField(), copies = new JTextField("1");
        Object[] fields = {"Title:", t, "Author:", a, "ISBN:", isbn, "Year:", yr, "Genre:", g, "Copies:", copies};
        if (JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) 
        {
            try { controller.Add_Book(t.getText(), a.getText(), isbn.getText(),
                        Integer.parseInt(yr.getText()), g.getText(), Integer.parseInt(copies.getText()));
                  JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(this, "Year and Copies must be numbers."); 
            }
        }
    }
    private void Show_EditBook_Dialog(Book b) 
    {
        JTextField t = new JTextField(b.get_Title()), a = new JTextField(b.get_Author());
        JTextField isbn = new JTextField(b.get_Isbn()), yr = new JTextField(String.valueOf(b.get_Year()));
        JTextField g = new JTextField(b.get_Genre()), copies = new JTextField(String.valueOf(b.get_Total_Copies()));
        Object[] fields = {"Title:", t, "Author:", a, "ISBN:", isbn, "Year:", yr, "Genre:", g, "Copies:", copies};
        if (JOptionPane.showConfirmDialog(this, fields, "Edit Book " + b.get_Book_Id(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) 
        {
            try { controller.Update_Book(b.get_Book_Id(), t.getText(), a.getText(), isbn.getText(),
                        Integer.parseInt(yr.getText()), g.getText(), Integer.parseInt(copies.getText()));
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Year and Copies must be numbers."); 
            }
        }
    }
    // MEMBERS 
    private JPanel Build_MembersPanel() 
    {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("👥 Member Management"), BorderLayout.NORTH);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setBackground(Theme.BG_DARK);
        JButton refreshBtn    = Small_Btn("↻ Refresh",   Theme.TEXT_MUTED);
        JButton deactivateBtn = Small_Btn("Deactivate",  Theme.DANGER);
        JButton payFineBtn    = Small_Btn("💰 Pay Fine", Theme.SUCCESS);
        toolbar.add(refreshBtn); toolbar.add(deactivateBtn); toolbar.add(payFineBtn);
        String[] cols = {"User ID","Name","Email","Student ID","Fine (Rs.)","Books","Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) 
        {
            public boolean isCellEditable(int r, int c)
            { 
                return false; 
            }
        };
        JTable table = Styled_Table(model);
        refreshMemberTable(model);
        refreshBtn.addActionListener(e -> refreshMemberTable(model));
        deactivateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a member first."); 
            return; 
            }
            String id = (String) model.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Deactivate member " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
            {
                controller.Deactivate_Member(id); refreshMemberTable(model); 
            }
        });
        payFineBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a member first."); 
            return; 
            }
            String id = (String) model.getValueAt(row, 0);
            String amtStr = JOptionPane.showInputDialog(this, "Enter payment amount (Rs.):");
            if (amtStr != null){
                try { 
                    controller.Pay_Fine(id, Double.parseDouble(amtStr)); 
                    refreshMemberTable(model); 
                    JOptionPane.showMessageDialog(this, "Fine payment recorded.");
                } catch (NumberFormatException ex) { 
                    JOptionPane.showMessageDialog(this, "Invalid amount."); 
                }
            }
        });
        p.add(toolbar, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p; 
    }
    private void refreshMemberTable(DefaultTableModel model)
    {
        model.setRowCount(0);
        for (Member m : controller.get_All_Members())
            model.addRow(new Object[]{m.get_User_Id(), m.get_Name(), m.get_Email(), m.get_Student_Id(),
                String.format("%.2f", m.get_Fine_Amount()), m.get_Borrowed_Book_Ids().size(),
                m.is_Active() ? "Active" : "Inactive"});
    }
    // ISSUE / RETURN / RENEW
    private JPanel Build_IssuePanel() 
    {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("📤 Issue / Return Books"), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(Theme.PANEL_BG);
        tabs.setForeground(Theme.TEXT_MAIN);
        tabs.addTab("Issue Book",  Build_IssueSubPanel());
        tabs.addTab("Return Book", Build_ReturnSubPanel());
        tabs.addTab("Renew Book",  Build_RenewSubPanel());
        p.add(tabs, BorderLayout.CENTER);
        return p;
    }
    private JPanel Build_IssueSubPanel() 
    {
        JPanel f = Form_Panel();
        JTextField memberIdField = Styled_Input(), bookIdField = Styled_Input();
        JLabel result = Result_Label();
        JButton btn = Accent_Btn_Small("Issue Book");
        btn.addActionListener(e -> {
            boolean ok = controller.CheckOut(bookIdField.getText().trim(), memberIdField.getText().trim());
            result.setText(ok ? "✅ Book issued successfully!" : "❌ Could not issue. Check IDs, availability, or member fine.");
            result.setForeground(ok ? Theme.SUCCESS : Theme.DANGER);
        });
        Add_Row(f, "Member ID:", memberIdField); 
        Add_Row(f, "Book ID:", bookIdField);
        f.add(Box.createVerticalStrut(20)); f.add(btn); f.add(Box.createVerticalStrut(12)); f.add(result);
        return f;
    }
    private JPanel Build_ReturnSubPanel() 
    {        
        JPanel f = Form_Panel();
        JTextField recordIdField = Styled_Input();
        JLabel result = Result_Label();
        JButton btn = Accent_Btn_Small("Process Return");
        btn.addActionListener(e -> {
            boolean ok = controller.Return_Item(recordIdField.getText().trim());
            result.setText(ok ? "✅ Book returned successfully!" : "❌ Record not found or already returned.");
            result.setForeground(ok ? Theme.SUCCESS : Theme.DANGER);
        });
        Add_Row(f, "Record ID:", recordIdField);
        f.add(Box.createVerticalStrut(20)); f.add(btn); f.add(Box.createVerticalStrut(12)); f.add(result);
        return f;
    }
    private JPanel Build_RenewSubPanel() 
    {
        JPanel f = Form_Panel();
        JTextField recordIdField = Styled_Input();
        JLabel result = Result_Label();
        JButton btn = Accent_Btn_Small("Renew Loan");
        btn.addActionListener(e ->{
            boolean ok = controller.Renew_Item(recordIdField.getText().trim());
            result.setText(ok ? "✅ Loan renewed by 14 days!" : "❌ Cannot renew. May be overdue or no spare copies.");
            result.setForeground(ok ? Theme.SUCCESS : Theme.DANGER);
        });
        Add_Row(f, "Record ID:", recordIdField);
        f.add(Box.createVerticalStrut(20)); f.add(btn); f.add(Box.createVerticalStrut(12)); f.add(result);
        return f;
    }
    //  RECORDS 
    private JPanel Build_RecordsPanel()
    {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("📋 Borrow Records"), BorderLayout.NORTH);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setBackground(Theme.BG_DARK);
        JTextField searchField=new JTextField(18);  Style_TextField(searchField);
        JButton searchBtn= Small_Btn("Search: ", Theme.ACCENT);
        JButton allBtn= Small_Btn("All Records", new Color(99,102,241));
        JButton activeBtn=Small_Btn("Active: ", Theme.SUCCESS);
        JButton overdueBtn = Small_Btn("Overdue", Theme.DANGER);
        JButton refreshBtn = Small_Btn("Refresh", Theme.TEXT_MUTED);
        String[] cols = {"Record ID","Member ID","Book ID","Borrow Date","Due Date","Return Date","Status","Fine"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) 
        {
            public boolean isCellEditable(int r, int c)
            { 
                return false; 
            }
        };
        JTable table = Styled_Table(model);
        Runnable loadAll = () -> { model.setRowCount(0); controller.get_All_Records().forEach(r -> model.addRow(Record_Row(r))); 
        };
        loadAll.run();
        searchBtn.addActionListener(e -> {
        String q= searchField.getText().trim();
        if(q.isEmpty()){loadAll.run(); return;}
        model.setRowCount(0);
        controller.Search_Records(q).forEach(r->model.addRow(Record_Row(r)));
        });
        searchField.addActionListener(e-> searchBtn.doClick());
        allBtn.addActionListener(e -> {searchField.setText(" ");loadAll.run(); });
        activeBtn .addActionListener(e -> {searchField.setText(" "); model.setRowCount(0); controller.get_Active_Records().forEach(r -> model.addRow(Record_Row(r))); 
        });
        overdueBtn.addActionListener(e -> { searchField.setText(" ");model.setRowCount(0); controller.get_Overdue_Records().forEach(r -> model.addRow(Record_Row(r))); 
        });
        refreshBtn.addActionListener(e -> { searchField.setText(" ");loadAll.run(); });
        toolbar.add(searchField); toolbar.add(searchBtn); 
        toolbar.add(allBtn); toolbar.add(activeBtn); toolbar.add(overdueBtn); toolbar.add(refreshBtn);
        p.add(toolbar, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }
    private Object[] Record_Row(BorrowRecord r) 
    {
        return new Object[]{r.get_Record_Id(), r.get_Member_Id(), r.get_Book_Id(),
            r.get_Borrow_Date(), r.get_DueDate(),
            r.get_Return_Date() != null ? r.get_Return_Date() : "-",
            r.get_Status(),
            r.get_Fine_Amount() > 0 ? "Rs." + String.format("%.2f", r.get_Fine_Amount()) : "-"};
    }
    // REPORTS
    private JPanel Build_ReportsPanel() 
    {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("📊 Reports & Analytics"), BorderLayout.NORTH);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setBackground(Theme.BG_DARK);
        JButton overdueBtn = Small_Btn("Overdue Report", Theme.DANGER);
        JButton summaryBtn = Small_Btn("Summary Report", Theme.ACCENT);
        toolbar.add(overdueBtn); toolbar.add(summaryBtn);
        JTextArea area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setBackground(Theme.PANEL_BG);
        area.setForeground(Theme.TEXT_MAIN);
        area.setEditable(false);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        area.setText("Click a button above to generate a report.\nReports are also saved to the data/reports/ folder.");
        overdueBtn.addActionListener(e -> area.setText(controller.Generate_Overdue_Report()));
        summaryBtn.addActionListener(e -> area.setText(controller.Generate_Summary_Report()));
        p.add(toolbar, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p; 
    }
    // Shared GUI helpers
    static JTable Styled_Table(DefaultTableModel model) 
    {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(Theme.TEXT_MAIN);
        table.setBackground(Theme.TABLE_ROW);
        table.setGridColor(new Color(45, 45, 75));
        table.setRowHeight(32);
        table.setSelectionBackground(Theme.ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(Theme.PANEL_BG);
        table.getTableHeader().setForeground(Theme.TEXT_MUTED);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() 
        {
            public Component get_Table_CellRenderer_Component(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) 
            {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Theme.TABLE_ROW : Theme.TABLE_ALT);
                setForeground(sel ? Color.WHITE : Theme.TEXT_MAIN);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this; 
            }
        });
        return table;
    }
    static JButton Small_Btn(String text, Color color)
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color.darker());
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(7, 14, 7, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    static JButton Accent_Btn_Small(String text) 
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Theme.ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(11, 28, 11, 28));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
    static JPanel Form_Panel() 
    {
        JPanel f = new JPanel();
        f.setBackground(Theme.PANEL_BG);
        f.setBorder(new EmptyBorder(24, 32, 24, 32));
        f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));
        return f;
    }
    static JTextField Styled_Input() 
    {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(new Color(45, 45, 75));
        f.setForeground(Theme.TEXT_MAIN);
        f.setCaretColor(Theme.TEXT_MAIN);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70, 70, 100), 1),new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(400, 42));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }
    static void Add_Row(JPanel panel, String label, JTextField field)  
    {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl); panel.add(Box.createVerticalStrut(6)); panel.add(field); panel.add(Box.createVerticalStrut(14));
    }
    static JLabel Result_Label() 
    {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    static void Style_TextField(JTextField field) 
    {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(new Color(45, 45, 75));
        field.setForeground(Theme.TEXT_MAIN);
        field.setCaretColor(Theme.TEXT_MAIN);
        field.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createLineBorder(new Color(70, 70, 100), 1), new EmptyBorder(6, 10, 6, 10)));
    }
}
//  MEMBER DASHBOARD
class Member_Dashboard extends JFrame 
{
    private final Library_Controller controller;
    private final Member  member;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    public Member_Dashboard(Library_Controller controller, Member member) 
    {
        this.controller = controller;
        this.member     = member;
        UI();
    }
    private void UI() 
    {
        setTitle("Advanced Library Management System – Member Dashboard");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);
        root.add(Build_Sidebar(), BorderLayout.WEST);
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG_DARK);
        contentPanel.add(Build_HomePanel(),    "HOME");
        contentPanel.add(Build_SearchPanel(),  "SEARCH");
        contentPanel.add(Build_BorrowPanel(),  "BORROW");
        contentPanel.add(Build_HistoryPanel(), "HISTORY");
        contentPanel.add(Build_ProfilePanel(), "PROFILE");
        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);
        Show_Card("HOME");
    }
    private JPanel Build_Sidebar() 
    {
        JPanel side = new JPanel();
        side.setBackground(Theme.SIDE_BG);
        side.setPreferredSize(new Dimension(195, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(20, 0, 20, 0));
        JLabel logo = new JLabel("📖 LMS", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Theme.TEXT_MAIN);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel memberLbl = new JLabel(member.get_Name(), SwingConstants.CENTER);
        memberLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        memberLbl.setForeground(Theme.TEXT_MUTED);
        memberLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50,50,80));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        side.add(logo); side.add(memberLbl);
        side.add(Box.createVerticalStrut(14)); side.add(sep); side.add(Box.createVerticalStrut(14));
        String[][] nav = {  {"🏠", "Home","HOME"},  {"🔍", "Search Books", "SEARCH"}, {"📤", "Borrow/Renew", "BORROW"}, {"📋", "My History",   "HISTORY"}, {"👤", "My Profile",   "PROFILE"},};
        for (String[] item : nav) side.add(Nav_Btn(item[0] + "  " + item[1], item[2]));
        side.add(Box.createVerticalGlue());
        side.add(Logout_Btn());
        return side; 
    }
    private JButton Nav_Btn(String text, String card) 
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Theme.TEXT_MUTED);
        btn.setBackground(Theme.SIDE_BG);
        btn.setBorder(new EmptyBorder(12, 22, 12, 22));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(e -> Show_Card(card));
        btn.addMouseListener(new MouseAdapter() 
        {
            public void Mouse_Entered(MouseEvent e) 
            { 
                btn.setBackground(new Color(35,35,65)); 
                btn.setForeground(Theme.TEXT_MAIN); 
            }
            public void Mouse_Exited(MouseEvent e)  
            { 
                btn.setBackground(Theme.SIDE_BG);      
                btn.setForeground(Theme.TEXT_MUTED); 
            }
        });
        return btn; 
    }
    private JButton Logout_Btn()
    {
        JButton btn = new JButton("🚪  Logout");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Theme.DANGER);
        btn.setBackground(Theme.SIDE_BG);
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(e -> { dispose(); new Login_Frame(controller).setVisible(true); 
        });
        return btn;
    }
    private void Show_Card(String name) 
    { 
        cardLayout.show(contentPanel, name);  
    }
    private JLabel Page_Header(String t) 
    {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(Theme.TEXT_MAIN);
        l.setBorder(new EmptyBorder(0, 0, 20, 0));
        return l;
    }
    // HOME 
    private JPanel Build_HomePanel() 
    {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("Welcome, " + member.get_Name() + " 👋"), BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 16));
        cards.setBackground(Theme.BG_DARK);
        cards.add(Stat_Card("📚", "Books Borrowed",   String.valueOf(member.get_Borrowed_Book_Ids().size()), Theme.ACCENT));
        cards.add(Stat_Card("💰", "Outstanding Fine", "Rs." + String.format("%.2f", member.get_Fine_Amount()), Theme.DANGER));
        cards.add(Stat_Card("✅", "Account Status",   member.is_Active() ? "Active" : "Inactive", member.is_Active() ? Theme.SUCCESS : Theme.DANGER));
        List<BorrowRecord> overdue = controller.getMemberRecords(member.get_User_Id()).stream().filter(BorrowRecord::Is_Overdue).collect(Collectors.toList());
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Theme.BG_DARK);
        if (!overdue.isEmpty()) 
        {
            JPanel notice = new JPanel(new BorderLayout());
            notice.setBackground(new Color(80, 20, 20));
            notice.setBorder(new EmptyBorder(16, 20, 16, 20));
            JLabel noteLbl = new JLabel("⚠️  You have " + overdue.size() + " overdue book(s). Please return them to avoid further fines.");
            noteLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            noteLbl.setForeground(new Color(255, 180, 180));
            notice.add(noteLbl);
            south.add(notice, BorderLayout.NORTH);
        }
        p.add(cards, BorderLayout.CENTER);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }
    private JPanel Stat_Card(String icon, String label, String value, Color accent) 
    {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.PANEL_BG);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        JLabel ico = new JLabel(icon + "  " + label);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        ico.setForeground(Theme.TEXT_MUTED);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 28));
        val.setForeground(accent);
        JPanel left = new JPanel(new BorderLayout()); 
        left.setBackground(Theme.PANEL_BG);
        left.add(ico, BorderLayout.NORTH); left.add(val, BorderLayout.CENTER);
        JPanel bar = new JPanel(); 
        bar.setPreferredSize(new Dimension(4, 0)); bar.setBackground(accent);
        card.add(bar, BorderLayout.WEST); card.add(left, BorderLayout.CENTER);
        return card;
    }
    // SEARCH 
    private JPanel Build_SearchPanel() 
    {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("🔍 Search Books"), BorderLayout.NORTH);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setBackground(Theme.BG_DARK);
        JTextField searchField = new JTextField(20);
        Admin_Dashboard.Style_TextField(searchField);
        JButton searchBtn = Admin_Dashboard.Small_Btn("Search",   Theme.ACCENT);
        JButton allBtn    = Admin_Dashboard.Small_Btn("Show All", Theme.TEXT_MUTED);
        JComboBox<String> genreBox = new JComboBox<>();
        genreBox.addItem("All Genres");
        controller.get_Genres().forEach(genreBox::addItem);
        genreBox.setBackground(Theme.PANEL_BG);
        genreBox.setForeground(Theme.TEXT_MAIN);
        toolbar.add(searchField); toolbar.add(searchBtn);
        JLabel genreLbl = new JLabel("  Genre: "); genreLbl.setForeground(Theme.TEXT_MUTED);
        toolbar.add(genreLbl); toolbar.add(genreBox); toolbar.add(allBtn);
        String[] cols = {"Book ID","Title","Author","Year","Genre","Available"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) 
        {
            public boolean isCellEditable(int r, int c) 
            { 
                return false; 
            }
        };
        JTable table = Admin_Dashboard.Styled_Table(model);
        Refresh_BooksTable(model, controller.get_All_Books());
        searchBtn.addActionListener(e -> Refresh_BooksTable(model, controller.Search_Books(searchField.getText().trim())));
        allBtn   .addActionListener(e -> Refresh_BooksTable(model, controller.get_All_Books()));
        genreBox .addActionListener(e -> {
            String sel = (String) genreBox.getSelectedItem();
            if ("All Genres".equals(sel)) Refresh_BooksTable(model, controller.get_All_Books());
            else Refresh_BooksTable(model, controller.get_Books_By_Genre(sel));
        });
        p.add(toolbar, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }
    private void Refresh_BooksTable(DefaultTableModel model, List<Book> books)
    {
        model.setRowCount(0);
        for (Book b : books)
            model.addRow(new Object[]{b.get_Book_Id(), b.get_Title(), b.get_Author(), b.get_Year(), b.get_Genre(), b.Is_Available() ? "✅ Yes" : "❌ No"});
    }
    // BORROW / RENEW 
    private JPanel Build_BorrowPanel() 
    {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("📤 Borrow / Renew"), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.PANEL_BG);
        tabs.setForeground(Theme.TEXT_MAIN);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Request Borrow", Build_RequestBorrowPanel());
        tabs.addTab("Renew Book",     Build_RenewPanel());
        p.add(tabs, BorderLayout.CENTER);
        return p;
    }
    private JPanel Build_RequestBorrowPanel() 
    {
        JPanel f = Admin_Dashboard.Form_Panel();
        JTextField bookIdField = Admin_Dashboard.Styled_Input();
        JLabel result = Admin_Dashboard.Result_Label();
        JButton btn = Admin_Dashboard.Accent_Btn_Small("Request Borrow");
        btn.addActionListener(e -> {
            boolean ok = controller.CheckOut(bookIdField.getText().trim(), member.get_User_Id());
            result.setText(ok ? "✅ Book issued! Check your history for the record ID.": "❌ Cannot issue. Book may be unavailable or you have outstanding fines.");
            result.setForeground(ok ? Theme.SUCCESS : Theme.DANGER);
        });
        JLabel lbl = new JLabel("Book ID:"); lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setForeground(Theme.TEXT_MUTED); lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.add(lbl); f.add(Box.createVerticalStrut(6)); f.add(bookIdField);
        f.add(Box.createVerticalStrut(20)); f.add(btn); f.add(Box.createVerticalStrut(12)); f.add(result);
        return f; 
    }
    private JPanel Build_RenewPanel() 
    {
        JPanel f = Admin_Dashboard.Form_Panel();
        JTextField recordIdField = Admin_Dashboard.Styled_Input();
        JLabel result = Admin_Dashboard.Result_Label();
        JButton btn = Admin_Dashboard.Accent_Btn_Small("Renew");
        btn.addActionListener(e ->{
            boolean ok = controller.Renew_Item(recordIdField.getText().trim());
            result.setText(ok ? "✅ Loan renewed by 14 days!" : "❌ Cannot renew. May be overdue or no spare copies.");
            result.setForeground(ok ? Theme.SUCCESS : Theme.DANGER); });
        JLabel lbl = new JLabel("Record ID:"); lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setForeground(Theme.TEXT_MUTED); lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.add(lbl); f.add(Box.createVerticalStrut(6)); f.add(recordIdField);
        f.add(Box.createVerticalStrut(20)); f.add(btn); f.add(Box.createVerticalStrut(12)); f.add(result);
        return f;
    }
    // HISTORY 
    private JPanel Build_HistoryPanel() 
    {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("📋 My Borrowing History"), BorderLayout.NORTH);
        JButton refreshBtn = Admin_Dashboard.Small_Btn("↻ Refresh", Theme.TEXT_MUTED);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(Theme.BG_DARK); toolbar.add(refreshBtn);
        String[] cols = {"Record ID","Book ID","Borrow Date","Due Date","Return Date","Status","Fine"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { 
                return false; } };
        JTable table = Admin_Dashboard.Styled_Table(model);
        refreshHistory(model);
        refreshBtn.addActionListener(e -> refreshHistory(model));
        p.add(toolbar, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p; 
    }
    private void refreshHistory(DefaultTableModel model) 
    {
        model.setRowCount(0);
        for (BorrowRecord r : controller.getMemberRecords(member.get_User_Id()))
            model.addRow(new Object[]{r.get_Record_Id(), r.get_Book_Id(),
                r.get_Borrow_Date(), r.get_DueDate(),
                r.get_Return_Date() != null ? r.get_Return_Date() : "-",
                r.get_Status(),
                r.get_Fine_Amount() > 0 ? "Rs." + String.format("%.2f", r.get_Fine_Amount()) : "-"});
    }
    //  PROFILE 
    private JPanel Build_ProfilePanel() 
    {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        p.add(Page_Header("👤 My Profile"), BorderLayout.NORTH);
        JPanel card = new JPanel(new GridLayout(0, 2, 16, 16));
        card.setBackground(Theme.PANEL_BG);
        card.setBorder(new EmptyBorder(28, 28, 28, 28));
        Add_Info(card, "User ID:",     member.get_User_Id());
        Add_Info(card, "Name:",        member.get_Name());
        Add_Info(card, "Email:",       member.get_Email());
        Add_Info(card, "Student ID:",  member.get_Student_Id());
        Add_Info(card, "Fine Amount:", "Rs." + String.format("%.2f", member.get_Fine_Amount()));
        Add_Info(card, "Status:",      member.is_Active() ? "Active" : "Inactive");
        Add_Info(card, "Books Held:",  String.valueOf(member.get_Borrowed_Book_Ids().size()));
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBackground(Theme.BG_DARK);
        wrapper.add(card);
        p.add(wrapper, BorderLayout.CENTER);
        return p; 
    }
    private void Add_Info(JPanel panel, String label, String value) 
    {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Theme.TEXT_MUTED);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        val.setForeground(Theme.TEXT_MAIN);
        panel.add(lbl); panel.add(val);
    }
}


