import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LibraryManagementTest {
	@Nested
	static class BookIdTest {
		@Test
		void testBoudaryBookIds() {

			try {
				Book book = new Book(100, "book");
				assertEquals(book.getTitle(), "book");
			} catch (Exception e) {
				assertNull(e);
			}
			try {
				Book book = new Book(999, "book");
				assertEquals(book.getTitle(), "book");
			} catch (Exception e) {
				assertNull(e);
			}

			try {
				Book book = new Book(1000, "book");
				assertNull(book);
			} catch (Exception e) {
				assertEquals(e.getMessage(), "ID must be 100-999");
			}
		}

		@Test
		void testInvalidBookId() {

			try {
				Book book = new Book(0, "book");
				assertNull(book);
			} catch (Exception e) {
				assertEquals(e.getMessage(), "ID must be 100-999");
			}
			try {
				Book book = new Book(5000, "book");
				assertNull(book);
			} catch (Exception e) {
				assertEquals(e.getMessage(), "ID must be 100-999");
			}
		}

	}

	@Nested
	static class BorrowReturnTest {
		static Book book;
		static Member member;
		static Transaction transaction;

		@BeforeAll
		static void Init() {
			// create book
			try {
				book = new Book(100, "book");
				assertEquals(book.getTitle(), "book");
			} catch (Exception e) {
				e.printStackTrace();
				assertNull(e);
			}

			// create member
			member = new Member(0, "member");
			assertEquals(member.getName(), "member");

			// create transaction
			transaction = Transaction.getTransaction();
		}

		@Test
		void testBookAvail() {
			assertEquals(book.getTitle(), "book");
		}

		@Test
		void testMemberExists() {
			assertEquals(member.getName(), "member");
		}

		@Test
		void testBorrow() {
			boolean didBorrowBook = transaction.borrowBook(book, member);
			assertTrue(didBorrowBook);
			assertFalse(book.isAvailable());
		}

		@Test
		void testBorrowFail() {
			boolean didBorrowBook = transaction.borrowBook(book, member);
			assertFalse(didBorrowBook);
			assertFalse(book.isAvailable());
		}

		@Test
		void testReturn() {
			boolean didReturnbook = transaction.returnBook(book, member);
			assertTrue(didReturnbook);
			assertTrue(book.isAvailable());
		}

		@Test
		void testReturnFail() {
			boolean didBorrowBook = transaction.borrowBook(book, member);
			assertFalse(didBorrowBook);
			assertTrue(book.isAvailable());
		}
	}

}
