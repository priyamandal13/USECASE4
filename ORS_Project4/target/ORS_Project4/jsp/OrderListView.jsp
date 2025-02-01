
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.OrderListCtl"%>
<%@page import="com.rays.pro4.Bean.*"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2004',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>

</script>


		<script>
	function validateNumericInput(inputField) {
		// Get the value entered by the user
		var inputValue = inputField.value;

		// Regular expression to check if the input is numeric
		var numericPattern = /^\d*$/;

		// Test the input value against the numeric pattern
		if (!numericPattern.test(inputValue)) {
			// If input is not numeric, clear the field
			inputField.value = inputValue.replace(/[^\d]/g, ''); // Remove non-numeric characters
		}
	}
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.OrderBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<form action="<%=ORSView.ORDER_LIST_CTL%>" method="post">

		<center>

			<div align="centre">
				<h1>Order List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>



				<%
					int pageNo = ServletUtility.getPageNo(request);
					int pageSize = ServletUtility.getPageSize(request);
					int index = ((pageNo - 1) * pageSize) + 1;

					List list = ServletUtility.getList(request);
					Iterator<OrderBean> it = list.iterator();

					if (list.size() != 0) {
				%>
				<table width="100%" align="center">
					<tr>
						<th></th>
						<label>product</font> :
						</label>
						<input type="text" name="productName"  onkeydown="return /[a-zA-Z]/i.test(event.key)" maxlength="20"
						
						placeholder="Enter  ProductName"
							value="<%=ServletUtility.getParameter("productName", request)%>">
						&nbsp;
					</tr>
						<td align="center"><label>OrderDate</font> :
						</label> <input type="text" name="orderDate" placeholder="Enter  OrderDate" 
							
							value="<%=ServletUtility.getParameter("orderDate", request)%>">


							<tr>
						<th></th>
						<label>Quantity</font> :
						</label>
						<input type="text" name="quantity"  oninput="validateNumericInput(this)"maxlength="17" placeholder="Enter  Quantity " 
							value="<%=ServletUtility.getParameter("quantity", request)%>">
						&nbsp;
					</tr>
					
						<tr>
						<th></th>
						<label>customer</font> :
						</label>
						<input type="text" name="coustomer"   maxlength="20"
						
						placeholder="Enter  coustomer"
							value="<%=ServletUtility.getParameter("coustomer", request)%>">
						&nbsp;
					</tr>



							&emsp; <input type="submit" name="operation"
							value="<%=OrderListCtl.OP_SEARCH%>"> &nbsp; <input
							type="submit" name="operation" value="<%=OrderListCtl.OP_RESET%>">
						</td>
					</tr>


					<table border="1" width="100%" align="centre" cellpadding=6px
						cellspacing=".2">
						<tr style="background: skyblue">
							<th><input type="checkbox" id="select_all" name="select">Select
								All</th>

							<th>S.No.</th>
							<th>Product</th>
							<th>OrderDate</th>
							<th>Quantity</th>
							<th>Customer</th>
							<th>Edit</th>
						</tr>
						<%
							while (it.hasNext()) {
									bean = it.next();
						%>
						<tr align="centre">

							<td><input type="checkbox" class="checkbox" name="ids"
								value="<%=bean.getId()%>"></td>

							<td><%=index++%></td>
							<td><%=bean.getProductName()%></td>
							<td><%=bean.getOrderDate()%></td>
							<td><%=bean.getQuantity()%></td>
							<td><%=bean.getCoustomer()%></td>

							<td><a href="OrderCtl?id=<%=bean.getId()%>">Edit</a></td>
						</tr>
						<%
							}
						%>
						<table width="100%">

							<tr>
								<th></th>
								<%
									if (pageNo == 1) {
								%>
								<td><input type="submit" name="operation"
									disabled="disabled" value="<%=OrderListCtl.OP_PREVIOUS%>"></td>
								<%
									} else {
								%>
								<td><input type="submit" name="operation"
									value="<%=OrderListCtl.OP_PREVIOUS%>"></td>
								<%
									}
								%>


								<td><input type="submit" name="operation"
									value="<%=OrderListCtl.OP_DELETE%>"></td>

								<td><input type="submit" name="operation"
									value="<%=OrderListCtl.OP_NEW%>"></td>

								<td align="right"><input type="submit" name="operation"
									value="<%=OrderListCtl.OP_NEXT%>"
									<%=(list.size() < pageSize) ? "disabled" : ""%>></td>




							</tr>
						</table>


						<%
							}
							if (list.size() == 0) {
						%>
						<td align="centre"><input type="submit" name="operation"
							value="<%=OrderListCtl.OP_BACK%>"> &nbsp; &nbsp;</td>
						<%
							}
						%>
						</tr>
					</table>
					<br>

				</table>

				</table>
	</form>
	</br>
	</br>
	</br>
	</br>

	</center>

	<%@include file="Footer.jsp"%>



</body>
</html>
