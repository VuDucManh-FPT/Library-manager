package com.example.LibraryManagement.Service.impl;

import com.example.LibraryManagement.Model.Publisher;
import com.example.LibraryManagement.Repository.PublisherRepository;
import com.example.LibraryManagement.Service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }
}
