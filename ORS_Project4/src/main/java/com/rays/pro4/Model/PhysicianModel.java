package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.PhysicianBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class PhysicianModel {

	/**
	 * 
	 */
	private static Logger log = Logger.getLogger(PhysicianModel.class);

	/**
	 * @return
	 * @throws DatabaseException
	 */
	public int nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");

		String sql = "SELECT MAX(ID) FROM ST_PHYSICIAN";
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
	 * Add a Physician
	 *
	 * @param bean
	 * @throws DatabaseException
	 *
	 */

	public long add(PhysicianBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "INSERT INTO ST_PHYSICIAN VALUES(?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFullName());
			pstmt.setDate(3, new java.sql.Date(bean.getBirthDate().getTime()));
			pstmt.setString(4, bean.getPhone());
			pstmt.setString(5, bean.getSpecialization());
			// date of birth caste by sql date

			int a = pstmt.executeUpdate();
			System.out.println("+++++++++++++++++++++++" + a);
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
	 * Delete a Physician
	 *
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(PhysicianBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "DELETE FROM ST_PHYSICIAN WHERE ID=?";
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
	 * Find Physician by PK
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public PhysicianBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "SELECT * FROM ST_PHYSICIAN WHERE ID=?";
		PhysicianBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PhysicianBean();
				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setBirthDate(rs.getDate(3));
				bean.setPhone(rs.getString(4));
				bean.setSpecialization(rs.getString(5));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting Physician by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method Find By PK end");
		return bean;
	}

	/**
	 * Update a client
	 *
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(PhysicianBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "UPDATE ST_PHYSICIAN SET FULL_NAME=?,BIRTH_DATE=?,PHONE=?,SPECIALIZATION=? WHERE ID=?";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getFullName());
			pstmt.setDate(2, new java.sql.Date(bean.getBirthDate().getTime()));
			pstmt.setString(3, bean.getPhone());
			pstmt.setString(4, bean.getSpecialization());
			pstmt.setLong(5, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("update client>> " + i);
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

	public List search(PhysicianBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Physician with pagination
	 *
	 * @return list : List of Physicians
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 *
	 * @throws DatabaseException
	 */

	public List search(PhysicianBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_PHYSICIAN WHERE 1=1");
		if (bean != null) {

			if (bean.getFullName() != null && bean.getFullName().length() > 0) {
				sql.append(" AND FULL_NAME like '" + bean.getFullName() + "%'");
			}
			if (bean.getBirthDate() != null && bean.getBirthDate().getTime() > 0) {
				Date d = new Date(bean.getBirthDate().getTime());
				sql.append(" AND Birth_DATE = '" + d + "'");
				System.out.println("done");
			}
			if (bean.getPhone() != null && bean.getPhone().length() > 0) {
				sql.append(" AND PHONE like '" + bean.getPhone() + "%'");
			}
			if (bean.getSpecialization() != null && bean.getSpecialization().length() > 0) {
				sql.append(" AND SPECIALIZATION like '" + bean.getSpecialization() + "%'");
			}

			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
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
				bean = new PhysicianBean();
				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setBirthDate(rs.getDate(3));
				bean.setPhone(rs.getString(4));
				bean.setSpecialization(rs.getString(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search Physician");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Search end");
		return list;

	}

	/**
	 * Get List of Physician
	 *
	 * @return list : List of Physician
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Physician with pagination
	 *
	 * @return list : List of clients
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from ST_PHYSICIAN");

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
				PhysicianBean bean = new PhysicianBean();
				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setBirthDate(rs.getDate(3));
				bean.setPhone(rs.getString(4));
				bean.setSpecialization(rs.getString(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of clients");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}
