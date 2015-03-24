package com.example.photogallery.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class SignInServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		
		if(user == null){
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}else{
			req.setAttribute("user", user.getEmail());
			req.setAttribute("urlAction", userService.createLogoutURL(req.getRequestURI()));
			req.setAttribute("urlUpload", blobstoreService.createUploadUrl("/uploadImage"));
			req.setAttribute("images", getImages(user.getEmail()));
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/photogallery.jsp");
			dispatcher.forward(req, resp);
		}
		
		
	}
	
	private List<String> getImages(String userEmail){
		List<String> images = new ArrayList<String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key userImagesKey = KeyFactory.createKey("User", userEmail);
	    Query query = new Query("ImageGallery", userImagesKey);
	    Iterator<Entity> imagesEntity = datastore.prepare(query).asIterator();
	    ImagesService imagesService = ImagesServiceFactory.getImagesService();
	    
	    
	    while(imagesEntity.hasNext()){
	    	Entity image = imagesEntity.next();	
	    	BlobKey imageBlob = (BlobKey) image.getProperty("image");
	    	Image oldImage = ImagesServiceFactory.makeImageFromBlob(imageBlob);
	    	ServingUrlOptions serve = ServingUrlOptions.Builder.withBlobKey(imageBlob);
	    	String url = imagesService.getServingUrl(serve);
	    	images.add(url);
	    }
		return images;    
	}
}
