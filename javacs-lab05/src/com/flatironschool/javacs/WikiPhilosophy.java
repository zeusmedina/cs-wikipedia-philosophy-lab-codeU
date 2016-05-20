package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
  final static List<String> visitedLinks = new ArrayList<String>();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started
    String startUrl = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		String endUrl =  "https://en.wikipedia.org/wiki/Philosophy";
	

    testPath(startUrl, endUrl);
		

  }

  public static void testPath(String startUrl, String endUrl) throws IOException {
    for(int i = 0; i < 11; i++) {
      if(visitedLinks.contains(startUrl)) {
        System.out.println("Ended back at start page");
          return;
      } else {
         visitedLinks.add(startUrl);
      }
      
      Element elm = findFirstValidLink(startUrl);
      if(elm == null) {
        System.out.println("No links on this page");
        return;
      }
      startUrl = elm.attr("abs:href");
      System.out.println(startUrl);
      if(startUrl.equals(endUrl)) {
        System.out.println("The myth is true!");
        break;
      }
  }

  }

  public static Element findFirstValidLink(String url) throws IOException {
    System.out.println("Searching path...");
    Elements paragraphs = wf.fetchWikipedia(url);
    Element elt = findFirstLink(paragraphs);
    return elt;
  }

  public static Element findFirstLink(Elements paragraphs) {
    for(Element paragraph: paragraphs) {
      Element firstLink = findFirstLinkInPara(paragraph);
      if(firstLink != null) {
        return firstLink;
      } else {
        return null;
      }
    }
    return null;
  }

  public static Element findFirstLinkInPara(Node root) {
    Iterable<Node> wikiNode = new WikiNodeIterable(root);

    for(Node node: wikiNode) {
      if(node instanceof Element) {
        Element firstLink = checkElementForLink((Element) node);
        if(firstLink != null) {
          return firstLink;
        }
      }
    }
    return null;
  }

  public static Element checkElementForLink(Element elt) {
    if(isValidLink(elt)) {
      return elt;
    }
    return null;
  }

  public static boolean isValidLink(Element elm) {
    if(!elm.tagName().equals("a")) {
      return false;
    }
    if (isItalic(elm)) {
      return false;
    }
    if(hasUpperCase(elm)) {
      return false;
    }
    return true;
  }

  public static boolean isItalic(Element start) {
    for (Element elt=start; elt != null; elt = elt.parent()) {
      if (elt.tagName().equals("i") || elt.tagName().equals("em")) {
        return true;
      }
    }
    return false;
  }

  public static boolean hasUpperCase(Element elm) {
    String wikiPageTitle = elm.text();
    boolean hasUppercase = !wikiPageTitle.equals(wikiPageTitle.toLowerCase());
    return hasUppercase;
  }

}
