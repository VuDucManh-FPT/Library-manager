package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "MembershipPayment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "membershipID",nullable = false)
    private MembershipInfo membershipInfo;
    //Int chu nhi ?
    @JoinColumn(name = "duration")
    private String duration;
    // DB dang de la int price
    @JoinColumn(name = "price")
    private double price;
}

