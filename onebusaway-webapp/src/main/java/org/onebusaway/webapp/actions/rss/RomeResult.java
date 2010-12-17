package org.onebusaway.webapp.actions.rss;

import java.io.Writer;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionInvocation;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;

public class RomeResult extends StrutsResultSupport {

  private static final long serialVersionUID = 1L;

  /**
   * By default, we look for a "feed" on the value stack
   */
  private String feedName = "feed";

  /**
   * By default, we output RSS 2.0
   */
  private String feedType = "rss_2.0";

  /**
   * 
   */
  private String mimeType = "application/rss+xml";

  private String encoding;

  public void setFeedName(String feedName) {
    this.feedName = feedName;
  }

  public void setFeedType(String feedType) {
    this.feedType = feedType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Implementation of
   * {@link org.apache.struts2.dispatcher.StrutsResultSupport#doExecute(String, com.opensymphony.xwork2.ActionInvocation)}
   * 
   * @param location final location (jsp page, action, etc)
   * @param actionInvocation the ActionInvocation
   * @throws Exception
   */
  public void doExecute(String location, ActionInvocation actionInvocation)
      throws Exception {

    if (feedName == null)
      throw new IllegalArgumentException(
          "required \"feedName\" parameter missing");

    ServletActionContext.getResponse().setContentType(mimeType);

    SyndFeed feed = (SyndFeed) actionInvocation.getStack().findValue(feedName);

    if (feed != null) {
      if (encoding == null)
        encoding = feed.getEncoding();

      if (encoding != null)
        ServletActionContext.getResponse().setCharacterEncoding(encoding);

      if (feedType != null)
        feed.setFeedType(feedType);

      SyndFeedOutput output = new SyndFeedOutput();

      Writer out = null;
      try {
        out = ServletActionContext.getResponse().getWriter();
        output.output(feed, out);
      } catch(Exception ex) {
        ex.printStackTrace();
      } finally {
        // close the output writer (will flush automatically)
        if (out != null)
          out.close();
      }
    } else {
      throw new IllegalStateException("feed not found");
    }
  }
}