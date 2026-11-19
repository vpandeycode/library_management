
package com.example.library;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    // Collection to store books
    private final List<Book> books = new ArrayList<>();

    // PART A/B: ADD A NEW BOOK
    // POST: http://localhost:8080/books

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {

        if (book.getId() == null) {
            return ResponseEntity.badRequest()
                    .body("Book ID is required.");
        }

        // Check for duplicate ID
        if (findBookIndex(book.getId()) != -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Book ID already exists.");
        }

        books.add(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(book);
    }


    // PART C: GET ALL BOOKS
    // GET: http://localhost:8080/books

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        return ResponseEntity.ok(books);
    }


    // PART C: GET BOOK BY ID
    // GET: http://localhost:8080/books/1

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {

        int index = findBookIndex(id);

        if (index == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found.");
        }

        return ResponseEntity.ok(books.get(index));
    }


    // PART D: UPDATE BOOK DETAILS
    // PUT: http://localhost:8080/books/1

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Integer id,
            @RequestBody Book updatedBook) {

        int index = findBookIndex(id);

        if (index == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found.");
        }

        // Use ID from the URL
        updatedBook.setId(id);

        books.set(index, updatedBook);

        return ResponseEntity.ok(updatedBook);
    }


    // PART E: DELETE BOOK BY ID
    // DELETE: http://localhost:8080/books/1

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Integer id) {

        int index = findBookIndex(id);

        if (index == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found. Cannot delete.");
        }

        books.remove(index);

        return ResponseEntity.ok(
                "Book deleted successfully."
        );
    }


    // HELPER METHOD: FIND BOOK INDEX BY ID

    private int findBookIndex(Integer id) {

        for (int i = 0; i < books.size(); i++) {

            if (books.get(i).getId().equals(id)) {
                return i;
            }
        }

        return -1;
    }
}
