/**
 * 
 */
package org.howsun.uploader.gridfs;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.howsun.gridfs.MongoGridFsService;
import org.howsun.uploader.outside.ReadFileAtOutside;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/**
 * 说明:
 * 
 * @author howsun ->[howsun.zhang@gmail.com]
 * @version 1.0
 *
 * 2017年4月2日 上午10:32:27
 */
@Controller
public class MongoGridFsController {

	@Resource
	private ServletContext servletContext;
	
	@Resource
	private MongoGridFsService mongoGridFsService;
	
	
	/**
	 * 多文件上传
	 * @return
	 */
	@RequestMapping(value = "/upload_by_gridfs", method = RequestMethod.GET)
	public String uploads(){
		return "upload/by_gridfs";
	}
	
	
	/**
	 * 多文件上传
	 * @param file
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/upload_by_gridfs", method = RequestMethod.POST)
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
	@RequestMapping(value = "/upload_by_gridfs_single", method = RequestMethod.POST)
	public void uploading(@RequestParam(required = false) MultipartFile file, Model model){
		try {
			if(file == null || file.isEmpty()){
				throw new RuntimeException("指定要上传的文件");
			}
			
			String filename = file.getOriginalFilename();
			System.out.println("文件名：" + filename);
			
			InputStream inputStream = file.getInputStream();
			//读取到ImageBuffer，获取图片长宽
			
			GridFSFile gridFSFile = mongoGridFsService.upload(inputStream, filename, servletContext.getMimeType(filename), null);
			
			model.addAttribute("message", "上传成功！");
			model.addAttribute("gridFSFile", gridFSFile);
		} catch (Exception e) {
			model.addAttribute("message", "上传失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/gridfs/{id}")
	public void view(@PathVariable String id, HttpServletRequest request, HttpServletResponse response){
		InputStream inputStream = null;
		try {
			
			GridFSDBFile gridFSDBFile = mongoGridFsService.download(id);
			
			/*
			 * 实现304协议
			 */
			long lastModified = gridFSDBFile.getUploadDate().getTime();
			SimpleDateFormat formater = new SimpleDateFormat(ReadFileAtOutside.DATE_PATTERN, Locale.ENGLISH);

			String eTag = String.valueOf(lastModified);
			response.addHeader("ETag", eTag);
			response.addHeader("Last-Modified", formater.format(gridFSDBFile.getUploadDate()));
			
			String ifNoneMatch = request.getHeader("If-None-Match");
			if(eTag.equals(ifNoneMatch)){
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			
			//向浏览器发送
			
			response.setHeader("Content-Type", servletContext.getMimeType(gridFSDBFile.getFilename()));
			response.setHeader("Content-Length", String.valueOf(gridFSDBFile.getLength()));
			response.setHeader("Content-Disposition", "inline; filename=\"" + gridFSDBFile.getFilename() + "\"");
			
			inputStream = gridFSDBFile.getInputStream();
			org.howsun.utils.Streams.copy(inputStream, response.getOutputStream(), true);
			
		} catch (Exception e) {
			response.setStatus(500);
			e.printStackTrace();
		} finally{
			org.howsun.utils.Streams.closeQuietly(inputStream);
		}
	}
}
