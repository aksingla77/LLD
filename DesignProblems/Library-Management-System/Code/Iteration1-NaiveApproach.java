import java.util.ArrayList;
import java.util.List;

// ======================== ENUMS ========================

enum BookStatus {
    AVAILABLE, ISSUED;
}

// ======================== BOOK ========================
// Represents the concept/metadata of a book (title, author, etc.)

class Book {
    String title;
    String author;
    List<BookItem> items;

    Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.items = new ArrayList<>();
    }

    BookItem addItem(String barcode) {
        BookItem item = new BookItem(barcode, this);
        items.add(item);
        return item;
    }

    BookItem getAvailableItem() {
        for (BookItem item : items)
            if (item.status == BookStatus.AVAILABLE) return item;
        return null;
    }
}

// ======================== BOOK ITEM ========================
// A physical copy of a Book. Uses composition (HAS-A Book), not inheritance.
// Why not "extends Book"? Because 3 copies of "Clean Code" would each
// duplicate title/author — wasteful. With HAS-A, they share one Book object.

class BookItem {
    String barcode;
    Book book;
    BookStatus status;

    BookItem(String barcode, Book book) {
        this.barcode = barcode;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }
}

// ======================== USER (Abstract) ========================

abstract class User {
    String userId;
    String name;
    String email;

    User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}

// ======================== MEMBER ========================

class Member extends User {
    int maxBooksAllowed;
    List<Loan> issuedLoans;

    Member(String userId, String name, String email, int maxBooks) {
        super(userId, name, email);
        this.maxBooksAllowed = maxBooks;
        this.issuedLoans = new ArrayList<>();
    }

    boolean canIssue() { return issuedLoans.size() < maxBooksAllowed; }
    int getIssuedLoansCount() { return issuedLoans.size(); }
}

// ======================== LIBRARIAN ========================

class Librarian extends User {

    Librarian(String userId, String name, String email) {
        super(userId, name, email);
    }

    void addBook(Library library, Book book) {
        library.addBook(book);
    }

    BookItem addBookItem(Book book, String barcode) {
        return book.addItem(barcode);
    }
}

// ======================== LOAN ========================

class Loan {
    Member member;
    BookItem bookItem;
    int issueDate;
    int dueDate;
    int returnDate; // -1 = not yet returned

    Loan(Member member, BookItem bookItem, int issueDate, int dueDate) {
        this.member = member;
        this.bookItem = bookItem;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = -1;
    }

    // If not yet returned, checks if today is past due. 
    // If already returned, checks if return date was past due.
    boolean isOverdue(int today) {
        int effectiveReturn = (returnDate == -1) ? today : returnDate;
        return effectiveReturn > dueDate;
    }
}

// ======================== LIBRARY ========================

class Library {
    List<Book> books;
    List<Member> members;
    List<Loan> loans;

    // Anyone can call new Library() — no control over how many instances exist
    Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.title + " (" + book.items.size() + " copies)");
    }

    void registerMember(Member member) {
        members.add(member);
        System.out.println("Member registered: " + member.name + " (max " + member.maxBooksAllowed + " books)");
    }

    List<Book> searchByTitle(String query) {
        List<Book> results = new ArrayList<>();
        for (Book book : books)
            if (book.title.toLowerCase().contains(query.toLowerCase()))
                results.add(book);
        return results;
    }

    List<Book> searchByAuthor(String query) {
        List<Book> results = new ArrayList<>();
        for (Book book : books)
            if (book.author.toLowerCase().contains(query.toLowerCase()))
                results.add(book);
        return results;
    }

    // ---- ISSUE ----
    Loan issueBook(Member member, BookItem bookItem, int today, int dueDays) {
        if (bookItem == null) {
            System.out.println("No available copy to issue.");
            return null;
        }
        if (!member.canIssue()) {
            System.out.println(member.name + " has reached borrow limit!");
            return null;
        }
        if (bookItem.status != BookStatus.AVAILABLE) {
            System.out.println("This book item is not available.");
            return null;
        }

        bookItem.status = BookStatus.ISSUED;
        Loan loan = new Loan(member, bookItem, today, today + dueDays);
        member.issuedLoans.add(loan);
        loans.add(loan);
        System.out.println(member.name + " borrowed \"" + bookItem.book.title + "\" [" + bookItem.barcode + "] (due day: " + loan.dueDate + ")");
        return loan;
    }

    // ---- RETURN----

    int returnBook(Loan loan, int today) {
        loan.returnDate = today;
        loan.bookItem.status = BookStatus.AVAILABLE;
        loan.member.issuedLoans.remove(loan);

        // Fine calculation hardcoded right here
        int fine = 0;
        int ratePerDay = 10;
        if (loan.isOverdue(today)) {
            int daysLate = loan.returnDate - loan.dueDate;
            fine = daysLate * ratePerDay;
        }

        System.out.println(loan.member.name + " returned \"" + loan.bookItem.book.title + "\" [" + loan.bookItem.barcode + "]");
        if (fine > 0) System.out.println("  Late fine: Rs." + fine);
        else System.out.println("  No fine. Returned on time!");
        return fine;
    }
}

