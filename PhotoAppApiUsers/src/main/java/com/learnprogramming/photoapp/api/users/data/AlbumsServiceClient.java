package com.learnprogramming.photoapp.api.users.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learnprogramming.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name="albums-ws",fallbackFactory=AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

	@GetMapping("users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}


@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {



	@Override
	public AlbumsServiceClient create(Throwable arg0) {
		// TODO Auto-generated method stub
		return new AlbumServiceClientCallback(arg0);
	}
	
}


class AlbumServiceClientCallback implements AlbumsServiceClient{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Throwable throwable;
	public AlbumServiceClientCallback(Throwable throwable) {
		
		this.throwable = throwable;
		
	}
	
	
	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		// TODO Auto-generated method stub
		
		if (this.throwable instanceof FeignException && ((FeignException) this.throwable).status() == 404) {
		 logger.error("404 error took place with user id " + id + "Error Message: " + this.throwable.getLocalizedMessage());	
		}
		else {
			 logger.error("Other error took place " + id + "Error Message: " + this.throwable.getLocalizedMessage());	
		}
		return new ArrayList<>();
	}
}