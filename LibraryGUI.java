import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LibraryGUI extends Application {
	private Library library = new Library();
	TableView<Member> memberTable = new TableView<Member>();
	TableView<Book> bookTable = new TableView<Book>();
	ListView<String> transactionsList = new ListView<String>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Library Management System");

		// Tabs
		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		Tab booksTab = new Tab("Books", BooksView());
		Tab membersTab = new Tab("Members", MembersView());
		Tab transactionsTab = new Tab("Transactions", TransactionsView());

		tabPane.getTabs().add(booksTab);
		tabPane.getTabs().add(membersTab);
		tabPane.getTabs().add(transactionsTab);

		HBox mainPane = new HBox();
		mainPane.getChildren().add(BorrowReturnPanel());
		mainPane.getChildren().add(tabPane);

		Scene scene = new Scene(mainPane, 1000, 800);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void updateTables() {
		memberTable.refresh();
		bookTable.refresh();
		transactionsList.refresh();
	}

	public VBox BorrowReturnPanel() {
		// Borrow Return panel
		Label mainLabel = new Label("Borrow/Return Book");
		mainLabel.setStyle("-fx-font-weight: bold");
		mainLabel.setPadding(new Insets(10, 10, 10, 10));

		// Borrow/Return Form
		Label memberIDLabel = new Label("Member ID:");
		memberIDLabel.setAlignment(Pos.CENTER_LEFT);
		TextField memberIDField = new TextField();

		Label bookIDLabel = new Label("Book ID:");
		bookIDLabel.setAlignment(Pos.CENTER_LEFT);
		TextField bookIDField = new TextField();

		// Error message to be displayed if invalid id etc.
		Text errorMessage = new Text("");
		errorMessage.setFill(Color.RED);
		errorMessage.setWrappingWidth(200);

		EventHandler<ActionEvent> onBorrowReturnHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Member member = library.findMemberById(Integer.parseInt(memberIDField.getText()));
				Book book = library.findBookById(Integer.parseInt(bookIDField.getText()));

				if (member == null)
					errorMessage.setText("Could not find member with that ID");
				if (book == null)
					errorMessage.setText(errorMessage.getText() + "  " + "Could not find book with that ID");
				else {
					if (!book.isAvailable()) {
						boolean isReturn = false;
						for (Book b : member.getBorrowedBooks()) {
							if (b.getId() == book.getId()) {
								isReturn = true;
							}
						}
						if (!isReturn)
							errorMessage.setText("Book is Unavailable");
						else
							Transaction.getTransaction().returnBook(book, member);
					} else {
						Transaction.getTransaction().borrowBook(book, member);
					}
				}
				updateTables();
			}
		};
		bookIDField.setOnAction(onBorrowReturnHandler);

		Button brBtn = new Button();
		brBtn.setText("Borrow/Return");
		brBtn.setOnAction(onBorrowReturnHandler);

		VBox mainBorrowReturnContainer = new VBox();
		mainBorrowReturnContainer.setAlignment(Pos.TOP_CENTER);
		mainBorrowReturnContainer.setPadding(new Insets(10, 10, 10, 10));
		mainBorrowReturnContainer.getChildren().addAll(mainLabel, memberIDLabel, memberIDField, bookIDLabel,
				bookIDField, brBtn, errorMessage);

		return mainBorrowReturnContainer;
	}

	public VBox MembersView() {
		// Members Table
		TableColumn<Member, Integer> idCol = new TableColumn<Member, Integer>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Member, Integer>("id"));
		TableColumn<Member, String> nameCol = new TableColumn<Member, String>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Member, String>("name"));
		TableColumn<Member, String> borBooksCol = new TableColumn<Member, String>("Borrowed Books");
		borBooksCol.setCellValueFactory(cellData -> {
			Member member = cellData.getValue();
			List<String> books = new ArrayList<String>();
			for (Book book : member.getBorrowedBooks()) {
				books.add(book.getTitle() + " (" + book.getId() + ")");
			}
			return new ReadOnlyObjectWrapper<String>(books.toString());
		});
		borBooksCol.setMinWidth(300);

		memberTable.getColumns().add(idCol);
		memberTable.getColumns().add(nameCol);
		memberTable.getColumns().add(borBooksCol);
		memberTable.setMinHeight(700);

		// Add Member Form
		Label memberIDLabel = new Label("Member ID:");
		TextField memberIDField = new TextField();

		Label memberNameLabel = new Label("Member Name:");
		TextField memberNameField = new TextField();

		// Error message to be displayed if invalid id etc.
		Text errorMessage = new Text("");
		errorMessage.setFill(Color.RED);

		EventHandler<ActionEvent> onAddMemberHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (memberIDField.getText().isEmpty() || memberNameField.getText().isEmpty())
					return;
				else {
					Member newMember = new Member(Integer.parseInt(memberIDField.getText()), memberNameField.getText());
					boolean didAddMember = library.addMember(newMember);
					if (didAddMember) {
						memberTable.getItems().add(newMember);
					} else {
						errorMessage.setText("Member with that ID already exists");
					}
					updateTables();
				}
			}
		};

		memberNameField.setOnAction(onAddMemberHandler);

		// Add member button
		Button amBtn = new Button();
		amBtn.setText("Add Member");
		amBtn.setOnAction(onAddMemberHandler);

		// Add member container
		HBox addMemberContainer = new HBox();
		addMemberContainer.setAlignment(Pos.BOTTOM_CENTER);
		addMemberContainer.getChildren().addAll(memberIDLabel, memberIDField, memberNameLabel, memberNameField, amBtn);

		// Main container
		VBox mainMembersContainer = new VBox();
		mainMembersContainer.getChildren().add(memberTable);
		mainMembersContainer.getChildren().add(addMemberContainer);
		mainMembersContainer.getChildren().add(errorMessage);

		return mainMembersContainer;
	}

	public VBox BooksView() {
		// Books Table
		TableColumn<Book, Integer> idCol = new TableColumn<Book, Integer>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
		TableColumn<Book, String> titleCol = new TableColumn<Book, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

		TableColumn<Book, String> statusCol = new TableColumn<Book, String>("Status");
		statusCol.setCellValueFactory(cellData -> {
			Book book = cellData.getValue();
			return new ReadOnlyObjectWrapper<String>(book.isAvailable() ? "Available" : "Borrowed");
		});

		bookTable.getColumns().add(idCol);
		bookTable.getColumns().add(titleCol);
		bookTable.getColumns().add(statusCol);
		bookTable.setMinHeight(700);

		// Add book form
		Label bookIDLabel = new Label("Book ID:");
		TextField bookIDField = new TextField();

		Label bookTitleLabel = new Label("Book Title:");
		TextField bookTitleField = new TextField();

		// Error message to be displayed if invalid id etc.
		Text errorMessage = new Text("");
		errorMessage.setFill(Color.RED);

		// Add book handler
		EventHandler<ActionEvent> onAddBookHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (bookIDField.getText().isEmpty() || bookTitleField.getText().isEmpty())
					return;
				else {
					Book newBook;
					try {
						newBook = new Book(Integer.parseInt(bookIDField.getText()), bookTitleField.getText());
						boolean didAddBook = library.addBook(newBook);
						if (didAddBook) {
							bookTable.getItems().add(newBook);
						} else {
							errorMessage.setText("Book with that ID already exists");
						}
					} catch (Exception e) {
						errorMessage.setText(e.getMessage());
					}
					updateTables();
				}

			}
		};

		bookTitleField.setOnAction(onAddBookHandler);

		Button abBtn = new Button();
		abBtn.setText("Add Book");
		abBtn.setOnAction(onAddBookHandler);

		// Add book form container
		HBox addBookContainer = new HBox();
		addBookContainer.getChildren().addAll(bookIDLabel, bookIDField, bookTitleLabel, bookTitleField, abBtn);
		addBookContainer.setAlignment(Pos.BOTTOM_CENTER);

		// Main container
		VBox mainBooksContainer = new VBox();
		mainBooksContainer.getChildren().add(bookTable);
		mainBooksContainer.getChildren().add(addBookContainer);
		mainBooksContainer.getChildren().add(errorMessage);

		return mainBooksContainer;
	}

	public VBox TransactionsView() {
		VBox mainTransactionsContainer = new VBox();

		transactionsList.setMinHeight(700);
		transactionsList.getItems().addAll(Transaction.getTransaction().displayTransactionHistory());

		mainTransactionsContainer.getChildren().add(transactionsList);

		return mainTransactionsContainer;
	}

}