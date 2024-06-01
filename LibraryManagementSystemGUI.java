import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Book {
    String name;
    int numCopies;

    Book(String name, int numCopies) {
        this.name = name;
        this.numCopies = numCopies;
    }
}

class Student {
    String name;
    int membershipNo;
    List<String> borrowedBooks;

    Student(String name, int membershipNo) {
        this.name = name;
        this.membershipNo = membershipNo;
        this.borrowedBooks = new ArrayList<>();
    }

    void searchBook(List<Book> books) {
        StringBuilder bookList = new StringBuilder();
        for (Book book : books) {
            bookList.append(book.name).append("\n");
        }
        JOptionPane.showMessageDialog(null, "Available Books:\n" + bookList.toString());
    }

    void borrowBook(List<Book> books) {
        String bookName = JOptionPane.showInputDialog(null, "Enter book name to borrow:");
        for (Book book : books) {
            if (book.name.equals(bookName)) {
                if (book.numCopies > 0) {
                    borrowedBooks.add(bookName);
                    book.numCopies--;
                    JOptionPane.showMessageDialog(null, "Book borrowed successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Book not available!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Book not available!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    void returnBook() {
        if (borrowedBooks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have not borrowed any books.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            StringBuilder borrowedBookList = new StringBuilder();
            for (String book : borrowedBooks) {
                borrowedBookList.append(book).append("\n");
            }
            String bookName = JOptionPane.showInputDialog(null, "Enter book name to return:\n\n" + borrowedBookList);
            if (borrowedBooks.contains(bookName)) {
                borrowedBooks.remove(bookName);
                JOptionPane.showMessageDialog(null, "Book returned successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "You have not borrowed this book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

public class LibraryManagementSystemGUI extends JFrame {
    private List<Book> books = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private static final String BOOKS_FILE = "books.txt";
    private static final String STUDENTS_FILE = "students.txt";

    public LibraryManagementSystemGUI() {
        loadBooksData();
        loadStudentsData();
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 10, 10)); // 2 rows, 1 column, and 10 pixels spacing

        JButton studentButton = new JButton("Student");
        studentButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase font size
        studentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleStudent();
            }
        });
        add(studentButton);

        JButton adminButton = new JButton("Admin");
        adminButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase font size
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAdmin();
            }
        });
        add(adminButton);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveBooksData();
                saveStudentsData();
            }
        });

        pack();
        setSize(500, 300); // Set initial size of the frame
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void handleStudent() {
        String studentName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (studentName != null && !studentName.trim().isEmpty()) {
            String membershipNoStr = JOptionPane.showInputDialog(this, "Enter your membership number:");
            if (membershipNoStr != null && !membershipNoStr.trim().isEmpty()) {
                try {
                    int membershipNo = Integer.parseInt(membershipNoStr);
                    boolean studentFound = false;
                    for (Student student : students) {
                        if (student.name.equals(studentName) && student.membershipNo == membershipNo) {
                            studentFound = true;
                            showStudentOptions(student);
                            break;
                        }
                    }
                    if (!studentFound) {
                        JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid membership number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void handleAdmin() {
        String password = JOptionPane.showInputDialog(this, "Enter admin password:");
        if (password != null && password.equals("admin123")) { // Replace "admin123" with your actual admin password
            showAdminOptions();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAdminOptions() {
        JFrame adminFrame = new JFrame("Admin Options");
        adminFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        adminFrame.setLayout(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column, and 10 pixels spacing

        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        adminFrame.add(addBookButton);

        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });
        adminFrame.add(removeBookButton);

        JButton viewBookDetailsButton = new JButton("View Book Details");
        viewBookDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewBookDetails();
            }
        });
        adminFrame.add(viewBookDetailsButton);

        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        adminFrame.add(addStudentButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
            }
        });
        adminFrame.add(backButton);

        adminFrame.pack();
        adminFrame.setSize(500, 300); // Set initial size of the frame
        adminFrame.setLocationRelativeTo(null); // Center the window on the screen
        adminFrame.setVisible(true);
    }

    private void addBook() {
        String bookName = JOptionPane.showInputDialog(null, "Enter book name:");
        if (bookName != null && !bookName.trim().isEmpty()) {
            String numCopiesStr = JOptionPane.showInputDialog(null, "Enter the number of copies:");
            if (numCopiesStr != null && !numCopiesStr.trim().isEmpty()) {
                try {
                    int numCopies = Integer.parseInt(numCopiesStr);
                    if (numCopies > 0) {
                        Book book = new Book(bookName, numCopies);
                        books.add(book);
                        JOptionPane.showMessageDialog(null, "Book added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid number of copies. Please enter a positive integer.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input for number of copies. Please enter a positive integer.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void removeBook() {
        List<String> allBooks = new ArrayList<>();
        for (Book book : books) {
            allBooks.add(book.name);
        }
        if (!allBooks.isEmpty()) {
            String bookToRemove = (String) JOptionPane.showInputDialog(null, "Select book to remove:", "Remove Book",
                    JOptionPane.QUESTION_MESSAGE, null, allBooks.toArray(), allBooks.get(0));
            if (bookToRemove != null) {
                books.removeIf(book -> book.name.equals(bookToRemove));
                JOptionPane.showMessageDialog(null, "Book removed successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No books available to remove!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewBookDetails() {
        StringBuilder bookDetails = new StringBuilder();
        for (Book book : books) {
            bookDetails.append(book.name).append(" - Copies: ").append(book.numCopies).append("\n");
        }
        JOptionPane.showMessageDialog(null, "Book Details:\n" + bookDetails.toString());
    }

    private void addStudent() {
        String studentName = JOptionPane.showInputDialog(null, "Enter student name:");
        if (studentName != null && !studentName.trim().isEmpty()) {
            String membershipNoStr = JOptionPane.showInputDialog(null, "Enter membership number:");
            if (membershipNoStr != null && !membershipNoStr.trim().isEmpty()) {
                try {
                    int membershipNo = Integer.parseInt(membershipNoStr);
                    Student newStudent = new Student(studentName, membershipNo);
                    students.add(newStudent);
                    JOptionPane.showMessageDialog(null, "Student added successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid membership number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showStudentOptions(Student student) {
        JFrame studentFrame = new JFrame("Student Options");
        studentFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        studentFrame.setLayout(new GridLayout(4, 1, 10, 10)); // 4 rows, 1 column, and 10 pixels spacing

        JButton searchBookButton = new JButton("Search Book");
        searchBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                student.searchBook(books);
            }
        });
        studentFrame.add(searchBookButton);

        JButton borrowBookButton = new JButton("Borrow Book");
        borrowBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                student.borrowBook(books);
            }
        });
        studentFrame.add(borrowBookButton);

        JButton returnBookButton = new JButton("Return Book");
        returnBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                student.returnBook();
            }
        });
        studentFrame.add(returnBookButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentFrame.dispose();
            }
        });
        studentFrame.add(backButton);

        studentFrame.pack();
        studentFrame.setSize(500, 300); // Set initial size of the frame
        studentFrame.setLocationRelativeTo(null); // Center the window on the screen
        studentFrame.setVisible(true);
    }
    private void loadBooksData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int numCopies = Integer.parseInt(parts[1]);
                    Book book = new Book(name, numCopies);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooksData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(book.name + "," + book.numCopies + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentsData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int membershipNo = Integer.parseInt(parts[1]);
                    Student student = new Student(name, membershipNo);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStudentsData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student student : students) {
                writer.write(student.name + "," + student.membershipNo + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LibraryManagementSystemGUI();
    }
}
