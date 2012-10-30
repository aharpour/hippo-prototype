/*
 *  Copyright 2012 Smile Group Benelux B.V.
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
package com.tdclighthouse.prototype.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class StreamParser {

	private List<HandlerPair<?>> handlers = new ArrayList<HandlerPair<?>>();

	public <T> StreamParser(EventListener<T> handleable, EventHandler<T> handler) {
		if (handleable == null || handler == null) {
			throw new IllegalArgumentException("handler or handleable can not be null");
		}
		handlers.add(new HandlerPair<T>(handleable, handler));
	}
	
	public StreamParser(List<HandlerPair<?>> handlers) {
		if (handlers.size() == 0) {
			throw new IllegalArgumentException("there must at list be one handler in the list");
		}
		this.handlers.addAll(handlers);
	}

	public void parse(File file) throws FileNotFoundException, JAXBException, SAXException,
			ParserConfigurationException, IOException {
		this.parse(new FileInputStream(file));
	}

	public void parse(final InputStream inputStream) throws JAXBException, SAXException, ParserConfigurationException,
			IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(handlers.get(0).handleable.getClass().getPackage().getName());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setListener(unmarshalerListener);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XMLReader reader = factory.newSAXParser().getXMLReader();
		reader.setContentHandler(unmarshaller.getUnmarshallerHandler());
		reader.parse(new InputSource(inputStream));
	}

	private final Listener unmarshalerListener = new Listener() {

		@Override
		public void beforeUnmarshal(Object target, Object parent) {
			setHandlerToObject(target, StreamParser.this.handlers, false);
		}

		@Override
		public void afterUnmarshal(Object target, Object parent) {
			setHandlerToObject(target, StreamParser.this.handlers, true);
		}

		@SuppressWarnings("unchecked")
		private void setHandlerToObject(Object target, List<HandlerPair<?>> handlers, boolean setNull) {
			for (HandlerPair<?> handlerPair : handlers) {
				
				if (target != null && target.getClass().equals(handlerPair.handleable.getClass())) {
					@SuppressWarnings("rawtypes")
					EventListener casted = (EventListener) target;
					if (!setNull) {
						casted.setHandler(handlerPair.handler);
					} else {
						casted.setHandler(null);
					}
				}
			}
		}
	};

	public static class HandlerPair<T> {
		private final EventListener<T> handleable;
		private final EventHandler<T> handler;

		public HandlerPair(EventListener<T> handleable, EventHandler<T> handler) {
			this.handleable = handleable;
			this.handler = handler;
		}

	}

}
