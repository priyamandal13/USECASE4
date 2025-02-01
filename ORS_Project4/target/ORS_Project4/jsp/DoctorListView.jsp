<%@page import="com.rays.pro4.Model.DoctorModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Bean.DoctorBean"%>
<%@page import="com.rays.pro4.Bean.BaseBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.DoctorListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Doctor List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

<link rel="stylesheet" href="/resources/demos/style.css">
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2024',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.DoctorBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.DOCTOR_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Doctor List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>

			<%
				List dlist = (List) request.getAttribute("Namee");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
				
				
			%>
			
			<% Map map = (Map) request.getAttribute("doc"); %>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<DoctorBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<tr>
				     <th></th>
					<td align="center">
				     &emsp; <label>Name</font> :
					</label> <%=HTMLUtility.getList("id", DataUtility.getStringData(bean.getName()),dlist )%>
				
				
					<label>MobileNo</font> :
					</label> <input type="text" name="mobileNo" placeholder="Enter MobileNo"
						value="<%=ServletUtility.getParameter("mobileNo", request)%>">

						
						&nbsp; <label>Expertise</font> :
					</label><%=HTMLUtility.getList2("expertise", DataUtility.getStringData(bean.getExpertise()), map)%>
						
						


						&nbsp; <input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation" value="<%=DoctorListCtl.OP_RESET%>">
					</td>
				</tr>
				&nbsp;
				&nbsp;
			</table>
&nbsp;


			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: orange">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>Name</th>
					<th>DateOfBirth</th>
					<th>MobileNo</th>
					<th>Expertise</th>
					<th>Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>


				<tr align="center">
			<%--<td><%=map.get(Integer.parseInt(bean.getImportance()))%></td> --%>
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>
					<td><%=index++%></td>
					<td><%=bean.getName()%></td>
					<td><%=bean.getDateOfBirth()%></td>
					<td><%=bean.getMobileNo()%></td>
					<td><%=map.get(bean.getExpertise())%></td>
					<td><a href="DoctorCtl?id=<%=bean.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>

			<table width="100%">
				<tr>
					<th></th>
					<%
						if (pageNo == 1) {
					%>
					<td><input type="submit" name="operation" disabled="disabled"
						value="<%=DoctorListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=DoctorListCtl.OP_BACK%>"></td>
			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">
	</form>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>

	</center>

	<%@include file="Footer.jsp"%>
</body>
</html>
