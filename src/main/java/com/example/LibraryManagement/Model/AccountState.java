package com.example.LibraryManagement.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AccountState")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountState {
    @Id
    @Column(name = "stateID")
    private int accountStateId;
    @Column(name = "name")
    private String accountStateName;
    @Column(name = "note")
    private String note;
}
