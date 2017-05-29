package pl.edu.agh.kis.florist.controller;

import com.google.gson.Gson;
import pl.edu.agh.kis.florist.dao.DirectoryDAO;
import pl.edu.agh.kis.florist.model.Directory;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * Created by kantor on 17.01.17.
 */
public class DirectoryController {
    private DirectoryDAO directoryDAO;
    private final Gson gson = new Gson();
    private static final int CREATED = 201;

    public DirectoryController(DirectoryDAO directoryDao) {
        this.directoryDAO = directoryDao;
    }

    public Object handleCreateDirectory(Request req, Response res){
        String Path=req.params("path");
        String part[]=Path.split("/");
        String parentPath="";
        String folderName=part[part.length-1];
        for(int i=0;i<part.length-1;i++){
            parentPath=parentPath+part[i]+"/";
        }// String
        //Directory directory = gson.fromJson(req.body(), Directory.class);
        Directory result = directoryDAO.storeDirectory(parentPath,folderName);
        res.status(CREATED);
        return result;

    }
    public Object handleListFolderContent(Request req, Response res){ //rozdzielic na path rodzica
        String Path=req.params("path");
        String recursive=req.queryParams("recursive");
        List result= directoryDAO.loadFolderContent(Path,recursive);
        return result;
    }

    public Object handleListFolderMetaData(Request req, Response res){
        String Path=req.params("path");
       Object result = directoryDAO.loadFolderMetaData(Path);
        return result;
    }
    public Integer handleDeleteFolder(Request req, Response res){
        String Path=req.params("path");
     return  directoryDAO.deleteFolder(Path);

    }
    public String handleMoveFolder(Request req, Response res){
        String oldPath=req.params("path");
        String newPath=req.queryParams("new_path");

        return directoryDAO.moveFolder(oldPath,newPath);

    }
    public Object handleRename(Request req, Response response) {
        String path=req.params("path");
        String name=req.queryParams("name");
        return directoryDAO.renameFile(path,name);
    }

public static void main(String args[]){
        String p="file/path1/pah22/nazwa/";
        /*String part[]=p.split("/");
        System.out.println(part[0]+" "+part[1]+" "+part[2]+" "+part[part.length-1]);
        System.out.println(part.length);
    String parentPath="";
    String folderName=part[part.length-1];
    for(int i=1;i<part.length-1;i++){
        parentPath=parentPath+part[i]+"/";

    }*/
        char parentPath= p.charAt(p.length()-1);
    System.out.println(parentPath);
}



}