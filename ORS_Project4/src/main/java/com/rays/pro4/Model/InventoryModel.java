package com.rays.pro4.Model;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

//import org.apache.log4j.Logger;

import com.rays.pro4.Bean.InventoryBean;
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
 * JDBC Implementation of InventoryModel.
 * 
 * @author priya mandal;
 *
 */

public class InventoryModel {
	private static Logger log = Logger.getLogger(InventoryModel.class);

	/**
	 * Find next PK of Inventory
	 *
	 * @throws DatabaseException
	 */

	public int nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");

		String sql = "SELECT MAX(Id) FROM st_inventory";
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
	 * Inventory Add
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
	public long add(InventoryBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "INSERT INTO st_inventory VALUES(?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getSupplierName());
			pstmt.setDate(3, new java.sql.Date(bean.getLastUpdatedDate().getTime()));
			pstmt.setInt(4, bean.getQuantity());
			pstmt.setInt(5, bean.getProduct());

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
	 * Delete a Inventory
	 *
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(InventoryBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "delete from st_inventory where id=?";
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
	 * Find Inventory by PK
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public InventoryBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "select * from st_inventory where id=?";
		InventoryBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new InventoryBean();
				bean.setId(rs.getLong(1));
				bean.setSupplierName(rs.getString(2));
				bean.setLastUpdatedDate(rs.getDate(3));
				bean.setQuantity(rs.getInt(4));
				bean.setProduct(rs.getInt(5));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting Inventory by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method Find By PK end");
		return bean;
	}

	/**
	 * Update a inventory
	 *
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(InventoryBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "update st_inventory set SUPPLIER_NAME=?, LASTUPDATE_DATE=?, QUANTITY=?, PRODUCT=? where id=? ";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bean.getSupplierName());
			pstmt.setDate(2, new java.sql.Date(bean.getLastUpdatedDate().getTime()));
			pstmt.setInt(3, bean.getQuantity());
			pstmt.setInt(4, bean.getProduct());
			pstmt.setLong(5, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("update inventory>> " + i);
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
	 * Search Inventory
	 *
	 * @param bean : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(InventoryBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Inventory with pagination
	 *
	 * @return list : List of Inventorys
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 *
	 * @throws DatabaseException
	 */

	public List search(InventoryBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("select * from st_inventory where 1=1");
		if (bean != null) {

			if (bean.getSupplierName() != null && bean.getSupplierName().length() > 0) {
				sql.append(" AND SUPPLIER_NAME like '" + bean.getSupplierName() + "%'");
			}

			if (bean.getProduct() > 0) {
				sql.append(" AND PRODUCT = " + bean.getProduct());
			}

			if (bean.getLastUpdatedDate() != null && bean.getLastUpdatedDate().getTime() > 0) {
				Date d = new Date(bean.getLastUpdatedDate().getTime());
				sql.append(" AND LAST_UPDATED_DATE = " + d);
			}
			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
			}

			if (bean.getQuantity() > 0) {
				sql.append(" AND QUANTITY = " + bean.getQuantity());
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
				bean = new InventoryBean();
				bean.setId(rs.getLong(1));
				bean.setSupplierName(rs.getString(2));
				bean.setLastUpdatedDate(rs.getDate(3));
				bean.setQuantity(rs.getInt(4));
				bean.setProduct(rs.getInt(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search Inventory");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Search end");
		return list;

	}

	/**
	 * Get List of Inventory
	 *
	 * @return list : List of Inventory
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Inventory with pagination
	 *
	 * @return list : List of inventorys
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_inventory");

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
				InventoryBean bean = new InventoryBean();

				bean.setId(rs.getLong(1));
				bean.setSupplierName(rs.getString(2));
				bean.setLastUpdatedDate(rs.getDate(3));
				bean.setQuantity(rs.getInt(4));
				bean.setProduct(rs.getInt(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of inventorys");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}
