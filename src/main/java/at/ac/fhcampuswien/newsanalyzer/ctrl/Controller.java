package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "6001323e43f44f908aee1c0650e26d9e";  //TODO add your api key

	public void process(String query, Category category) {
		System.out.println("Start process");

		//TODO load the news based on the parameters
		NewsApi newsApi;
		if (category != null) {
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

		//TODO implement Error handling
		NewsResponse newsResponse = null;
		try {
			newsResponse = newsApi.getNews();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		//TODO implement methods for analysis
		if (newsResponse != null) {
			List<Article> articles = newsResponse.getArticles();

			if (articles.isEmpty()) {
				System.out.println("No Articles to analyze ");
			} else {
				//5 - a
				System.out.println("How many Articles: " + articles.stream().count());

				//5 - b
				String provider = articles.stream()
								.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
								.entrySet().stream().max(Comparator.comparingInt(a -> Math.toIntExact(a.getValue()))).get().getKey();

				if (provider != null)
					System.out.println("Most published Articles: " + provider);

				//5 - c
				String author = articles.stream()
								.filter(article -> Objects.nonNull(article.getAuthor()))
								.min(Comparator.comparingInt(article -> article.getAuthor().length()))
								.get().getAuthor();

				if ( author != null ){
					System.out.println("Shortest author: " + author);
				}

				//5 - d
				List<Article> sorted = articles.stream()
						.sorted(Comparator.comparingInt(a -> a.getTitle().length()))
						.sorted(Comparator.comparing(Article::getTitle))
						.collect(Collectors.toList());

				System.out.println("First sorted Article: " + sorted.get(0));

				for (Article article : articles) {
					try {
						URL url = new URL(article.getUrl());
						InputStream is = url.openStream();
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						BufferedWriter wr =
								new BufferedWriter(new FileWriter(article.getTitle().substring(0, 10) + ".html"));
						String line;
						while ((line = br.readLine()) != null) {
							wr.write(line);
						}
						br.close();
						wr.close();

					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
			}
		}
		System.out.println("End process");
	}
	

	public Object getData() {
		
		return null;
	}
}
