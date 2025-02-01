<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.InventoryCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Inventory Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2024:2026',
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.InventoryBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>


	<center>

		<form action="<%=ORSView.INVENTORY_CTL%>" method="post">



			<div align="center">
				<h1>

					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<tr>
						<th><font size="5px"> Update Inventory </font></th>
					</tr>
					<%
						} else {
					%>
					<tr>
						<th><font size="5px"> Add Inventory </font></th>
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
					Map map = (Map) request.getAttribute("invent");
				%>

			</div>


			<table>
				<tr>
					<input type="hidden" name="id" value="<%=bean.getId()%>">
					<th align="left">Supplier Name<span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="supplierName"
						placeholder="Enter Supplier Name"  size="26" maxlength="30"
						value="<%=DataUtility.getStringData(bean.getSupplierName())%>"
						></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("supplierName", request)%></font></td>

				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>
				
				<tr>
					<th align="left">Last Updated Date <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="lastUpdatedDate"
						placeholder="Enter Last Updated Date" size="26" readonly="readonly"
						id="udatee"
						value="<%=DataUtility.getDateString(bean.getLastUpdatedDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("lastUpdatedDate", request)%></font></td>
				</tr>

				

				<tr>
					<th style="padding: 3px"></th>
				</tr>
				
              	<tr>
					<th align="left">Quantity<span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="quantity" maxlength="30"
						placeholder="Enter Quantity" size="26"
						value="<%=(DataUtility.getStringData(bean.getQuantity()).equals("0")?"" : bean.getQuantity())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("quantity", request)%></font></td>

				</tr>
				

				<tr>
					<th style="padding: 3px"></th>
				</tr>
				
				
				<tr>
					<th align="left">Product<span style="color: red">*</span> :
					</th>
					<td>
						<%
						String hlist = HTMLUtility.getList2("product", DataUtility.getStringData(bean.getProduct()),map);
						%> 
						<%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("product", request)%></font></td>
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
						name="operation" value="<%=InventoryCtl.OP_UPDATE%>">
						&nbsp; &nbsp; <input type="submit" name="operation"
						value="<%=InventoryCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=InventoryCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=InventoryCtl.OP_RESET%>"></td>

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