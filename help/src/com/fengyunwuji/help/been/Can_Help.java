package com.fengyunwuji.help.been;

import cn.bmob.v3.BmobObject;

/**
 * ���԰���
 * 
 * @com.fengyunwuji.help.been
 * @author �޽���
 * @date 2016-2-29����7:36:39
 * 
 */
public class Can_Help extends BmobObject {

	private String title;// ����
	private String describe;// ����
	private String phone;// ��ϵ�ֻ�

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
