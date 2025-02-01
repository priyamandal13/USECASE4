
package com.rays.pro4.controller;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.OrderBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.OrderModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "OrderCtl", urlPatterns = { "/ctl/OrderCtl" })
public class OrderCtl extends BaseCtl {
	// private static Logger log = Logger.getLogger(OrderCtl.class);
	
	
	



	

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "product"));
			pass = false;
		}  else if (DataValidator.isTooLong(request.getParameter("product"),20)) {
			  request.setAttribute("product", "Product must contain max 20 charactor ");
			  pass = false; 
			  }
		if (DataValidator.isNull(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.require", "orderDate"));
			pass = false;
		} else if (!DataValidator.isAge(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.require", "Minimum Age 18 Year"));			
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "Quantity"));
			pass = false;
		} 
		
		
		/*
		 * else if (!DataValidator.isLong(request.getParameter("quantity"))) {
		 * request.setAttribute("quantity", " quantity contains digit only "); pass=
		 * false; }
		 */	
		
		  else if (DataValidator.isTooLong(request.getParameter("quantity"),16)) {
		  request.setAttribute("quantity", "quantity must contain max 16 digit  ");
		  pass = false; }
		

		if(DataValidator.isNull(request.getParameter("customer"))) {
			request.setAttribute("customer", PropertyReader.getValue("error.require", "Customer"));
		}

		return pass; 
	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		OrderBean bean = new OrderBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setProductName(DataUtility.getString(request.getParameter("productName")));

		bean.setCoustomer(DataUtility.getInt(request.getParameter("coustomer")));

		bean.setOrderDate(DataUtility.getDate(request.getParameter("orderDate")));

		bean.setQuantity(DataUtility.getInt(request.getParameter("quantity")));


		return bean;

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("in get method");

		String op = DataUtility.getString(request.getParameter("operation"));
		System.out.println("=============================================");
		OrderModel model = new OrderModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println(id);
		if (id > 0 || op != null) {
			System.out.println("-------------------------------------------------------");

			OrderBean bean;
			try {

				bean = model.findByPK(id);
				System.out.println(" FIND kiya");
				System.out.println(bean);
				ServletUtility.setBean(bean, request);

				ServletUtility.forward(getView(), request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;

		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		OrderModel model = new OrderModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			OrderBean bean = (OrderBean) populateBean(request);

			try {
				if (id > 0) {
					 bean.setId(id);
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Order is successfully Updated", request);
					

				} else {
					long pk = model.add(bean);
                    ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Order is successfully Added", request);
					//ServletUtility.forward(getView(), request, response);

				}

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login already exists", request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			OrderBean bean = (OrderBean) populateBean(request);
			try {
				model.delete(bean);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ServletUtility.redirect(ORSView.ORDER_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.ORDER_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

	}
	@Override
	protected String getView() {
		return ORSView.ORDER_VIEW;
	}

}
