package com.ooush.api.utils;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

	public static String injectContent(String htmlMessage, String target, String content) {
		return htmlMessage.replace("{" + target + "}", Strings.nullToEmpty(content));
	}

	public static String withCustomActionButton(String url, String buttonMessage) {
		String buttonTemplate = "<tr>"
									+ "<td style='padding:50px 0;font-size: 16px;'>"
										+"<a "
											+ "href='{Url}'"
											+ "style='background:#0984AB;color:#FFFFFF;padding:20px;text-decoration:none;'"
										+ ">"
											+ "{ButtonMessage}"
										+ "</a>"
									+ "</td>"
								+ "</tr>";
		return buttonTemplate.replace("{Url}", url).replace("{ButtonMessage}", Strings.nullToEmpty(buttonMessage));
	}

}
