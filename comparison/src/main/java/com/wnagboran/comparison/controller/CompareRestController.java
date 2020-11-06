package com.wnagboran.comparison.controller;

import com.wnagboran.comparison.service.CompareService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 类描述
 *
 * @author WangBoran
 * @since 2020/11/4 10:14
 */
@RestController
@RequestMapping("/api/v1/comparison")
public class CompareRestController {

	@Autowired
	CompareService compareService;

	@PostMapping
	@ApiOperation("compare csv")
	public void compareCSV(@RequestBody MultipartFile file1, @RequestBody MultipartFile file2, HttpServletResponse response){
		compareService.compareCSV(file1,file2,response);
	}

}