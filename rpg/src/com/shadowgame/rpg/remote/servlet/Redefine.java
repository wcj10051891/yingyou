package com.shadowgame.rpg.remote.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.shadowgame.rpg.service.Services;

@WebServlet
public class Redefine extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!ServletFileUpload.isMultipartContent(request)) {
			response.getWriter().println("not multipart content");
			return;
		}
		response.setCharacterEncoding("UTF-8");
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(10 * 1024 * 1024);

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			// Parse the request
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem f : items) {
				if(f.getContentType().equals("application/x-zip-compressed") && f.getName().endsWith(".jar")) {
					JarInputStream jar = new JarInputStream(f.getInputStream());
					JarEntry next = null;
					while((next = jar.getNextJarEntry()) != null) {
						String className = next.getName();
						className = className.replace("/", ".");
						className = className.replace(".class", "");
						Services.app.instrument.redefineClasses(new ClassDefinition(Class.forName(className), IOUtils.toByteArray(jar)));
						jar.closeEntry();
					}
					jar.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
			e.printStackTrace();
		}
		response.getWriter().flush();
	}
	
}
