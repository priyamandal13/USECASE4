package com.rays.pro4.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.PhysicianBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.PhysicianModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class PhysicianCtl.
 * 
 * @author priya mandal;
 * 
 */
@WebServlet(name = "PhysicianCtl", urlPatterns = { "/ctl/PhysicianCtl" })
public class PhysicianCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(PhysicianCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#preload(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		
		PhysicianModel model = new PhysicianModel();
		Map<Integer, String> map = new HashMap();

		map.put(1, "Diabetes");
		map.put(2, "Malaria");
		map.put(3, "Depression");
		map.put(4, "Covid-19");
		
		request.setAttribute("Physician", map);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("PhysicianCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "Full Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("fullName"))) {
			request.setAttribute("fullName", "Full Name contains alphabet only");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "BirthDate"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "BirthDate"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("phone"))) {
			request.setAttribute("phone", PropertyReader.getValue("error.require", "Phone"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("phone"))) {
			request.setAttribute("phone", "Phone No. must be 10 Digit and No. Series start with 6-9");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("specialization"))) {
			request.setAttribute("specialization", PropertyReader.getValue("error.require", "Specialization"));
			pass = false;
//			
		}

		log.debug("PhysicianCtl Method validate Ended");

		return pass;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */

	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("PhysicianCtl Method populatebean Started");

		PhysicianBean bean = new PhysicianBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setFullName(DataUtility.getString(request.getParameter("fullName")));

		bean.setBirthDate(DataUtility.getDate(request.getParameter("dob")));
		System.out.println("birthDate" + bean.getBirthDate());
		
		bean.setPhone(DataUtility.getString(request.getParameter("phone")));

		bean.setSpecialization(DataUtility.getString(request.getParameter("specialization")));


		log.debug("PhysicianCtl Method populatebean Ended");

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
		log.debug("PhysicianCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		PhysicianModel model = new PhysicianModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Physician Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			PhysicianBean bean;
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
		log.debug("PhysicianCtl Method doGet Ended");
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

		log.debug("PhysicianCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><<><>**********" + id + op);

		PhysicianModel model = new PhysicianModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			PhysicianBean bean = (PhysicianBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");
					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Physician is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);
					bean = model.findByPK(pk);
					
					// bean.setId(pk);
					 ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Physician is successfully Added", request);
					
				}
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Physician is successfully saved", request);
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

			PhysicianBean bean = (PhysicianBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.PHYSICIAN_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.PHYSICIAN_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("PhysicianCtl Method doPostEnded");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.PHYSICIAN_VIEW;
	}

}
	


