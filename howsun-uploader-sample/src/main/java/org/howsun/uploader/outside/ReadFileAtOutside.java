/**
 * 
 */
package org.howsun.uploader.outside;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 说明:
 * 
 * @author howsunimport org.springframework.web.bind.annotation.RequestMapping;
 ->[howsun.zhang@gmail.com]
 * @version 1.0
 *
 * 2017年4月1日 上午11:36:42
 */
@Controller
public class ReadFileAtOutside {

	
	public static String DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
	
	@Resource
	private ServletContext servletContext;
	
	
	/**
	 * 浏览器浏览
	 * http://locahost/pics/03090c95-2666-4f09-8a32-8abb633ad1506.jpg
	 */
	@RequestMapping("/pics/**")
	public void view(HttpServletRequest request, HttpServletResponse response){
		FileInputStream fileInputStream = null;
		try {
			String pathInfo = request.getPathInfo();
			if(pathInfo == null){
				pathInfo = request.getRequestURI();
			}
			
			String filename = URLDecoder.decode(pathInfo.substring(5), "UTF-8");
			File file = new File(SpringUploader2.UPLOAD_DIR, filename);
			
			/*
			 * 实现304协议
			 */
			long lastModified = file.lastModified();
			SimpleDateFormat formater = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);

			String eTag = String.valueOf(file.lastModified());
			response.addHeader("ETag", eTag);
			response.addHeader("Last-Modified", formater.format(new Date(lastModified)));
			
			String ifNoneMatch = request.getHeader("If-None-Match");
			if(eTag.equals(ifNoneMatch)){
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			
			//向浏览器发送
			fileInputStream = new FileInputStream(file);
			
			response.setHeader("Content-Type", servletContext.getMimeType(filename));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
			
			org.howsun.utils.Streams.copy(fileInputStream, response.getOutputStream(), true);
			
		} catch (Exception e) {
			response.setStatus(500);
			e.printStackTrace();
		} finally{
			org.howsun.utils.Streams.closeQuietly(fileInputStream);
		}
	}
	
	
	/**
	 * 以二进制形式下载
	 * http://locahost/pics/03090c95-2666-4f09-8a32-8abb633ad1506.jpg
	 */
	@RequestMapping(value = "/pics/**", params = "download=true")
	public void downloading(HttpServletRequest request, HttpServletResponse response){
		FileInputStream fileInputStream = null;
		try {
			String pathInfo = request.getPathInfo();
			if(pathInfo == null){
				pathInfo = request.getRequestURI();
			}
			
			String filename = URLDecoder.decode(pathInfo.substring(5), "UTF-8");
			File file = new File(SpringUploader2.UPLOAD_DIR, filename);
			
			/*
			 * 实现304协议
			 */
			long lastModified = file.lastModified();
			SimpleDateFormat formater = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
			
			String eTag = String.valueOf(file.lastModified());
			response.addHeader("ETag", eTag);
			response.addHeader("Last-Modified", formater.format(new Date(lastModified)));
			
			String ifNoneMatch = request.getHeader("If-None-Match");
			if(eTag.equals(ifNoneMatch)){
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			
			//向浏览器发送
			fileInputStream = new FileInputStream(file);
			
			//改变头部指令，让浏览下载而不是显示
			response.addHeader("pragma","NO-cache");
			response.addHeader("Cache-Control","no-cache");
			response.addDateHeader("Expries", 0);
			response.setContentType("application/x-download");
			try {filename = new String(filename.getBytes("UTF-8"), "ISO8859_1");}catch (Exception e) {e.printStackTrace();}
			response.addHeader("Content-Disposition","attachment;filename=" + filename);

			org.howsun.utils.Streams.copy(fileInputStream, response.getOutputStream(), true);
			
		} catch (Exception e) {
			response.setStatus(500);
			e.printStackTrace();
		} finally{
			org.howsun.utils.Streams.closeQuietly(fileInputStream);
		}
	}
}
