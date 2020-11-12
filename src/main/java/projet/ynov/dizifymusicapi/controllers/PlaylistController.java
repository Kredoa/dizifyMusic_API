package projet.ynov.dizifymusicapi.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.ynov.dizifymusicapi.entity.Playlist;
import projet.ynov.dizifymusicapi.entity.Title;
import projet.ynov.dizifymusicapi.entity.User;
import projet.ynov.dizifymusicapi.entity.params.PlaylistParams;
import projet.ynov.dizifymusicapi.exceptions.ResourceNotFoundException;
import projet.ynov.dizifymusicapi.repositories.PlaylistRepository;
import projet.ynov.dizifymusicapi.repositories.TitleRepository;
import projet.ynov.dizifymusicapi.repositories.UserRepository;

@RestController
@RequestMapping("/api")
public class PlaylistController {

	@Autowired
	private PlaylistRepository playlistRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TitleRepository titleRepository;
	
	/**
	 * Get all Playlist list.
	 *
	 * @return the list
	 */
	@GetMapping("/playlists")
	public List<Playlist> getAllPlaylists() {
		return playlistRepository.findAll();
    }

	/**
	 * Gets Playlist by id.
	 *
	 * @param PlaylistId the Playlist id
	 * @return the Playlists by id
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@GetMapping("/playlists/{id}")
	public ResponseEntity<Playlist> getPlaylistsById(@PathVariable(value = "id") Long playlistId) throws ResourceNotFoundException {
		Playlist playlist = playlistRepository
			  				.findById(playlistId)
	  						.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Playlist not found with id : " + playlistId));
	  
		return ResponseEntity.ok().body(playlist);
	}

	/**
	 * Create Playlist.
	 *
	 * @param params the PlaylistParams
	 * @return the Playlist
	 */
	@PostMapping("/playlists")
	public Playlist createPlaylist(@Validated @RequestBody PlaylistParams params) {
		User user = userRepository
			  				.findById(params.getUser_id())
	  						.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "User not found with id : " + params.getUser_id()));
		
		params.setCreatedAt(new Date());
		params.setUpdatedAt(new Date());
		
		Playlist playlist = new Playlist(params);
		playlist.setUser(user);
		
		if (params.getTitle_ids() != null) {
	    	List<Title> titles = titleRepository.findAllById(params.getTitle_ids());
	    	playlist.setTitles(new HashSet<Title>(titles));
	    }
		
		return playlistRepository.save(playlist);
	}

	/**
	 * Update Playlist response entity.
	 *
	 * @param PlaylistId the Playlist id
	 * @param PlaylistDetails the Playlist details
	 * @return the response entity
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@PutMapping("/playlists/{id}")
	public ResponseEntity<Playlist> updatePlaylist(@PathVariable(value = "id") Long playlistId, @RequestBody PlaylistParams playlistDetails)
			throws ResourceNotFoundException {
	    Playlist playlist = playlistRepository
	            			.findById(playlistId)
	            			.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Playlist not found with id : " + playlistId));

	    if (playlistDetails.getName() != null) {
	    	playlist.setName(playlistDetails.getName());
	    }
	    
	    if (playlistDetails.getTitle_ids() != null) {
	    	List<Title> titles = titleRepository.findAllById(playlistDetails.getTitle_ids());
	    	playlist.setTitles(new HashSet<Title>(titles));
	    }
	    
	    playlist.setUpdatedAt(new Date());
	    final Playlist updatedPlaylist = playlistRepository.save(playlist);
	    return ResponseEntity.ok(updatedPlaylist);
	}
	

	/**
	 * Delete Playlist map.
	 *
	 * @param PlaylistId the Playlist id
	 * @return the map
	 * @throws Exception the exception
	 */
	@DeleteMapping("/playlists/{id}")
	public Map<String, Boolean> deletePlaylist(@PathVariable(value = "id") Long playlistId) throws Exception {
	    Playlist playlist = playlistRepository
	            			.findById(playlistId)
	            			.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Playlist not found with id : " + playlistId));

	    playlistRepository.delete(playlist);
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("deleted", Boolean.TRUE);
	    return response;
	}
}