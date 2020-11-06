package com.wnagboran.comparison.bean;

import lombok.Data;
import org.springframework.util.StringUtils;


/**
 * 类描述
 *
 * @author WangBoran
 * @since 2020/11/4 15:15
 */
@Data
public class CaseBean {

	private String bh;

	private String ah;

	private String larq;

	private String jarq;


	@Override
	public String toString() {

		String ahTmp = StringUtils.isEmpty(ah)?"案号为空":ah;
		String larqTmp = StringUtils.isEmpty(larq)?"立案日期为空":larq;
		String jarqTmp = StringUtils.isEmpty(jarq)?"结案日期为空":jarq;

		return bh + "\t" + ahTmp + "\t" + larqTmp + "\t" + jarqTmp + "\t";
	}
}