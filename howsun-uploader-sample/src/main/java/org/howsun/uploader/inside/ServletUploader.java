/**
 * 
 */
package org.howsun.uploader.inside;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.howsun.utils.Files;
import org.howsun.utils.Streams;

/**
 * 说明:
 * 
 * @author howsun ->[howsun.zhang@gmail.com]
 * @version 1.0
 *
 * 2017年4月1日 上午10:12:26
 */
public class ServletUploader extends HttpServlet {

	
	private static final long serialVersionUID = 1987554323349768291L;

	public static String UPLOAD_DIR = "upload";
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/view/upload/by_servlet.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)	throws ServletException, IOException {
		try {
			Part part = request.getPart("file");
			String name = Paths.get(getSubmittedFileName(part)).getFileName().toString();
			System.out.println("文件名：" + name);
			
			String realPathDir = getServletConfig().getServletContext().getRealPath(UPLOAD_DIR);
			System.out.println("真实物理路径：" + realPathDir);
			
			InputStream inputStream = part.getInputStream();
			OutputStream out = new FileOutputStream(new File(realPathDir, UUID.randomUUID().toString() + Files.getExtName(name)));
			Streams.copy(inputStream, out, true);
			
			request.setAttribute("message", "上传成功！");
		} catch (Exception e) {
			request.setAttribute("message", "上传失败：" + e.getMessage());
			e.printStackTrace();
		}
		request.getRequestDispatcher("/WEB-INF/view/message.jsp").forward(request, resp);
	}
	
	
	private static String getSubmittedFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	         return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}
}
