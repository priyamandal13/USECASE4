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
import com.rays.pro4.Bean.DoctorBean;
import com.rays.pro4.Bean.DoctorBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;

import com.rays.pro4.Model.DoctorModel;
import com.rays.pro4.Model.DoctorModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class DoctorCtl.
 * 
 * @author Priya Mandal
 * 
 */
@WebServlet(name = "DoctorCtl", urlPatterns = { "/ctl/DoctorCtl" })
public class DoctorCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(DoctorCtl.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("DoctorCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Name contains alphabet only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("expertise"))) {
			request.setAttribute("expertise", PropertyReader.getValue("error.require", "Expertise"));
			pass = false;
		} 
		
		if (DataValidator.isNull(request.getParameter("dateOfBirth"))) {
			request.setAttribute("dateOfBirth", PropertyReader.getValue("error.require", "DateOfBirth"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("dateOfBirth"))) {
			request.setAttribute("dateOfBirth", PropertyReader.getValue("error.date", "DateOfBirth"));
			pass = false;
		}
		
	

		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "MobileNo"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Mobile No. must be 10 Digit and No. Series start with 6-9");
			pass = false;
		}


		log.debug("DoctorCtl Method validate Ended");

		return pass;
	}
	
	@Override
	protected void preload(HttpServletRequest request) {
		
		
			Map<Integer, String> map = new HashMap();

			map.put(1, "Orthopediest");
			map.put(2, "Neurologiest");
			map.put(3, "HeartSpecialist");
			
			request.setAttribute("doc", map);
		
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */
	
	

	
	
	/**
	 *  This is Populate Bean
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("DoctorCtl Method populatebean Started");

		DoctorBean bean = new DoctorBean();

		bean.setId(DataUtility.getLong(request.getParameter("id"))); 
		
		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setExpertise(DataUtility.getInt(request.getParameter("expertise"))); 
		
		bean.setDateOfBirth(DataUtility.getDate(request.getParameter("dateOfBirth")));

		bean.setMobileNo(DataUtility.getLong(request.getParameter("mobileNo")));

	

		log.debug("DoctorCtl Method populatebean Ended");

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
		log.debug("DoctorCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		DoctorModel model = new DoctorModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Doctor Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			DoctorBean bean;
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
		log.debug("DoctorCtl Method doGet Ended");
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

		log.debug("DoctorCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));


		DoctorModel model = new DoctorModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			DoctorBean bean = (DoctorBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");
					
					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Doctor is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);
					
					bean = model.findByPK(pk);
					
					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Doctor is successfully Added", request);
				}	

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

			DoctorBean bean = (DoctorBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.DOCTOR_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("DoctorCtl Method doPostEnded");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.DOCTOR_VIEW;
	}

}


