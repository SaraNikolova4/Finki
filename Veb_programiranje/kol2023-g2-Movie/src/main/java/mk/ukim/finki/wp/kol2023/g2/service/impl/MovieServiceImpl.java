package mk.ukim.finki.wp.kol2023.g2.service.impl;

import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidDirectorIdException;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.DirectorRepository;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }
    @Override
    public List<Movie> listAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(InvalidDirectorIdException::new);
    }

    @Override
    public Movie create(String name, String description, Double rating, Genre genre, Long director) {
        Director director1 = directorRepository.findById(director).orElseThrow(InvalidDirectorIdException :: new);
        return movieRepository.save(new Movie(name,description,rating,genre,director1));
    }

    @Override
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director) {
        Director director1 = directorRepository.findById(director).orElseThrow(InvalidDirectorIdException :: new);
        Movie movie1= movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        movie1.setName(name);
        movie1.setDescription(description);
        movie1.setDirector(director1);
        movie1.setGenre(genre);
        movie1.setRating(rating);
        movieRepository.save(movie1);
        return movie1;
    }

    @Override
    public Movie delete(Long id) {
        Movie movie1= movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        movieRepository.delete(movie1);
        return movie1;
    }

    @Override
    public Movie vote(Long id) {
        Movie movie1= movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        movie1.setVotes(movie1.getVotes() + 1);
        movieRepository.save(movie1);
        return movie1;
    }

    @Override
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre) {

        //zemi gi tie so ist zarn
        // zemi gi tie so pomal rejting

        if(rating == null && genre == null)
        {
            return movieRepository.findAll();
        }
        if(rating != null && genre == null)
        {
            return movieRepository.findByRatingLessThan(rating);
        }
        if(rating == null && genre !=null)
        {
            return movieRepository.findByGenreEquals(genre);
        }
        // da se vneseni dvete
        return movieRepository.findByGenreEqualsAndRatingIsLessThan(genre, rating);
    }
}
