package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BookImport")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookImport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookImportId;
    @Column(name = "importDate")
    private Date importDate;
    @Column(name = "isHandled")
    private boolean isHandled;
    @ManyToOne
    @JoinColumn(name = "providerID", nullable = false,foreignKey = @ForeignKey(name = "FK_BookImport_Provider"))
    private Provider provider;
    @OneToMany(mappedBy = "bookImport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportDetail> importDetails;
}
