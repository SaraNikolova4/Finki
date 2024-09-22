package mk.ukim.finki.wp.september2021.service.impl;

import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsCategory;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsCategoryIdException;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.september2021.repository.NewsCategoryRepository;
import mk.ukim.finki.wp.september2021.repository.NewsRepository;
import mk.ukim.finki.wp.september2021.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewServiceImpl implements NewsService {

    private final NewsCategoryRepository newsCategoryRepository;
    private final NewsRepository newsRepository;

    public NewServiceImpl(NewsCategoryRepository newsCategoryRepository, NewsRepository newsRepository) {
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> listAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
    }

    @Override
    public News create(String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        return newsRepository.save(new News(name,description,price,type,newsCategory));
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException :: new);
        news.setName(name);
        news.setDescription(description);
        news.setType(type);
        news.setCategory(newsCategory);
        news.setPrice(price);
        return newsRepository.save(news);
    }

    @Override
    public News delete(Long id) {
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException :: new);
        newsRepository.delete(news);
        return news;
    }

    @Override
    public News like(Long id) {
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException :: new);
        news.setLikes(news.getLikes() + 1);
        newsRepository.save(news);
        return news;
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {

        if(price == null && type ==null)
        {
            return newsRepository.findAll();
        }
        else if (price != null && type != null){
            return newsRepository.findByPriceLessThanAndType(price, type);
        }
        else if (price == null){
            return newsRepository.findByType(type);
        }
        else return newsRepository.findByPriceLessThan(price);
    }
}


