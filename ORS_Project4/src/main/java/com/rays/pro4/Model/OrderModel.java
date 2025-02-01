
package com.rays.pro4.Model;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.OrderBean;
import com.rays.pro4.Bean.OrderBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.JDBCDataSource;

public class OrderModel {

	public int nextPK() throws Exception {

		String sql = ("SELECT MAX(ID) FROM ST_ORDER");
		Connection conn = null;
		int pk = 0;

		conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			pk = rs.getInt(1);

		}
		rs.close();
		return pk + 1;

	}

	public long add(OrderBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();
		int pk = 0;
		pk = nextPK();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO ST_ORDER VALUES(?,?,?,?,?)");

		ps.setLong(1, pk);
		ps.setString(2, bean.getProductName());
		ps.setDate(3, new Date(bean.getOrderDate().getTime()));
		ps.setInt(4, bean.getQuantity());
		ps.setInt(5, bean.getCoustomer());
		
		

		ps.executeUpdate();

		return pk;

	}

	public void update(OrderBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();
		PreparedStatement ps = conn
				.prepareStatement("UPDATE ST_ORDER SET PRODUCT_NAME=?,ORDERDATE=?, QUANTITY=?, CUSTOMER=? WHERE ID=?");

		ps.setString(1, bean.getProductName());
		ps.setDate(2, new Date(bean.getOrderDate().getTime()));
		ps.setInt(3, bean.getQuantity());
		ps.setInt(4, bean.getCoustomer());
		ps.setLong(5, bean.getId());

		ps.executeUpdate();

	}

	public void delete(OrderBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("DELETE FROM ST_ORDER WHERE ID=?");

		ps.setLong(1, bean.getId());
		ps.executeUpdate();
	}

	public OrderBean findByPK(long pk) throws Exception {

		String sql = ("SELECT * FROM ST_ORDER WHERE ID=?");
		OrderBean bean = null;
		Connection conn = null;

		conn = JDBCDataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, pk);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			bean = new OrderBean();
			bean.setId(rs.getLong(1));
			bean.setProductName(rs.getString(2));
			bean.setOrderDate(rs.getDate(3));
			bean.setQuantity(rs.getInt(4));
			bean.setCoustomer(rs.getInt(5));
			
			
		

		}
		return bean;
	}

	public List search(OrderBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_ORDER WHERE 1=1");
		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND ID =" + bean.getId());

			}

			
			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" AND PRODUCT_NAME like '" + bean.getProductName() + "%'");
			}
			 if (bean.getOrderDate() != null && bean.getOrderDate().getTime() > 0) {
				 Date d = new java.sql.Date(bean.getOrderDate().getTime());
			  sql.append(" AND ORDERDATE like '" + d+"%'" ); 
			  }
			 
			 
			
			/*
			 * if (bean.getOrderDate() != null && bean.getOrderDate().getDate() > 0) { Date
			 * d = new Date(bean.getOrderDate().getDate()); sql.append(" AND ORDERDATE = " +
			 * DataUtility.getDateString(d)); }
			 */
			  if (bean.getQuantity() > 0) {
					sql.append(" AND QUANTITY =  " + bean.getQuantity());

				}
			 
		}
		if (bean.getCoustomer()  > 0) {
			sql.append(" AND CUSTOMER like '" + bean.getCoustomer());
		}
		
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + "," + pageSize);
		}

		System.out.println("sql ====> " + sql.toString());
		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new OrderBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setOrderDate(rs.getDate(3));
				bean.setQuantity(rs.getInt(4));
				bean.setCoustomer(rs.getInt(5));
				
				list.add(bean);
				System.out.println("Amout is =====" );

			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception: Exception in Search User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;

	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_ORDER");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}
		System.out.println(sql);
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderBean bean = new OrderBean();
				bean.setId(rs.getLong(1));
				bean.setProductName(rs.getString(2));
				bean.setOrderDate(rs.getDate(3));
				bean.setQuantity(rs.getInt(4));
				bean.setCoustomer(rs.getInt(5));
				

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting list of users");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

}
