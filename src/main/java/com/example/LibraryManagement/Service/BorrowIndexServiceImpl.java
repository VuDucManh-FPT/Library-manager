package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowIndexServiceImpl implements BorrowIndexService{
    @Autowired
    private BorrowIndexRepository borrowIndexRepository;

    public List<BorrowIndex> getAllBorrowIndex() {
        return borrowIndexRepository.findAllBorrowIndex();
    }
}
