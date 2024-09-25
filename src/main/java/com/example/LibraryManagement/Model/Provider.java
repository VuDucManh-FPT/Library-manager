package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Provider")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int providerId;
    @Column(name = "name")
    private String providerName;
    @Column(name = "taxCode")
    private String taxCode;
    @Column(name = "note")
    private String providerNote;
    @OneToMany(mappedBy = "provider")
    private List<BookImport> bookImports;
}
