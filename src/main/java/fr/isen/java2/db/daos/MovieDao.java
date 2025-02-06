package fr.isen.java2.db.daos;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

import fr.isen.java2.db.entities.Movie;
import fr.isen.java2.db.entities.Genre;


public class MovieDao {

	public List<Movie> listMovies() {
		List<Movie> movies = new ArrayList<>();
		String sql = "SELECT movie.idmovie, movie.title, movie.release_date, movie.duration, movie.director, movie.summary, " +
				"genre.idgenre, genre.name " +
				"FROM movie JOIN genre ON movie.genre_id = genre.idgenre";

		try (Connection connection = DataSourceFactory.getDataSource().getConnection();
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {

			while (resultSet.next()) {
				Genre genre = new Genre(resultSet.getInt("idgenre"), resultSet.getString("name"));
				Movie movie = new Movie(
						resultSet.getInt("idmovie"),
						resultSet.getString("title"),
						resultSet.getTimestamp("release_date").toLocalDateTime().toLocalDate(),
						genre,
						resultSet.getInt("duration"),
						resultSet.getString("director"),
						resultSet.getString("summary")
				);
				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}

	public List<Movie> listMoviesByGenre(String genreName) {
		List<Movie> movies = new ArrayList<>();
		String sql = "SELECT movie.idmovie, movie.title, movie.release_date, movie.duration, movie.director, movie.summary, " +
				"genre.idgenre, genre.name " +
				"FROM movie JOIN genre ON movie.genre_id = genre.idgenre WHERE genre.name = ?";

		try (Connection connection = DataSourceFactory.getDataSource().getConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, genreName);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Genre genre = new Genre(resultSet.getInt("idgenre"), resultSet.getString("name"));
					Movie movie = new Movie(
							resultSet.getInt("idmovie"),
							resultSet.getString("title"),
							resultSet.getTimestamp("release_date").toLocalDateTime().toLocalDate(),
							genre,
							resultSet.getInt("duration"),
							resultSet.getString("director"),
							resultSet.getString("summary")
					);
					movies.add(movie);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}


	public Movie addMovie(Movie movie) {
		String sql = "INSERT INTO movie (title, release_date, genre_id, duration, director, summary) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = DataSourceFactory.getDataSource().getConnection();
			 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, movie.getTitle());
			statement.setTimestamp(2, Timestamp.valueOf(movie.getReleaseDate().atStartOfDay()));
			statement.setInt(3, movie.getGenre().getId());
			statement.setInt(4, movie.getDuration());
			statement.setString(5, movie.getDirector());
			statement.setString(6, movie.getSummary());

			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating movie failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return new Movie(
							generatedKeys.getInt(1),
							movie.getTitle(),
							movie.getReleaseDate(),
							movie.getGenre(),
							movie.getDuration(),
							movie.getDirector(),
							movie.getSummary()
					);
				} else {
					throw new SQLException("Creating movie failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
