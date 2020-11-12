package projet.ynov.dizifymusicapi.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.ynov.dizifymusicapi.entity.Album;
import projet.ynov.dizifymusicapi.entity.Artist;
import projet.ynov.dizifymusicapi.entity.Favorite;
import projet.ynov.dizifymusicapi.entity.Title;
import projet.ynov.dizifymusicapi.entity.User;
import projet.ynov.dizifymusicapi.entity.params.FavoriteParams;
import projet.ynov.dizifymusicapi.exceptions.ResourceNotFoundException;
import projet.ynov.dizifymusicapi.repositories.AlbumRepository;
import projet.ynov.dizifymusicapi.repositories.ArtistRepository;
import projet.ynov.dizifymusicapi.repositories.FavoriteRepository;
import projet.ynov.dizifymusicapi.repositories.TitleRepository;
import projet.ynov.dizifymusicapi.repositories.UserRepository;

@RestController
@RequestMapping("/api")
public class FavoriteController {

	@Autowired
	private FavoriteRepository favoriteRepository;
	@Autowired
	private ArtistRepository artistRepository;
	@Autowired
	private AlbumRepository albumRepository;
	@Autowired
	private TitleRepository titleRepository;
	@Autowired
	private UserRepository userRepository;
	
	
	/**
	 * Get all Favorite list.
	 *
	 * @return the list
	 */
	@GetMapping("/favorites")
	public List<Favorite> getAllFavorites() {
		return favoriteRepository.findAll();
    }

	/**
	 * Gets Favorite by id.
	 *
	 * @param FavoriteId the Favorite id
	 * @return the Favorites by id
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@GetMapping("/favorites/{id}")
	public ResponseEntity<Favorite> getFavoritesById(@PathVariable(value = "id") Long favoriteId) throws ResourceNotFoundException {
		Favorite favorite = favoriteRepository
			  				.findById(favoriteId)
	  						.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Favorite not found with id : " + favoriteId));
	  
		return ResponseEntity.ok().body(favorite);
	}

	/**
	 * Create Favorite.
	 *
	 * @param params the FavoriteParams
	 * @return the Favorite
	 * @throws Exception 
	 */
	@PostMapping("/favorites")
	public Favorite createFavorite(@Validated @RequestBody FavoriteParams params) throws Exception {
		User user = userRepository
			  				.findById(params.getUser_id())
	  						.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "User not found with id : " + params.getUser_id()));
		
		params.setCreatedAt(new Date());
		params.setUpdatedAt(new Date());

		Favorite favorite = new Favorite(params);
		favorite.setUser(user);
		
		if (params.getAlbum_id() != 0L) {
			Album album = albumRepository
			  					.findById(params.getAlbum_id())
								.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Album not found with id : " + params.getAlbum_id()));
			
			favorite.setAlbum(album);
		} else if (params.getArtist_id() != 0L) {
			Artist artist = artistRepository
			  					.findById(params.getArtist_id())
								.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Artist not found with id : " + params.getArtist_id()));

			favorite.setArtist(artist);
		} else if (params.getTitle_id() != 0L) {
			Title title = titleRepository
			  					.findById(params.getTitle_id())
								.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Title not found with id : " + params.getTitle_id()));

			favorite.setTitle(title);
		} else {
			throw new Exception("album_id or title_id or artist_id must be not null");
		}
		
		
		return favoriteRepository.save(favorite);
	}

	/**
	 * Delete Favorite map.
	 *
	 * @param FavoriteId the Favorite id
	 * @return the map
	 * @throws Exception the exception
	 */
	@DeleteMapping("/favorites/{id}")
	public Map<String, Boolean> deleteFavorite(@PathVariable(value = "id") Long favoriteId) throws Exception {
	    Favorite favorite = favoriteRepository
	            			.findById(favoriteId)
	            			.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Favorite not found with id : " + favoriteId));

	    favoriteRepository.delete(favorite);
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("deleted", Boolean.TRUE);
	    return response;
	}
}