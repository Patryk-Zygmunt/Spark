package pl.edu.agh.kis.florist.controller;
import com.google.gson.Gson;
import pl.edu.agh.kis.florist.dao.FileDAO;
import pl.edu.agh.kis.florist.model.File;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * Created by kantor on 21.01.17.
 */
public class FileController {
    private FileDAO fileDAO;
    private final Gson gson = new Gson();
    private static final int CREATED = 201;

    public FileController(FileDAO fileDao) {
        this.fileDAO = fileDao;
    }

   /* public Object handleUploadFile(Request req, Response res){
        String Path=req.params("path");
        String part[]=Path.split("/");
        String enclosingPath="";
        String folderName=part[part.length-1];
        for(int i=0;i<part.length-1;i++){
           enclosingPath=enclosingPath+part[i]+"/";
        }// String
        //Directory directory = gson.fromJson(req.body(), Directory.class);
        File result = fileDAO.uploadFile(enclosingPath,folderName);
        res.status(CREATED);
        return result;

    }*/
    /*public Object handleListFolderContent(Request req, Response res){ //rozdzielic na path rodzica
        String Path=req.params("path");
        String recursive=req.queryParams("recursive");
        List result= directoryDAO.loadFolderContent(Path,recursive);
        return result;
    }*/

    /*public Object handleFileDownload(Request req, Response res){
        String Path=req.params("path");
        File result= fileDAO.fileDownload(Path);
        return result;
    }*/
   /* public Integer handleFileUplad(Request req, Response res){
        String Path=req.params("path");
        return  fileDAO.uploadFile(Path);

    }*/


    }

