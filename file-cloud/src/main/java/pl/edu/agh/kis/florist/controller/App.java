package pl.edu.agh.kis.florist.controller;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import pl.edu.agh.kis.florist.dao.DirectoryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import pl.edu.agh.kis.florist.dao.AuthorDAO;
import pl.edu.agh.kis.florist.dao.BookDAO;
import pl.edu.agh.kis.florist.exceptions.ParameterFormatException;
import pl.edu.agh.kis.florist.model.ParameterFormatError;
import spark.Request;
import spark.ResponseTransformer;

public class App {

	final static private Logger LOGGER = LoggerFactory.getILoggerFactory().getLogger("requests");

	public static void main(String[] args) {

		final String AUTHORS_PATH = "/authors";
		final String BOOKS_PATH = "/books";
		final String BOOK_PATH = "/books/:bookid";

		final Gson gson = new Gson();
		final ResponseTransformer json = gson::toJson;
		final DirectoryController directoryController = new DirectoryController(new DirectoryDAO());
		final BookController bookController = new BookController(new AuthorDAO(),new BookDAO());
		//Changes port on which server listens
		port(4567);

		//registers filter before processing of any request with special metothod stated below
		//this method is run to log request with logger
		//but similar method can be used to check user authorisation
		before("/*/", (req, res) -> {
			info(req);
		});

		//registers HTTP GET on resource /atuthors 
		//and delegates processing into BookController
		get(AUTHORS_PATH, (request, response) -> {
			return bookController.handleAllAuthors(request, response);
		}, json);

		//registers HTTP POST on resource /atuthors 
		//and delegates processing into BookController
		post(AUTHORS_PATH, (request, response) -> {
			return bookController.handleCreateNewAuthor(request,response);
		}, json);

		//registers HTTP GET on resource /books 
		//and delegates processing into BookController
		get(BOOKS_PATH, (request, response) -> {
			return bookController.hadleAllBooks(request,response);
		}, json);
		
		//registers HTTP GET on resource /books/{bookId} 
		//and delegates processing into BookController
		get(BOOK_PATH, (request, response) -> {
			return bookController.handleSingleBook(request,response);
		}, json);
		
		//handleSingleBook can throw ParameterFromatException which will be processed
		//gracefully instead of 500 Internal Server Error
		exception(ParameterFormatException.class,(ex,request,response) -> {
			response.status(403);
			response.body(gson.toJson(new ParameterFormatError(request.params())));
		});

		put("/files/:path/create_directory",(request,response) -> {
			return directoryController.handleCreateDirectory(request,response);
		},json);

		get("/files/:path/list_folder_content",(request,response) -> {
			return directoryController.handleListFolderContent(request,response);
		},json);


		delete("/files/:path/delete",(request,response) -> {
			return directoryController.handleDeleteFolder(request,response);
		},json);

		put("/files/:path/move",(request,response) -> {
			return directoryController.handleMoveFolder(request,response);
		},json);
		get("/files/:path/get_meta_data",(request,response) -> {

			return directoryController.handleListFolderMetaData(request,response);
		},json);
		put("/files/:path/rename",(request,response) -> {

			return directoryController.handleRename(request,response);
		},json);


		exception(Exception.class,(ex,req,res)-> {
			System.err.println(String.format("format: %s",req.uri()));
			System.err.println(String.format("params: %s",req.params()));
			System.err.println(String.format("headers: %s",req.headers()));
			System.err.println(String.format("cookies: %s",req.cookies()));
			System.err.println(String.format("body: %s",req.body()));
//cos innego jeszcze?
			ex.printStackTrace();
		});


	}

	private static void info(Request req) {
		LOGGER.info("{}", req);
	}

}
