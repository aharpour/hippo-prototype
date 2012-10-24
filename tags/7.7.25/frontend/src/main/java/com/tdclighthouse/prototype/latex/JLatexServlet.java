/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.latex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JLabel;

import org.apache.commons.lang3.StringEscapeUtils;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 * @author Ebrahim Aharpour
 *
 */
public class JLatexServlet extends HttpServlet {

	private static final String IMAGE_TYPE = "png";
	private static final String MIME_TYPE = "image/png";
	private static final String LATEX_PARAM = "latex";
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String latexExpression = req.getParameter(LATEX_PARAM);
		latexExpression = StringEscapeUtils.unescapeXml(latexExpression);
		
		if (latexExpression != null && !"".equals(latexExpression)) {
			BufferedImage image = generateImage(latexExpression);
			resp.setContentType(MIME_TYPE);
			ImageIO.write(image, IMAGE_TYPE, resp.getOutputStream());
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private synchronized BufferedImage generateImage(String latex) {
		TeXFormula formula = new TeXFormula(latex);
		TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
		icon.setInsets(new Insets(5, 5, 5, 5));

		BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
		JLabel jl = new JLabel();
		jl.setForeground(new Color(0, 0, 0));
		icon.paintIcon(jl, g2, 0, 0);
		return image;
	}

}
