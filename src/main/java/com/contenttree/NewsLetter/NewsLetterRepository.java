package com.contenttree.NewsLetter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLetterRepository  extends JpaRepository<NewsLetter,Long> {

}
