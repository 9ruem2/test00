<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>우리인재개발원</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="상세 설명이 여기 들어갑니다." />
<link rel="shortcut icon" href="./images/favicon.ico" type="image/x-icon" />
<link rel="icon" href="./images/woori.png" type="image/x-icon" />
<link href="./css/style.css" rel="stylesheet" type="text/css">
<link href="./css/update.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css">

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>

<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
</head>
<body>
<%@include file="menu.jsp"%>
<div class="container">
	<div class="main">
	<!-- 여기에 작성 -->
		
		<div id="updateform" class="form-group">
			<form action="./update" method="post">
				<input class="form-controll" type="text" value="${update.board_title }" name="title">
				<textarea id="summernote" class="form-controll" name="content">${update.board_content }</textarea>
				<input type="hidden" value="${update.board_no }" name="bno">
				<button type="submit" class="btn btn-success">수정하기</button>
			</form>
		</div>
		
		
	</div>	
</div>
<%@include file="footer.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
	  $('#summernote').summernote({
		  height: 500
	  });
	});
</script>
</body>
</html>