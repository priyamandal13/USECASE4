<%@page import="com.rays.pro4.controller.PhysicianCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Stock Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1999:2025',
		});
	});
</script>

</head>


<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.PhysicianBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>
	<div align="center">

		<form action="<%=ORSView.PHYSICIAN_CTL%>" method="post">




			<h1>

				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<tr>
					<th><font size="5px"> Update Physician </font></th>
				</tr>
				<%
					} else {
				%>
				<tr>
					<th><font size="5px"> Add Physician </font></th>
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
				Map map = (Map) request.getAttribute("Physician");
			%>

			<table>
				<tr>
					<input type="hidden" name="id" value="<%=bean.getId()%>">
					<th align="left">Full Name<span style="color: red">*</span>:
					</th>
					<td><input type="text" name="fullName"
						placeholder="Enter Your Full Name" size="26"
						value="<%=(DataUtility.getStringData(bean.getFullName()))%>"></td>

					<td style="position: fixed"><font color="red"
						id="quantityError"><%=ServletUtility.getErrorMessage("fullName", request)%></font></td>

				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Birth Date <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="dob"
						placeholder="Enter Birth Date" size="26" readonly="readonly"
						id="udatee" value="<%=DataUtility.getDateString(bean.getBirthDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("dob", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Phone Number<span style="color: red">*</span> :
					</th>
					<td><input type="text" name="phone"
						placeholder="Enter Number" size="26"
						value="<%=(DataUtility.getStringData(bean.getPhone()))%>"></td>
					<td style="position: fixed"><font color="red"
						><%=ServletUtility.getErrorMessage("phone", request)%></font></td>

				</tr>




				<tr>
					<th style="padding: 3px"></th>
				</tr>




				<tr>
					<th align="left">Specialization<span style="color: red">*</span> :
					</th>
					<td>
						<%
							String hlist = HTMLUtility.getList2("specialization", DataUtility.getStringData(bean.getSpecialization()), map);
						%> <%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("specialization", request)%></font></td>
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
						name="operation" value="<%=PhysicianCtl.OP_UPDATE%>">
						&nbsp; &nbsp; <input type="submit" name="operation"
						value="<%=PhysicianCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=PhysicianCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=PhysicianCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</div>

	<%@ include file="Footer.jsp"%>

</body>
</html>