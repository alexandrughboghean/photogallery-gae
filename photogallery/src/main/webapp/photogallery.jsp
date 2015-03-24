<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link type="text/css" rel="stylesheet" href="/stylesheets/style.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Photo Gallery</title>
</head>
<body>

	<h2>Welcome to my Photo Gallery</h2>
	<c:choose>
		<c:when test="${not empty requestScope.user}">
			<p>
				Hello ${requestScope.user }. You can <a
					href="${requestScope.urlAction }">sign out</a>
			</p>
		</c:when>
		<c:otherwise>
			<c:redirect url="/signin.jsp" />
		</c:otherwise>
	</c:choose>
	
	<div id="uploadImage">
	<h3>Image Upload</h3>
	<form action="${requestScope.urlUpload }" method="post" enctype="multipart/form-data">
		<span>Select an image to upload: <input type="file" name="imageUpload" /></span>
		<input type="submit" value="Upload Image" />
	</form>
	</div>
	
	<div id="imageGallery">
		<h3>Photo Gallery</h3>
		<c:forEach items="${requestScope.images}" var="item">
			<img src="${item }" width="250px" height="250px"/>
		</c:forEach>
	</div>
</body>
</html>