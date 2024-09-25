package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "MembershipInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "name")
    private String memName;
    @JoinColumn(name = "description")
    private String description;
    @OneToMany(mappedBy = "membershipInfo")
    private List<Subscription> subscriptions;
    @OneToMany(mappedBy = "membershipInfo")
    private List<MembershipPayment> membershipPayments;
}
