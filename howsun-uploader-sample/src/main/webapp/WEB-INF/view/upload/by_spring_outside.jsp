<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>Spring上传文件</h1>
<form action="/upload_by_spring_outside" method="post" enctype="multipart/form-data">
文件：<input type="file" name="file" multiple="multiple"/><br/>
<!-- 文件2：<input type="file" name="file"/><br/>
文件3：<input type="file" name="file"/><br/> -->
<button type="submit">上传</button>
</form>
</body>
</html>