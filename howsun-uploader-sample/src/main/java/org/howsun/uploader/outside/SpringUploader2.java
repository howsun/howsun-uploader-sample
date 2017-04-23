/**
 * 
 */
package org.howsun.uploader.outside;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.howsun.utils.Files;
import org.howsun.utils.Streams;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 说明:
 * 
 * @author howsun ->[howsun.zhang@gmail.com]
 * @version 1.0
 *
 * 2017年4月1日 上午11:32:33
 */
@Controller
public class SpringUploader2 {

	/**
	 * 文件仓库
	 */
	public static String UPLOAD_DIR = "D:/data/repository/upload";
	
	@Resource
	private ServletContext servletContext;
	
	
	/**
	 * 多文件上传
	 * @return
	 */
	@RequestMapping(value = "/upload_by_spring_outside", method = RequestMethod.GET)
	public String uploads(){
		return "upload/by_spring_outside";
	}
	
	
	/**
	 * 多文件上传
	 * @param file
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/upload_by_spring_outside", method = RequestMethod.POST)
	public String uploadings(@RequestParam(required = false) MultipartFile[] file, Model model){
		for(MultipartFile f : file){
			uploading(f, model);
		}
		return "message";
	}
	
	
	/**
	 * 单个文件上传
	 * @param file
	 * @param model
	 */
	@RequestMapping(value = "/upload_by_spring_outside_single", method = RequestMethod.POST)
	public void uploading(@RequestParam(required = false) MultipartFile file, Model model){
		try {
			String name = file.getOriginalFilename();
			System.out.println("文件名：" + name);
			
			InputStream inputStream = file.getInputStream();
			OutputStream out = new FileOutputStream(new File(UPLOAD_DIR, UUID.randomUUID().toString() + Files.getExtName(name)));
			Streams.copy(inputStream, out, true);
			
			model.addAttribute("message", "上传成功！");
		} catch (Exception e) {
			model.addAttribute("message", "上传失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
}
