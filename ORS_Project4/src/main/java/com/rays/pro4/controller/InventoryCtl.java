package com.rays.pro4.controller;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.InventoryBean;
import com.rays.pro4.Bean.InventoryBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;

import com.rays.pro4.Model.InventoryModel;
import com.rays.pro4.Model.InventoryModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class InventoryCtl.
 * 
 * @author Priya mandal;
 * 
 */
@WebServlet(name ="InventoryCtl", urlPatterns ={"/ctl/InventoryCtl"})
public class InventoryCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(InventoryCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("InventoryCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("supplierName"))) {
			request.setAttribute("supplierName", PropertyReader.getValue("error.require", "Supplier Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("supplierName"))) {
			request.setAttribute("supplierName", "Supplier Name contains alphabet only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "Product"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("lastUpdatedDate"))) {
			request.setAttribute("lastUpdatedDate", PropertyReader.getValue("error.require", "Last Updated Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("lastUpdatedDate"))) {
			request.setAttribute("lastUpdatedDate", PropertyReader.getValue("error.date", "Last Updated Date"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "Quantity"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.integer", "Quantity"));
			pass = false;
		}

		

		log.debug("InventoryCtl Method validate Ended");

		return pass;
	}

	@Override
	protected void preload(HttpServletRequest request) {

		Map<Integer, String> map = new HashMap();


		map.put(1, "Shoes");
		map.put(2, "Mobile");
		map.put(3, "Laptop");
		map.put(4, "Watch");
		map.put(5, "Speaker");
		map.put(6, "Book");

		request.setAttribute("invent", map);


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */

	/**
	 * This is Populate Bean
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("InventoryCtl Method populatebean Started");

		InventoryBean bean = new InventoryBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setSupplierName(DataUtility.getString(request.getParameter("supplierName")));

		bean.setProduct(DataUtility.getInt(request.getParameter("product")));

		bean.setLastUpdatedDate(DataUtility.getDate(request.getParameter("lastUpdatedDate")));

		bean.setQuantity(DataUtility.getInt(request.getParameter("quantity")));

		log.debug("InventoryCtl Method populatebean Ended");

		return bean;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("InventoryCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		InventoryModel model = new InventoryModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Inventory Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			InventoryBean bean;
			try {
				bean = model.findByPK(id);
				System.out.println(bean);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("InventoryCtl Method doGet Ended");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		log.debug("InventoryCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		InventoryModel model = new InventoryModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			InventoryBean bean = (InventoryBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");

					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Inventory is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);

					/*
					 * bean = model.findByPK(pk);
					 */
					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Inventory is successfully Added", request);
					/*
					 * ServletUtility.forward(getView(), request, response);
					 */ }
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Inventory is successfully saved", request);
				 */

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				System.out.println(" U ctl D post 4444444");
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" U ctl D p 5555555");

			InventoryBean bean = (InventoryBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView. INVENTORY_LIST_CTL,request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.INVENTORY_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("InventoryCtl Method doPostEnded");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.INVENTORY_VIEW;
	}

}
	
	
	


