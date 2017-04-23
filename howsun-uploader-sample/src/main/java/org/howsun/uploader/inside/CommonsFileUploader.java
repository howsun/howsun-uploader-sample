/**
 * 
 */
package org.howsun.uploader.inside;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.howsun.utils.Streams;

/**
 * 说明:
 * 
 * @author howsun ->[howsun.zhang@gmail.com]
 * @version 1.0
 *
 * 2017年4月1日 上午10:12:26
 */
public class CommonsFileUploader extends HttpServlet {

	
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
	        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
	        for (FileItem item : items) {
	            if (!item.isFormField()){
	                // Process form file field (input type="file").
	                //String fieldName = item.getFieldName();//控件上标的名字
	                String fileName = FilenameUtils.getName(item.getName());
	                InputStream fileContent = item.getInputStream();
	                
	                String realPathDir = getServletConfig().getServletContext().getRealPath(UPLOAD_DIR);
	                OutputStream out = new FileOutputStream(new File(realPathDir, UUID.randomUUID().toString() + org.howsun.utils.Files.getExtName(fileName)));
	    			Streams.copy(fileContent, out, true);
	    			request.setAttribute("message", "上传成功！");
	            }
	        }

		} catch (Exception e) {
			request.setAttribute("message", "上传失败：" + e.getMessage());
			e.printStackTrace();
		}
		request.getRequestDispatcher("/WEB-INF/view/message.jsp").forward(request, resp);
	}
	
	
}
