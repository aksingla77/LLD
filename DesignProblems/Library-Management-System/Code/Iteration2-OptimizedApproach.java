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
    List<Loan> issuedBooks;

    Member(String userId, String name, String email, int maxBooks) {
        super(userId, name, email);
        this.maxBooksAllowed = maxBooks;
        this.issuedBooks = new ArrayList<>();
    }

    boolean canIssue() { return issuedBooks.size() < maxBooksAllowed; }
    int getIssuedBooksCount() { return issuedBooks.size(); }
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

    boolean isOverdue(int today) {
        int effectiveReturn = (returnDate == -1) ? today : returnDate;
        return effectiveReturn > dueDate;
    }
}

// ======================== FINE ========================
// Interface so we can swap fine policies easily (OCP)

interface FineCalculator {
    int calculateFine(Loan loan, int today);
}

class PerDayFineCalculator implements FineCalculator {
    int ratePerDay;

    PerDayFineCalculator(int ratePerDay) { this.ratePerDay = ratePerDay; }

    public int calculateFine(Loan loan, int today) {
        if (!loan.isOverdue(today)) return 0;
        int returnDay = (loan.returnDate == -1) ? today : loan.returnDate;
        return (returnDay - loan.dueDate) * ratePerDay;
    }
}

class incrementalFineCalculator implements FineCalculator {
    int initialFine;
    int incrementPerDay;

    incrementalFineCalculator(int initialFine, int incrementPerDay) {
        this.initialFine = initialFine;
        this.incrementPerDay = incrementPerDay;
    }

    public int calculateFine(Loan loan, int today) {
        if (!loan.isOverdue(today)) return 0;
        int daysLate = ((loan.returnDate == -1) ? today : loan.returnDate) - loan.dueDate;
        return initialFine + (daysLate - 1) * incrementPerDay;  // e.g. Rs.50 for first day late, then Rs.20 for each additional day
    }
}

// ======================== CATALOG ========================
// Handles searching books. Uses an interface so new search types can be added (OCP)

interface BookSearcher {
    List<Book> search(List<Book> books, String query);
}

class SearchByTitle implements BookSearcher {
    public List<Book> search(List<Book> books, String query) {
        List<Book> results = new ArrayList<>();
        for (Book book : books)
            if (book.title.toLowerCase().contains(query.toLowerCase()))
                results.add(book);
        return results;
    }
}

class SearchByAuthor implements BookSearcher {
    public List<Book> search(List<Book> books, String query) {
        List<Book> results = new ArrayList<>();
        for (Book book : books)
            if (book.author.toLowerCase().contains(query.toLowerCase()))
                results.add(book);
        return results;
    }
}

class SearchByGenre implements BookSearcher {
    public List<Book> search(List<Book> books, String query) {
        List<Book> results = new ArrayList<>();
        for (Book book : books)
            if (book.author.toLowerCase().contains(query.toLowerCase()))
                results.add(book);
        return results;
    }
}

// ======================== LIBRARY ========================
// Private constructor + getInstance → only one library should exist

class Library {
    static Library instance = null;
    List<Book> books;
    List<Member> members;
    List<Loan> loans;  // keeps all loans (active + returned) as history

    private Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    static Library getInstance() {
        if (instance == null) instance = new Library();
        return instance;
    }

    void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.title + " (" + book.items.size() + " copies)");
    }

    void registerMember(Member member) {
        members.add(member);
        System.out.println("Member registered: " + member.name + " (max " + member.maxBooksAllowed + " books)");
    }

    List<Book> searchBooks(BookSearcher searcher, String query) {
        return searcher.search(books, query);
    }

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
        member.issuedBooks.add(loan);
        loans.add(loan);
        System.out.println(member.name + " borrowed \"" + bookItem.book.title + "\" [" + bookItem.barcode + "] (due day: " + loan.dueDate + ")");
        return loan;
    }

    int returnBook(Loan loan, FineCalculator fineCalc, int today) {
        loan.returnDate = today;
        loan.bookItem.status = BookStatus.AVAILABLE;
        loan.member.issuedBooks.remove(loan);

        int fine = fineCalc.calculateFine(loan, today);
        System.out.println(loan.member.name + " returned \"" + loan.bookItem.book.title + "\" [" + loan.bookItem.barcode + "]");
        if (fine > 0) System.out.println("  Late fine: Rs." + fine);
        else System.out.println("  No fine. Returned on time!");
        return fine;
    }
}

// ======================== MAIN ========================

class Main {
    public static void main(String[] args) {
        Library library = Library.getInstance();

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
        List<Book> results = library.searchBooks(new SearchByGenre(), "clean");
        for (Book b : results) System.out.println("  Found: " + b.title + " by " + b.author);
        System.out.println();

        // Issue books
        System.out.println("=== Issuing books ===");
        Loan loan1 = library.issueBook(member1, b1.getAvailableItem(), 1, 14);
        Loan loan2 = library.issueBook(member2, b2.getAvailableItem(), 1, 14);
        System.out.println();

        // Return on time
        System.out.println("=== Return on time ===");
        library.returnBook(loan2, new PerDayFineCalculator(10), 10);
        System.out.println();

        // Return late
        System.out.println("=== Return late ===");
        library.returnBook(loan1, new PerDayFineCalculator(10), 20);  // 5 days late → Rs.50
        System.out.println();

        // Librarian adds a new copy
        System.out.println("=== Librarian adds a new copy ===");
        librarian.addBookItem(b2, "B006");
        System.out.println("Harry Potter now has " + b2.items.size() + " copies");
    }
}
