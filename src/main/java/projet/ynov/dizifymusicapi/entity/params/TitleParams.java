package projet.ynov.dizifymusicapi.entity.params;

import java.sql.Time;
import java.util.Date;


public class TitleParams {

    private long id;
    
    private String name;

    private Time duration;

    private long album_id;

    private long author_id;

    private Date createdAt;

    private Date updatedAt;
    
    public TitleParams() {
    	super();
    }
    

	public TitleParams(String name, Time duration, Date createdAt, Date updatedAt) {
		super();
		this.name = name;
		this.duration = duration;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Time getDuration() {
		return duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
	}

	public long getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(long author_id) {
		this.author_id = author_id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(long album_id) {
		this.album_id = album_id;
	}
    
}
