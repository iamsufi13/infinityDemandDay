package com.contenttree.NewsLetter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsLetterService {
    @Autowired
    NewsLetterRepository newsLetterRepository;
}
