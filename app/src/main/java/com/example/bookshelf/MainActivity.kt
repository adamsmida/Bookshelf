package com.example.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookshelf.ui.theme.BookshelfTheme

// Data class for a book with title, author, and cover image
data class Book(val title: String, val author: String, val coverImageResId: Int)

// Composable for displaying a single book item
@Composable
fun BookItem(book: Book) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display the book's cover image
        Image(
            painter = painterResource(id = book.coverImageResId),
            contentDescription = "Cover of ${book.title}",
            modifier = Modifier
                .size(64.dp)
                .padding(end = 16.dp)
        )

        // Display book title and author
        Column {
            Text(text = "Title: ${book.title}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Author: ${book.author}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Composable for displaying a list of books
@Composable
fun BookList(books: List<Book>) {
    LazyColumn {
        items(books.size) { index ->
            BookItem(book = books[index])
        }
    }
}

// Composable for searching through the list of books
@Composable
fun BookSearch(books: List<Book>, onSearchResults: (List<Book>) -> Unit) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    Column(modifier = Modifier.padding(16.dp)) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                val filteredBooks = books.filter {
                    it.title.contains(query.text, ignoreCase = true) ||
                            it.author.contains(query.text, ignoreCase = true)
                }
                onSearchResults(filteredBooks)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

// Main app composable
@Composable
fun BookshelfApp() {
    val books = listOf(
        Book("The Great Gatsby", "F. Scott Fitzgerald", R.drawable.the_great_gatsby),
        Book("To Kill a Mockingbird", "Harper Lee", R.drawable.o_kill_a_mockingbird),
        Book("1984", "George Orwell", R.drawable._984),
        Book("Pride and Prejudice", "Jane Austen", R.drawable.pride_and_prejudice),
        Book("Moby-Dick", "Herman Melville", R.drawable.moby_dick)
    )
    var displayedBooks by remember { mutableStateOf(books) }

    Column(modifier = Modifier.fillMaxSize()) {
        BookSearch(books = books) { filteredBooks ->
            displayedBooks = filteredBooks
        }
        Spacer(modifier = Modifier.height(8.dp))
        BookList(books = displayedBooks)
    }
}

// Main activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookshelfTheme {
                BookshelfApp()
            }
        }
    }
}

// Preview for the app in Android Studio
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BookshelfTheme {
        BookshelfApp()
    }
}
