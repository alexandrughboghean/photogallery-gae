package com.example.photogallery.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UploadImageServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("imageUpload");
		
        if(blobKeys.isEmpty())
        	throw new ServletException();
        
        Key userImagesKey = KeyFactory.createKey("User", user.getEmail());
        Entity entity = new Entity("ImageGallery", userImagesKey);
        BlobKey blobImage = blobKeys.get(0);
        entity.setProperty("image", blobImage);
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(entity);
		
        resp.sendRedirect("/photogallery");
	}
}
