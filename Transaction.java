import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Transaction {
	private static Transaction instance;
	private static final Path transactionsFilePath = Paths.get("transactions.txt");

	// Singleton constructor
	private Transaction() {
	}

	// Singleton of transaction instance
	public static synchronized Transaction getTransaction() {
		if (instance == null)
			instance = new Transaction();

		return instance;
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
	public boolean returnBook(Book book, Member member) {
		if (member.getBorrowedBooks().contains(book)) {
			member.returnBook(book);
			book.returnBook();
			String transactionDetails = getCurrentDateTime() + " - Returning: " + member.getName() + " returned "
					+ book.getTitle();
			System.out.println(transactionDetails);
			this.saveTransaction(transactionDetails);
			return true;
		} else {
			System.out.println("This book was not borrowed by the member.");
			return false;
		}
	}

	// Get the current date and time in a readable format
	private String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	// Display transaction history by reading from file
	public List<String> displayTransactionHistory() {
		File f = new File(transactionsFilePath.toString());

		System.out.println("----TRANSACTION HISTORY----");
		// if the file doesn't exist, history must be empty
		if (!f.exists())
			System.out.println("Transaction history empty.");
		try {
			List<String> transactionHistory = Files.readAllLines(transactionsFilePath);
			// print one transaction for each line
			for (String t : transactionHistory) {
				System.out.println(t.toString());
			}

			return transactionHistory;
		} catch (IOException e) {
			System.out.println("Error reading transaction file");
			e.printStackTrace();
			return null;
		}

	}

	// save transaction details to file system
	public void saveTransaction(String tDetails) {
		try {
			// write transaction details to file as line
			if (!Files.exists(transactionsFilePath, LinkOption.NOFOLLOW_LINKS))
				Files.createFile(transactionsFilePath);
			Files.write(transactionsFilePath, Arrays.asList(tDetails), StandardCharsets.UTF_8,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println("Error writing transaction file");
			e.printStackTrace();
		}
	}
}