// ======================== MAIN ========================

class Main {
    public static void main(String[] args) {
        Library library = new Library();

        // Librarian sets up the library
        Librarian librarian = new Librarian("L001", "Admin", "admin@library.com");

        Book b1 = new Book("Clean Code", "Robert Martin");
        librarian.addBook(library, b1);
        librarian.addBookItem(b1, "B001");
        librarian.addBookItem(b1, "B002");

        Book b2 = new Book("Harry Potter", "JK Rowling");
        librarian.addBook(library, b2);
        librarian.addBookItem(b2, "B003");

        Book b3 = new Book("Sapiens", "Yuval Harari");
        librarian.addBook(library, b3);
        librarian.addBookItem(b3, "B004");
        librarian.addBookItem(b3, "B005");
        System.out.println();

        // Register Members
        Member member1 = new Member("M001", "Akshit", "akshit@email.com", 5);
        Member member2 = new Member("M002", "Prof. Sharma", "sharma@email.com", 10);
        library.registerMember(member1);
        library.registerMember(member2);
        System.out.println();

        // Search
        System.out.println("=== Search by title: 'clean' ===");
        List<Book> results = library.searchByTitle("clean");
        for (Book b : results) System.out.println("  Found: " + b.title + " by " + b.author);
        System.out.println();

        // Issue books
        System.out.println("=== Issuing books ===");
        Loan loan1 = library.issueBook(member1, b1.getAvailableItem(), 1, 14);
        Loan loan2 = library.issueBook(member2, b2.getAvailableItem(), 1, 14);
        System.out.println();

        // Return on time
        System.out.println("=== Return on time ===");
        library.returnBook(loan2, 10);
        System.out.println();

        // Return late
        System.out.println("=== Return late ===");
        library.returnBook(loan1, 20);  // 6 days late → Rs.60
        System.out.println();

        // Librarian adds a new copy
        System.out.println("=== Librarian adds a new copy ===");
        librarian.addBookItem(b2, "B006");
        System.out.println("Harry Potter now has " + b2.items.size() + " copies");
    }
}

// ======================== PROBLEMS WITH THIS DESIGN ========================
// Uncomment this class and comment out Main above to run the problems demo.

/*
class ProblemsDemo {
    public static void main(String[] args) {

        // ========== PROBLEM 1: No Singleton ==========
        // Anyone can create multiple Library instances → data splits

        Library library1 = new Library();
        Library library2 = new Library();  // two separate libraries!

        Librarian librarian = new Librarian("L001", "Admin", "admin@lib.com");

        Book cleanCode = new Book("Clean Code", "Robert Martin");
        librarian.addBook(library1, cleanCode);
        cleanCode.addItem("B001");

        Book sapiens = new Book("Sapiens", "Yuval Harari");
        librarian.addBook(library2, sapiens);  // added to wrong instance!
        sapiens.addItem("B004");

        System.out.println("Search library1 for 'Sapiens': " + library1.searchByTitle("Sapiens").size() + " results");  // 0
        System.out.println("Search library2 for 'Sapiens': " + library2.searchByTitle("Sapiens").size() + " results");  // 1
        System.out.println("→ Data split across instances!\n");


        // ========== PROBLEM 2: No BookSearcher ==========
        // Want searchByGenre()? Must modify Library class. OCP violation.

        // library1.searchByGenre("fiction");  // ← DOESN'T COMPILE
        // To add this, you'd open Library and add a new method every time.


        // ========== PROBLEM 3: No FineCalculator ==========
        // Fine logic hardcoded in returnBook(). Can't swap policies.

        Library library = new Library();
        Member member = new Member("M001", "Akshit", "akshit@email.com", 5);
        library.registerMember(member);

        Book hp = new Book("Harry Potter", "JK Rowling");
        library.addBook(hp);
        hp.addItem("B003");

        Loan loan = library.issueBook(member, hp.getAvailableItem(), 1, 14); // due day 15
        int fine = library.returnBook(loan, 21);  // returned day 21, 6 days late

        System.out.println("Fine charged: Rs." + fine + " (6 days × Rs.10/day, hardcoded)");
        System.out.println("→ Want flat Rs.50 fee? Must rewrite returnBook() internals. OCP violation.");
    }
}
*/
