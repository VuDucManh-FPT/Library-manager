package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import com.example.LibraryManagement.Service.BookService;
import com.example.LibraryManagement.Utils.FileUploadUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class BookController {
    private BookService bookService;
    private BookRepository bookRepository;
    private PublisherRepository publisherRepository;
    private BookImportRepository bookImportRepository;
    private ImportDetailRepository importDetailRepository;
    private ProviderRepository providerRepository;
    private BorrowIndexRepository borrowIndexRepository;

    @GetMapping("books")
    public String books(Model model) {
        List<Book> books = bookRepository.findAll();
        Map<Integer, Date> bookImportDates = new HashMap<>();

        for (Book book : books) {
            if (!book.getImportDetails().isEmpty()) {
                ImportDetail importDetail = book.getImportDetails().get(0);
                BookImport bookImport = importDetail.getBookImport();
                bookImportDates.put(book.getBookId(), bookImport.getImportDate());
            }
            if (book.getBookImages() != null && !book.getBookImages().isEmpty()) {
                book.setFirstImageName(book.getBookImages().get(0).getImageURL());
            } else {
                book.setFirstImageName("nullI.png");
            }
        }
        model.addAttribute("bookImportDates", bookImportDates);
        model.addAttribute("books", books);
        return "Admin/books";
    }
    @GetMapping("/book/add")
    public String addBook(Model model) {
        model.addAttribute("publishers", publisherRepository.findAll());
        model.addAttribute("book", new Book());
        return "Admin/book-add";
    }
    @PostMapping("/book/add")
    public String addBook(
            @ModelAttribute Book book,
            @RequestParam("publisherId") Long publisherId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") float price,
            @RequestParam("importDate") String importDateStr,
            Model model) throws ParseException {

        if (bookRepository.existsByBookName(book.getBookName())) {
            model.addAttribute("error", "A book with the same name already exists.");
            return "redirect:/admin//book/add";
        }
        Publisher publisher = publisherRepository.findById(Math.toIntExact(publisherId)).orElseThrow(() -> new RuntimeException("Publisher not found"));
        book.setPublisher(publisher);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date importDate = null;
        importDate = formatter.parse(importDateStr);
        String uploadDir = "src/main/resources/static/BookSto/assets/images/book-dec";
        try {
            // Lưu ảnh sách
            String fileName = file.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, file);

            if (book.getBookImages() == null) {
                book.setBookImages(new ArrayList<>());
            }

            // Tạo và liên kết BookImage với Book
            BookImage bookImage = new BookImage();
            bookImage.setImageURL(fileName);
            bookImage.setBook(book);
            book.getBookImages().add(bookImage);

            BookImport bookImport = new BookImport();
            bookImport.setImportDate(importDate);
            bookImport.setHandled(true);

            // Liên kết BookImport với Provider
            Provider provider = providerRepository.findById(1).orElseThrow();
            bookImport.setProvider(provider);
            int importPrive = (int) (price*quantity);
            // Tạo ImportDetail và liên kết với Book và BookImport
            ImportDetail importDetail = new ImportDetail();
            importDetail.setQuantity(quantity);
            importDetail.setPrice(importPrive);
            importDetail.setVat(0);
            importDetail.setBook(book);
            importDetail.setBookImport(bookImport);

            // Thêm ImportDetail vào BookImport
            List<ImportDetail> importDetails = new ArrayList<>();
            importDetails.add(importDetail);
            bookImport.setImportDetails(importDetails);

            // Lưu Book và BookImport (cả ImportDetail được lưu tự động)
            bookRepository.save(book);
            bookImportRepository.save(bookImport);

            // Thông báo thành công
            model.addAttribute("success", "Add book successfully");

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while storing the image!");
        }
        return "redirect:/admin/books";
    }
    @GetMapping("/book/edit/{id}")
    public String editBook(@PathVariable("id") Long bookId, Model model) {
        Book book = bookRepository.findById(Math.toIntExact(bookId))
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getBookImages() != null && !book.getBookImages().isEmpty()) {
            book.setFirstImageName(book.getBookImages().get(0).getImageURL());
        } else {
            book.setFirstImageName("nullI.png");
        }
        ImportDetail importDetail = importDetailRepository.findByBook(book);
        BookImport bookImport = importDetail.getBookImport();
        model.addAttribute("book", book);
        model.addAttribute("publishers", publisherRepository.findAll());
        model.addAttribute("bookImport", bookImport);
        model.addAttribute("importDetail", importDetail);
        return "Admin/book-edit";
    }
    @PostMapping("/book/edit/{id}")
    public String updateBook(
            @PathVariable("id") Long bookId,
            @ModelAttribute Book book,
            @RequestParam("publisherId") Long publisherId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") float price,
            @RequestParam("importDate") String importDateStr,
            Model model) throws ParseException {

        Book existingBook = bookRepository.findById(Math.toIntExact(bookId))
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        existingBook.setBookName(book.getBookName());
        existingBook.setPrice(price);

        Publisher publisher = publisherRepository.findById(Math.toIntExact(publisherId))
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        existingBook.setPublisher(publisher);

        if (file != null && !file.isEmpty()) {
            String uploadDir = "src/main/resources/static/BookSto/assets/images/book-dec";
            try {
                String fileName = file.getOriginalFilename();
                FileUploadUtil.saveFile(uploadDir, fileName, file);

                existingBook.getBookImages().clear();
                BookImage bookImage = new BookImage();
                bookImage.setImageURL(fileName);
                bookImage.setBook(existingBook);
                existingBook.getBookImages().add(bookImage);

            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error occurred while storing the image!");
                return "Admin/book-edit";
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date importDate = formatter.parse(importDateStr);
        BookImport bookImport = existingBook.getImportDetails().get(0).getBookImport();
        bookImport.setImportDate(importDate);
        bookImport.setHandled(true);

        ImportDetail importDetail = existingBook.getImportDetails().get(0);
        importDetail.setQuantity(quantity);
        importDetail.setPrice((int) (price * quantity));
        importDetail.setVat(0);

        bookRepository.save(existingBook);
        bookImportRepository.save(bookImport);

        model.addAttribute("success", "Book updated successfully");

        return "redirect:/admin/books";
    }
    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") Long bookId, Model model) {
        Book book = bookRepository.findById(Math.toIntExact(bookId))
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (borrowIndexRepository.existsBorrowIndexByBook(book)) {
            model.addAttribute("error", "Cannot delete the book because it has borrow records.");
            return "redirect:/admin/books";
        }
        List<ImportDetail> importDetails = book.getImportDetails();
        if (importDetails != null && !importDetails.isEmpty()) {
            importDetailRepository.deleteAll(importDetails);
        }

        for (ImportDetail importDetail : importDetails) {
            BookImport bookImport = importDetail.getBookImport();
            if (bookImport != null) {
                bookImportRepository.delete(bookImport);
            }
        }
        bookRepository.delete(book);
        model.addAttribute("success", "Book deleted successfully.");
        return "redirect:/admin/books";
    }
}
