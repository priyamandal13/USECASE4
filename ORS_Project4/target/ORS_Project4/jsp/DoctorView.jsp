<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.DoctorCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Doctor Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2080:2010',
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.DoctorBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>


	<center>

		<form action="<%=ORSView.DOCTOR_CTL%>" method="post">



			<div align="center">
				<h1>

					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<tr>
						<th><font size="5px"> Update Doctor </font></th>
					</tr>
					<%
						} else {
					%>
					<tr>
						<th><font size="5px"> Add Doctor </font></th>
					</tr>
					<%
						}
					%>
				</h1>

				<h3>
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"> <%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

				<%
					Map map = (Map) request.getAttribute("doc");
				%>

			</div>


			<table>
				<tr>
					<input type="hidden" name="id" value="<%=bean.getId()%>">
					<th align="left">Name<span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="name"
						placeholder="Enter name " size="25"
						oninput="handleLetterInputs(this, 'nameError', 20)"
						onblur="validateLetterInput(this, 'nameError', 20)"
						value="<%=DataUtility.getStringData(bean.getName())%>">
						<font color="red" id="nameError"> <%=ServletUtility.getErrorMessage("name", request)%></td>
				</tr>
				

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Date Of Birth <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="dateOfBirth"
						placeholder="Enter Date Of Birth" size="26" readonly="readonly"
						id="udatee"
						value="<%=DataUtility.getDateString(bean.getDateOfBirth())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("dateOfBirth", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>
				
					<tr>
					<th align="left">Mobile No<span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="mobileNo"
						placeholder="Enter MoblieNo" size="26"
						value="<%=(DataUtility.getStringData(bean.getMobileNo()).equals("0")?"" : bean.getMobileNo())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("mobileNo", request)%></font></td>

				</tr>
				
				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Expertise<span style="color: red">*</span> :
					</th>
					<td>
						<%
						String hlist = HTMLUtility.getList2("expertise", DataUtility.getStringData(bean.getExpertise()),map);
						%> 
						<%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("expertise", request)%></font></td>
				</tr>
				
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				
				



				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=DoctorCtl.OP_UPDATE%>">
						&nbsp; &nbsp; <input type="submit" name="operation"
						value="<%=DoctorCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=DoctorCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=DoctorCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</center>

	<%@ include file="Footer.jsp"%>
</body>
</html>