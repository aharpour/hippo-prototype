package com.tdclighthouse.prototype.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_SIZE = 300;
    private static final int DEFAULT_MAX_SIZE = 800;
    private static final int DEFAULT_MAXIMUM_CONTENT_LENGTH = 200;
    
    private int maximumSize;
    private int defaultSize;
    private int maximumContentLength;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        maximumSize = getIntegerInitParameter(config, "maximumSize", DEFAULT_MAX_SIZE);
        defaultSize = getIntegerInitParameter(config, "defaultSize", DEFAULT_SIZE);
        maximumContentLength = getIntegerInitParameter(config, "maximumContentLength", DEFAULT_MAXIMUM_CONTENT_LENGTH);
    }

    private int getIntegerInitParameter(ServletConfig config, String paramName, int defaultValue) {
        int result;
        String parameter = config.getInitParameter(paramName);
        if (StringUtils.isNotBlank(parameter)) {
            result = Integer.parseInt(parameter);
        } else {
            result = defaultValue;
        }
        return result;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String content = getContent(req);
            int size = getSize(req);

            BitMatrix bitMatrix;
            resp.setContentType("image/png");
            resp.setHeader("Content-Disposition", "filename=\"QRCode.png\"");
            bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size);
            ServletOutputStream outputStream = resp.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
            outputStream.close();
        } catch (WriterException e) {
            throw new IOException(e);
        }
    }

    private int getSize(HttpServletRequest req) {
        int result = defaultSize;
        String parameter = req.getParameter("size");
        if (StringUtils.isNumeric(parameter)) {
            result = Integer.parseInt(parameter);
            result = Math.min(maximumSize, result);
        }
        return result;
    }

    private String getContent(HttpServletRequest req) throws ServletException {
        String content = req.getParameter("c");
        if (content.length() > maximumContentLength) {
            throw new ServletException("the content of the QRCode could not exceed 200 characters.");
        }
        return content;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
