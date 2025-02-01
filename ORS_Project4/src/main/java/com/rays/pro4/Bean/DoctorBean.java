package com.rays.pro4.Bean;



import java.util.Date;

public class DoctorBean extends BaseBean {

	private String name;
	private Date dateOfBirth;
	private long mobileNo;
	private int expertise;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getExpertise() {
		return expertise;
	}

	public void setExpertise(int expertise) {
		this.expertise = expertise;
	}

	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return id+"";
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return name;
	}

}


