package com.rays.pro4.Model;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

//import org.apache.log4j.Logger;

import com.rays.pro4.Bean.DoctorBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.EmailBuilder;
import com.rays.pro4.Util.EmailMessage;
import com.rays.pro4.Util.EmailUtility;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of DoctorModel.
 * 
 * @author priya mandal
 *
 */

public class DoctorModel {
	private static Logger log = Logger.getLogger(DoctorModel.class);

	/**
	 * Find next PK of Doctor
	 *
	 * @throws DatabaseException
	 */

	public int nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");

		String sql = "select max(id) from st_doctor";
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK Started");
		return pk + 1;

	}

	/**
	 * Doctor Add
	 *
	 * @param bean
	 * @throws DatabaseException
	 *
	 */

	/**
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(DoctorBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "insert into st_doctor values(?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setDate(3, new java.sql.Date(bean.getDateOfBirth().getTime()));
			pstmt.setLong(4, bean.getMobileNo());
			pstmt.setInt(5, bean.getExpertise());

			int i = pstmt.executeUpdate();
			System.out.println(i);
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			log.error("Database Exception ...", e);
			try {
				e.printStackTrace();
				conn.rollback();

			} catch (Exception e2) {
				e2.printStackTrace();
				// application exception
				throw new ApplicationException("Exception : add rollback exceptionn" + e2.getMessage());
			}
		}

		finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Add End");
		return pk;

	}

	/**
	 * Delete a Doctor
	 *
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(DoctorBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "delete from st_doctor where id=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete rollback Exception" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Delete End");
	}

	/**
	 * Find Doctor by PK
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public DoctorBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "select * from st_doctor where id=?";
		DoctorBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDateOfBirth(rs.getDate(3));
				bean.setMobileNo(rs.getLong(4));
				bean.setExpertise(rs.getInt(5));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting Doctor by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method Find By PK end");
		return bean;
	}

	/**
	 * Update a doctor
	 *
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(DoctorBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "update st_doctor set name=?, date_of_birth=?, mobile_no=?, expertise=? where id=? ";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bean.getName());
			pstmt.setDate(2, new java.sql.Date(bean.getDateOfBirth().getTime()));
			pstmt.setLong(3, bean.getMobileNo());
			pstmt.setInt(4, bean.getExpertise());
			pstmt.setLong(5, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("update doctor>> " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Update End ");
	}

	/**
	 * Search Doctor
	 *
	 * @param bean : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(DoctorBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Doctor with pagination
	 *
	 * @return list : List of Doctors
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 *
	 * @throws DatabaseException
	 */

	public List search(DoctorBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("select * from st_doctor where 1=1");
		if (bean != null) {

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" AND name like '" + bean.getName() + "%'");
			}
			
			if (bean.getExpertise() > 0) {
				sql.append(" AND expertise = " + bean.getExpertise());
			}
			
			if (bean.getDateOfBirth() != null && bean.getDateOfBirth().getTime() > 0 ) {
				Date d = new Date(bean.getDateOfBirth().getTime());
				sql.append(" AND Date_Of_Birth = " +d );
			}
			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
			}
			
			if (bean.getMobileNo() > 0) {
				sql.append(" AND Mobile_No = " + bean.getMobileNo());
			}

		}
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}
		System.out.println("sql query search >>= " + sql.toString());
		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDateOfBirth(rs.getDate(3));
				bean.setMobileNo(rs.getLong(4));
				bean.setExpertise(rs.getInt(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Search end");
		return list;

	}

	/**
	 * Get List of Doctor
	 *
	 * @return list : List of Doctor
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Doctor with pagination
	 *
	 * @return list : List of doctors
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_doctor");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DoctorBean bean = new DoctorBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDateOfBirth(rs.getDate(3));
				bean.setMobileNo(rs.getLong(4));
				bean.setExpertise(rs.getInt(5));
			
				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of doctors");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}
