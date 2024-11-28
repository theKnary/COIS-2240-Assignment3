import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Transaction {
	private static Transaction instance;

	// Singleton of transaction instance
	public static Transaction getTransaction() {
		if (instance == null)
			instance = new Transaction();

		return instance;
	}

	private Transaction() {

	}

	// Perform the borrowing of a book
	public boolean borrowBook(Book book, Member member) {
		if (book.isAvailable()) {
			book.borrowBook();
			member.borrowBook(book);
			String transactionDetails = getCurrentDateTime() + " - Borrowing: " + member.getName() + " borrowed "
					+ book.getTitle();
			System.out.println(transactionDetails);

			this.saveTransaction(transactionDetails);
			return true;
		} else {
			System.out.println("The book is not available.");
			return false;
		}
	}

	// Perform the returning of a book
	public void returnBook(Book book, Member member) {
		if (member.getBorrowedBooks().contains(book)) {
			member.returnBook(book);
			book.returnBook();
			String transactionDetails = getCurrentDateTime() + " - Returning: " + member.getName() + " returned "
					+ book.getTitle();
			System.out.println(transactionDetails);
			this.saveTransaction(transactionDetails);
		} else {
			System.out.println("This book was not borrowed by the member.");
		}
	}

	// Get the current date and time in a readable format
	private String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public void displayTransactionHistory() {
		// TODO Auto-generated method stub

	}

	// save transaction details to file system
	public void saveTransaction(String tDetails) {
		try {

			Path filePath = Paths.get("transactions.txt");
			Files.write(filePath, Arrays.asList(tDetails), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println("Error writing transaction file");
			e.printStackTrace();
		}
	}
}