import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LibraryManagementTest {
	@Nested
	class BookIdTest {
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
}
