<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Spring-Mvc Multi Threads Test Application </title>
</head>
<body>

	<div>
		<h2>Spring-Mvc Multi Threads Test Application</h2>
		<h3>Please select a file of txt-format to upload. Warning! After you click the button 'Upload' below some threads will start to operate with selected file which is going to be changed and also it'll contain some specific generated hash.</h3>
		<br />
		<form:form method="post" enctype="multipart/form-data"
			modelAttribute="uploadedFile" action="fileUpload.htm">
			<table>
				<tr>
					<td>Upload:&nbsp;</td>
					<td><input type="file" name="file" />
					</td>
					<td style="color: red; font-style: oblique"><form:errors
							path="file" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="Upload" />
					</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html>
