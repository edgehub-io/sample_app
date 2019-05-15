package com.xiangli2.grove;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GroveServlet
 */
@WebServlet("/rotaryvalue")
public class GroveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, Integer> cameraFindings = new HashMap<>();
    private int criminal_appearance = 0;
    private String msg =  "";
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public GroveServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
        request.setAttribute("message", msg);
        request.getRequestDispatcher("/WEB-INF/hello.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    PrintWriter out = response.getWriter();
	    BufferedReader reader = request.getReader();
	    try {
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
            String receivedMsg = stringBuffer.toString();
            if (receivedMsg.contains("camera")) {
                // "camera1", "camera2"
                String camName = receivedMsg;
                criminal_appearance++;
                msg = "";
                msg += "Found criminal from " + camName;
                cameraFindings.put(camName, cameraFindings.getOrDefault(camName, 0) + 1);
                msg += "\nTotal criminal appearance:" + criminal_appearance;
                for (Map.Entry<String, Integer> entry : cameraFindings.entrySet()) {
                    msg += "\nAsset " + entry.getKey() + " has found " + entry.getValue() + " times";
                }
                if (criminal_appearance > 4) {
                    msg += "\nCRIMINAL FOUND!";
                    msg += "\nNEW HUNTING STARTED.";
                    criminal_appearance = 0;
                } else {
                    msg += "\nSTILL NEED MORE PICS.";
                }
            } else if (receivedMsg.contains("reset")) {
                criminal_appearance = 0;
                msg = "COUNT RESET";
            }
	    } catch (Exception e) {
	        throw e;
	    }
		doGet(request, response);
	}

	public List<File> listFilesForFolder(final File folder) {
	    List<File> files = new ArrayList<>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            files.add(fileEntry);
	        }
	    }
	    return files;
	}
}
