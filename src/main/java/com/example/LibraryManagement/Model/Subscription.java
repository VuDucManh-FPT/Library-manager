package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionId;
    @ManyToOne
    @JoinColumn(name = "studentID",nullable = false,foreignKey = @ForeignKey(name = "FK_Subscription_Student"))
    private Student student;
    @ManyToOne
    @JoinColumn(name = "membershipID",nullable = false,foreignKey = @ForeignKey(name = "FK_Subscription_MembershipInfo"))
    private MembershipInfo membershipInfo;
    @JoinColumn(name = "startDate")
    private Date startDate;
    @JoinColumn(name = "endDate")
    private Date endDate;
}
