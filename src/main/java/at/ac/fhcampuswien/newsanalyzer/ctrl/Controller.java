package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "6001323e43f44f908aee1c0650e26d9e";  //TODO add your api key

	public void process(String query, Category category) {
		System.out.println("Start process");

		//TODO implement Error handling

		//TODO load the news based on the parameters
		NewsApi newsApi;
		if ( category!=null ) {
			newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(query)
					.setEndPoint(Endpoint.TOP_HEADLINES)
					.setSourceCountry(Country.at)
					.setSourceCategory(category)
					.createNewsApi();
		} else {
			newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(query)
					.setEndPoint(Endpoint.TOP_HEADLINES)
					.setSourceCountry(Country.at)
					.createNewsApi();
		}

		NewsResponse newsResponse = newsApi.getNews();

		//TODO implement methods for analysis
		if(newsResponse != null){
			List<Article> articles = newsResponse.getArticles();

			//5 - a
			System.out.println("How many Articles: " + articles.stream().count());

			//5 - b
			System.out.println("Most Published Articles: " +
					articles.stream()
							.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
							.entrySet().stream().max(Comparator.comparingInt(a -> Math.toIntExact(a.getValue()))).get().getKey());

			//5 - c
			System.out.println("Shortest Author: " +
					articles.stream()
							.filter(article -> Objects.nonNull(article.getAuthor()))
							.min(Comparator.comparingInt(article -> article.getAuthor().length()))
							.get().getAuthor());

		}

		System.out.println("End process");
	}
	

	public Object getData() {
		
		return null;
	}
}
