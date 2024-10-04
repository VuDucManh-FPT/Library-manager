package com.example.LibraryManagement.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @OneToMany(mappedBy = "accountStates")
    private List<Student> students;
    @OneToMany(mappedBy = "accountStates")
    private List<Staff> staffs;
}
