import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
		@Order(1)
		void testBookAvail() {
			assertEquals(book.getTitle(), "book");
		}

		@Test
		@Order(2)
		void testMemberExists() {
			assertEquals(member.getName(), "member");
		}

		@Test
		@Order(3)
		void testBorrow() {
			boolean didBorrowBook = transaction.borrowBook(book, member);
			assertTrue(didBorrowBook);
			assertFalse(book.isAvailable());
		}

		@Test
		@Order(4)
		void testBorrowFail() {
			boolean didBorrowBook = transaction.borrowBook(book, member);
			assertFalse(didBorrowBook);
			assertFalse(book.isAvailable());
		}

		@Test
		@Order(5)
		void testReturn() {
			boolean didReturnbook = transaction.returnBook(book, member);
			assertTrue(didReturnbook);
			assertTrue(book.isAvailable());
		}

		@Test
		@Order(6)
		void testReturnFail() {
			boolean didReturnBook = transaction.returnBook(book, member);
			assertFalse(didReturnBook);
			assertTrue(book.isAvailable());
		}
	}

}
