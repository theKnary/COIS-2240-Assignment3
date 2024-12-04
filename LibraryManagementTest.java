import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

class LibraryManagementTest {
	@Nested
	static class BookIdTest {
		@Test
		void testBoudaryBookIds() {
			try {
				// Book should be valid
				Book book = new Book(100, "book");
				assertEquals(book.getTitle(), "book");
			} catch (Exception e) {
				// should be no error
				assertNull(e);
			}

			try {
				// Book should be valid
				Book book = new Book(999, "book");
				assertEquals(book.getTitle(), "book");
			} catch (Exception e) {
				// should be no error
				assertNull(e);
			}

			try {
				// Book should be invalid
				Book book = new Book(1000, "book");
				assertNull(book);
			} catch (Exception e) {
				// Error should show
				assertEquals(e.getMessage(), "ID must be 100-999");
			}
		}

		@Test
		void testInvalidBookId() {

			try {
				// Book should be invalid
				Book book = new Book(0, "book");
				assertNull(book);
			} catch (Exception e) {
				// Error should show
				assertEquals(e.getMessage(), "ID must be 100-999");
			}
			try {
				// Book should be invalid
				Book book = new Book(5000, "book");
				assertNull(book);
			} catch (Exception e) {
				// Error should show
				assertEquals(e.getMessage(), "ID must be 100-999");
			}
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	static class BorrowReturnTest {
		static Book book;
		static Member member;
		static Transaction transaction;

		@BeforeAll
		static void Init() {
			// create book
			try {
				// should be valid
				book = new Book(100, "book");
				assertEquals(book.getTitle(), "book");
			} catch (Exception e) {
				assertNull(e);
			}

			// create member (valid)
			member = new Member(0, "member");
			assertEquals(member.getName(), "member");

			// create transaction
			transaction = Transaction.getTransaction();
		}

		@Test
		@Order(1)
		// check book is valid
		void testBookAvail() {
			assertEquals(book.getTitle(), "book");
		}

		@Test
		@Order(2)
		// check member is valid
		void testMemberExists() {
			assertEquals(member.getName(), "member");
		}

		@Test
		@Order(3)
		// book should borrow
		void testBorrow() {
			boolean didBorrowBook = transaction.borrowBook(book, member);
			assertTrue(didBorrowBook);
			// book should no longer be available
			assertFalse(book.isAvailable());
		}

		@Test
		@Order(4)
		// book should not borrow
		void testBorrowFail() {
			boolean didBorrowBook = transaction.borrowBook(book, member);
			assertFalse(didBorrowBook);
			// book should not be available
			assertFalse(book.isAvailable());
		}

		@Test
		@Order(5)
		// book should return
		void testReturn() {
			boolean didReturnbook = transaction.returnBook(book, member);
			assertTrue(didReturnbook);
			// book should now be available
			assertTrue(book.isAvailable());
		}

		@Test
		@Order(6)
		// book should not return
		void testReturnFail() {
			boolean didReturnBook = transaction.returnBook(book, member);
			assertFalse(didReturnBook);
			// book should still be available
			assertTrue(book.isAvailable());
		}
	}

	@Nested
	static class SingletonTransactionTest {
		@Test
		void testSingletonTransaction() {
			try {
				// constructor should be private
				Constructor<Transaction> constructor = Transaction.class.getDeclaredConstructor();
				assertEquals(constructor.getModifiers(), Modifier.PRIVATE);
			} catch (NoSuchMethodException | SecurityException e) {
				// should be no error
				assertNull(e);
			}

		}

		// BONUS
		@Test
		void testSingletonTransactionGet() {
			// transaction instances should be the same (prove singleton)
			Transaction transaction = Transaction.getTransaction();
			Transaction transaction2 = Transaction.getTransaction();
			assertEquals(transaction, transaction2);
		}
	}

}
