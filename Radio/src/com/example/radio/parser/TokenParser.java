package com.example.radio.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.radio.entity.ResponseInfo;

import android.util.Log;
import android.util.Xml;

public class TokenParser {
	
	private static final String TAG_CODE = "code";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_MESSAGE = "message";
	
	public ResponseInfo parse(InputStream stream){
	
	XmlPullParser parser = Xml.newPullParser();
	
	ResponseInfo info = new ResponseInfo();
	
	try {
		parser.setInput(stream, null);
		int eventType = parser.getEventType();
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String localName = parser.getName();
			switch (eventType) {
				case XmlPullParser.START_TAG:			
					if (TAG_CODE.equals(localName)) {
							info.setCode(readText(parser));						
					}

					if (TAG_TOKEN.equals(localName)) {
							info.setToken(readText(parser));						
						}
					
					if (TAG_MESSAGE.equals(localName)) {
						info.setMessage(readText(parser));						
					}
					break;
			}
			try {
				eventType = parser.next();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	} catch (XmlPullParserException e) {
		e.printStackTrace();
	}finally {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	return info;
	
	}
	
	private String readText(XmlPullParser parser)  {
		String result = "";
		try {
			if (parser.next() == XmlPullParser.TEXT) {
				result = parser.getText();
				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
