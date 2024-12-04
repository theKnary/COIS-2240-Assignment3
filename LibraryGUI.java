import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
		Tab transactionsTab = new Tab("Transactions");

		tabPane.getTabs().add(booksTab);
		tabPane.getTabs().add(membersTab);
		tabPane.getTabs().add(transactionsTab);

		VBox mainPane = new VBox(tabPane);
		Scene scene = new Scene(mainPane, 1000, 800);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public VBox MembersView() {
		// Members Table
		TableView<Member> memberTable = new TableView<Member>();
		TableColumn<Member, Integer> idCol = new TableColumn<Member, Integer>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Member, Integer>("id"));
		TableColumn<Member, String> nameCol = new TableColumn<Member, String>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Member, String>("name"));

		memberTable.getColumns().add(idCol);
		memberTable.getColumns().add(nameCol);

		// Add Member Form
		Label memberIDLabel = new Label("Member ID:");
		TextField memberIDField = new TextField();

		Label memberNameLabel = new Label("Member Name:");
		TextField memberNameField = new TextField();

		EventHandler<ActionEvent> onAddMemberHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Member newMember = new Member(Integer.parseInt(memberIDField.getText()), memberNameField.getText());
				library.addMember(newMember);
				memberTable.getItems().add(newMember);
			}
		};

		memberNameField.setOnAction(onAddMemberHandler);

		// Add member button
		Button amBtn = new Button();
		amBtn.setText("Add Member");
		amBtn.setOnAction(onAddMemberHandler);

		// Add member container
		HBox addMemberContainer = new HBox();
		addMemberContainer.getChildren().addAll(memberIDLabel, memberIDField, memberNameLabel, memberNameField, amBtn);

		// Main container
		VBox mainMembersContainer = new VBox();
		mainMembersContainer.getChildren().add(memberTable);
		mainMembersContainer.getChildren().add(addMemberContainer);

		return mainMembersContainer;
	}

	public VBox BooksView() {
		// Books Table
		TableView<Book> bookTable = new TableView<Book>();
		TableColumn<Book, Integer> idCol = new TableColumn<Book, Integer>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
		TableColumn<Book, String> titleCol = new TableColumn<Book, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

		bookTable.getColumns().add(idCol);
		bookTable.getColumns().add(titleCol);

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
				Book newBook;
				try {
					newBook = new Book(Integer.parseInt(bookIDField.getText()), bookTitleField.getText());
					library.addBook(newBook);
					bookTable.getItems().add(newBook);
				} catch (Exception e) {
					errorMessage.setText(e.getMessage());
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

		// Main container
		VBox mainBooksContainer = new VBox();
		mainBooksContainer.getChildren().add(bookTable);
		mainBooksContainer.getChildren().add(addBookContainer);
		mainBooksContainer.getChildren().add(errorMessage);

		return mainBooksContainer;
	}

}