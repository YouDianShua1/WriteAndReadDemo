package com.wnagboran.comparison.service;

import com.csvreader.CsvReader;
import com.google.common.collect.Maps;
import com.wnagboran.comparison.bean.CaseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述
 *
 * @author WangBoran
 * @since 2020/11/4 10:18
 */
@Service
@Slf4j
public class CompareService {

	public void compareCSV(MultipartFile file1, MultipartFile file2, HttpServletResponse response) {

		HashMap<String,CaseBean> file1Beans = getBeans(file1);
		HashMap<String,CaseBean> file2Beans = getBeans(file2);

		//file1中存在， 但是file2中不存在的案件
		List<CaseBean> infile1NotInFile2 = compareList(file1Beans, file2Beans);

		//file2中存在，但是file1中不存在的key
		List<CaseBean> infile2NotInFile1 = compareList(file2Beans, file1Beans);

		OutputStream out = null;

		try {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("compare_result.txt", StandardCharsets.UTF_8.toString()));
			out = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (Objects.nonNull(out)) {
			//写结果
			writeResult(out, infile1NotInFile2, file1.getOriginalFilename(), file2.getOriginalFilename());
			writeResult(out, infile2NotInFile1, file2.getOriginalFilename(), file1.getOriginalFilename());
		}

		if (Objects.nonNull(out)) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void writeResult(OutputStream out,
	                         List<CaseBean> beanList,
	                         String name1, String name2) {
		if (!CollectionUtils.isEmpty(beanList)) {
			try {
				out.write(("find " + beanList.size() + " cases in " + name1 + " not in " + name2 + ":\r\n").getBytes(StandardCharsets.UTF_8));
				for (CaseBean bean : beanList) {
					out.write((bean.toString() + "\r\n").getBytes(StandardCharsets.UTF_8));
				}
			} catch (IOException e) {
				log.error("IOException:", e);
			}
		}
	}

	private HashMap<String,CaseBean> getBeans(MultipartFile file) {

		HashMap<String,CaseBean> hashMap = new HashMap<>();

		Reader reader;

		try {
			reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				String[] strings = line.split(",");

				CaseBean caseBean = new CaseBean();
				caseBean.setBh(trimAndReplace(strings[0]));

				caseBean.setAh(trimAndReplace(strings[1]));

				caseBean.setLarq(trimAndReplace(strings[2]));
				if (strings.length == 3) {
					caseBean.setJarq("");
				} else {
					caseBean.setJarq(trimAndReplace(strings[3]));
				}
				hashMap.put(caseBean.getBh(),caseBean);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	private String trimAndReplace(String string) {
		return string.replace("\"", "").replace("\uFEFF","").trim();
	}

	private List<CaseBean> compareList(HashMap<String,CaseBean> map1, HashMap<String,CaseBean> map2) {
		List<CaseBean> beansInList1NotInList2 = new ArrayList<>();

		map1.forEach((k,v)->{

			if(Objects.isNull(map2.get(k))){
				beansInList1NotInList2.add(v);
			}

		});

		return beansInList1NotInList2;
	}

}