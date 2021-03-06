package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.xhtml.XHTMLManager;
import util.xhtml.XHTMLManagerFactory;

/**
 * This class manipulates XHTML files.
 */
public class XHTMLManipulator extends HypertextManipulator {

    private XHTMLManager xMng = null;

    /**
     * Uses the XHTMLManagerFactory ({@link XHTMLManagerFactory}) to create a
     * concrete XHTMLManager ({@link XHTMLManager}) that will be used to do the
     * underlying work in the XHTML file source code.
     */
    public XHTMLManipulator() {
        XHTMLManagerFactory xMngFactory = new XHTMLManagerFactory();
        xMng = xMngFactory.create();
    }

    @Override
    public boolean load(String strPath) {
        try {
            return xMng.load(strPath);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getTitle() {
        try {
            return xMng.getTitle();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean setTitle(String hypertextTitle) {
        try {
            return xMng.setTitle(hypertextTitle);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean persistChanges(String hypertextPath) {
        try {
            return xMng.persistChanges(hypertextPath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeCSS(String hypertextPath) {
        try {
            String strCode = xMng.getXHTMLContents();
            String preCode = "";
            String aftCode = "";

            //parse through the XHTML code to find all <link> elements
            Pattern p = Pattern.compile("<link(.*?)</link>",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
            Matcher m = p.matcher(strCode);
            boolean result = m.find();

            //loop through all occurrences
            while (result) {
                //remove CSS <link> elements from source
                String element = m.group();
                if (element.indexOf("type=\"text/css\"") > 0) {
                    int endIndex = strCode.indexOf(element);
                    //retrieve source code prior to the removee element
                    preCode = strCode.substring(0, endIndex);
                    //retrieve source code after the removee element
                    aftCode = strCode.substring(endIndex + element.length());
                    //update source code
                    strCode = preCode + aftCode;
                }
                result = m.find();
            }

            //parse through the update XHTML code to find all <style> elements
            p = Pattern.compile("<style(.*?)</style>",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
            m = p.matcher(strCode);
            result = m.find();

            //loop through all occurrences
            while (result) {
                //remove CSS <style> elements from source
                String element = m.group();
                if (element.indexOf("type=\"text/css\"") > 0) {
                    int endIndex = strCode.indexOf(element);
                    //retrieve source code prior to the removee element
                    preCode = strCode.substring(0, endIndex);
                    //retrieve source code after the removee element
                    aftCode = strCode.substring(endIndex + element.length());
                    //update source code
                    strCode = preCode + aftCode;
                }
                result = m.find();
            }

            //persist changes
            xMng.loadFromString(strCode);
            xMng.persistChanges(hypertextPath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeParentDirsFromHyperlinks(String hypertextPath) {
        try {
            String strCode = xMng.getXHTMLContents();
            String preCode = "";
            String aftCode = "";

            //parse through the XHTML code to find all <a> elements
            Pattern p = Pattern.compile("<a(.*?)</a>",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
            Matcher m = p.matcher(strCode);
            boolean result = m.find();

            //loop through all occurrences
            int elementStart = 0;
            int beginIndex = 0;
            int endIndex = 0;
            String newHref = "";
            String element = "";
            while (result) {
                //only process internal links
                element = m.group();
                if ((element.indexOf("href=") > 0)
                        && (element.indexOf("www.") < 0)) {
                    //remove parent directories from "href=" element
                    elementStart = strCode.indexOf(element);
                    //find where the href attribute begins
                    beginIndex = element.indexOf("href=\"") + 6;
                    //and where it ends
                    endIndex = element.indexOf("\"", beginIndex);
                    //retrieve href attribute value
                    newHref = element.substring(beginIndex, endIndex);
                    //retrieve source code prior to the href element
                    preCode = strCode.substring(0, elementStart + beginIndex);
                    //retrieve source code after the href element
                    aftCode = strCode.substring(elementStart + endIndex);

                    //remove parent dirs from href attribute
                    newHref = newHref.substring(newHref.lastIndexOf("/") + 1);

                    //update source code
                    strCode = preCode + newHref + aftCode;
                }
                result = m.find();
            }

            //update editor source code
            xMng.loadFromString(strCode);
            //commit changes
            xMng.persistChanges(hypertextPath);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String[] getTokens(String startDelimiter, String endDelimiter) {
        List<String> paragraphs = null;
        String[] res = null;

        try {
            // Retrieve html source
            String html = xMng.getXHTMLContents();
            String tmp = "";
            int beginIndex = 0;
            // find first token position
            beginIndex = html.indexOf(startDelimiter);
            if (beginIndex > 0) {
                // instantiate placeholder
                paragraphs = new ArrayList<String>();
            }
            // Traverse entire html    
            while (beginIndex != -1) {
                // Retrieve fragments beetwen the 'start' and 'end' delimiters
                tmp = html.substring(beginIndex,
                        html.indexOf(endDelimiter, beginIndex) + endDelimiter.length());
                // add token to the placeholder
                paragraphs.add(tmp.trim());
                // update search offset
                beginIndex = html.indexOf(startDelimiter, html.indexOf(endDelimiter, beginIndex));
            }

            if (paragraphs.size() > 0) {
                res = new String[paragraphs.size()];
                // load the tokens in the String array
                int index = 0;
                for (Iterator iterPara = paragraphs.iterator(); iterPara.hasNext();) {
                    res[index++] = (String) (iterPara.next());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
