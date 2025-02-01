
<%@page import="com.rays.pro4.Model.PhysicianModel"%>
<%@page import="com.rays.pro4.Bean.PhysicianBean"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.PhysicianListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Physician List</title>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">


<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1999:2025',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.PhysicianBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.PHYSICIAN_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Physician List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>
			<%
				Map map = (Map) request.getAttribute("physician");
			%>

			<%
				List plist = (List) request.getAttribute("plist");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);

				Iterator<PhysicianBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<tr>
					<th></th>
					<td align="center"><label>FullName</font> : 
					</label> <input type="text" name="fullName" placeholder="Enter Full Name"
						value="<%=ServletUtility.getParameter("fullName", request)%>">

					<td align="center"><label>Specialization</font> :
					
					</label>
						<%
							String hlist = HTMLUtility.getList2("specialization", DataUtility.getStringData(bean.getSpecialization()), map);
						%> <%=hlist%>
				
					<%-- </label> <input type="text" name="illness" placeholder="Enter illness"
						value="<%=ServletUtility.getParameter("illness", request)%>"> --%>
					
					<label>Birth Date</font> :
					</label><input type="text" name="dob" id="udate"
						readonly="readonly" size="25" placeholder="Enter Birth Date"
						value="<%=ServletUtility.getParameter("dob", request)%>">
					
					
					<td align="center"><label>Phone No.</font> :
					</label> <input type="text" name="phone" placeholder="Enter Phone"
						value="<%=ServletUtility.getParameter("phone", request)%>">
					
					




						<input type="submit" name="operation"
						value="<%=PhysicianListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation"
						value="<%=PhysicianListCtl.OP_RESET%>"></td>
				</tr>
				
			
			</table>
			<br>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: skyblue">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>FullName</th>
					<th>Birth Date</th>
					<th>Phone No.</th>
					<th>Speialization</th>
					<th>Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>


				<tr align="center">
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>
					<td><%=index++%></td>
					<td><%=bean.getFullName()%></td>
					<td><%=bean.getBirthDate()%></td>
					<td><%=bean.getPhone()%></td>
					<td><%=map.get(Integer.parseInt(bean.getSpecialization()))%></td>
					<td><a href="PhysicianCtl?id=<%=bean.getId()%>">Edit</a></td>
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
						value="<%=PhysicianListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=PhysicianListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=PhysicianListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=PhysicianListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=PhysicianListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=PhysicianListCtl.OP_BACK%>"></td>
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
