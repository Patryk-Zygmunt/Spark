package pl.edu.agh.kis.florist.dao;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import pl.edu.agh.kis.florist.db.tables.pojos.FileMetadata;
import pl.edu.agh.kis.florist.db.tables.pojos.FolderMetadata;
import pl.edu.agh.kis.florist.db.tables.records.FileMetadataRecord;
import pl.edu.agh.kis.florist.db.tables.records.FolderMetadataRecord;
import pl.edu.agh.kis.florist.model.Directory;
import pl.edu.agh.kis.florist.model.File;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.agh.kis.florist.db.Tables.FILE_METADATA;
import static pl.edu.agh.kis.florist.db.Tables.FOLDER_METADATA;

/**
 * Created by kantor on 17.01.17.
 */
public class DirectoryDAO {
    private final String DB_URL = "jdbc:sqlite:test.db";

    public List<Serializable> loadFolderContent(String path, String recursive) {
        try (DSLContext create = DSL.using(DB_URL)) {
            if(!pathExist(path))
                throw new IllegalStateException("Folder o podanej ścieżce nie istnieje");
            int parentId=getId(path);
             if(parentId==-2)return null;//
            List<FolderMetadata> recordFolderList = create.selectFrom(FOLDER_METADATA).where(FOLDER_METADATA.PARENT_FOLDER_ID.equal(parentId))
                   .fetchInto(FolderMetadata.class);
            // System.out.print(recordFolderList);
            List<FileMetadata> recordFileList = create.selectFrom(FILE_METADATA).where(FILE_METADATA.ENCLOSING_FOLDER_ID.equal(parentId))
                    .fetchInto(FileMetadata.class);

            List<Directory> contentFolder = recordFolderList.stream().map(Directory::new)
                    .collect(Collectors.toList());
            List<File> contentFile = recordFileList.stream().map(File::new)
                    .collect(Collectors.toList());

            List<Serializable> lista = new ArrayList<>();
            lista.addAll(contentFile);
            lista.addAll(contentFolder);
            return lista;

        }
    }
                      //znajde wszystkie foldery a potem foreach loadfolderconyent
    private List<FolderMetadata>  loadFolderRecursive (String path) {
        try (DSLContext create = DSL.using(DB_URL)) {
            List<FolderMetadata> folders = create.selectFrom(FOLDER_METADATA).where(FOLDER_METADATA.PARENT_FOLDER_ID.equal(0)).fetchInto(FolderMetadata.class);
            List<FolderMetadata> allFolders = new ArrayList<>();
            allFolders.addAll(folders);
            if (!folders.isEmpty()) {
                for (FolderMetadata record : folders
                        ) {
                    loadFolderRecursive(record.getPathDisplay());
                }
            } else return allFolders;


        return allFolders;}
    }

    public Object loadFolderMetaData(String path) {
        try (DSLContext create = DSL.using(DB_URL)) {
            if(!pathExist(path))
                throw new IllegalStateException("Folder o podanej ścieżce nie istnieje");
            char slash= path.charAt(path.length()-1);
            if(slash=='/'){
                FolderMetadataRecord record = create.selectFrom(FOLDER_METADATA).where(FOLDER_METADATA.PATH_DISPLAY.equal(path))
                        .fetchOne();
                Directory retrived = new Directory(record.into(FolderMetadata.class));
                return retrived;

            }
            else{
                FileMetadataRecord record = create.selectFrom(FILE_METADATA).where(FILE_METADATA.PATH_DISPLAY.equal(path))
                        .fetchOne();
               File retrived = new File(record.into(FileMetadata.class));
                return retrived;
            }
        }
    }
// folder nowy create.insert((FOLDER_METADATA,FOlderrMETADA.PATAH_LOWER..values(i wpisuje warrtposci)
//get folder id z bazy danych selectem po  bazie znalezc

    public Directory storeDirectory(Directory dir) {
        try (DSLContext create = DSL.using(DB_URL)) {
            FolderMetadataRecord record = create.newRecord(FOLDER_METADATA, dir);
            record.store();
            Directory retrived = new Directory(record.into(FolderMetadata.class));
            return retrived;
        }
    }

    public File storeFile(File file) {
        try (DSLContext create = DSL.using(DB_URL)) {
            FileMetadataRecord record = create.newRecord(FILE_METADATA, file);
            record.store();
            File retrived = new File(record.into(FileMetadata.class));
            return retrived;
        }
    }

    private int getId(String path) {
        try (DSLContext create = DSL.using(DB_URL)) {
            if(!pathExist(path))
                throw new IllegalStateException("Folder  o podanej ścieżce nie istnieje");
            char slash= path.charAt(path.length()-1);
            int Id;
            if(slash=='/') {
                FolderMetadataRecord record = create.selectFrom(FOLDER_METADATA).where(FOLDER_METADATA.PATH_DISPLAY.equal(path)).fetchAny();
                // System.out.print(">" + record + "<");
                Directory parentDirectory = new Directory(record.into(FolderMetadata.class));
                Id = parentDirectory.getFolderId();
            return Id;}
            else {
                FileMetadataRecord record = create.selectFrom(FILE_METADATA).where(FILE_METADATA.PATH_DISPLAY.equal(path)).fetchAny();
                // System.out.print(">" + record + "<");
                 File file = new File(record.into(FileMetadata.class));
                Id = file.getFileId();
                return Id;}
        }
    }

    public Integer deleteFolder(String path) {
        try (DSLContext create = DSL.using(DB_URL)) {
            char slash= path.charAt(path.length()-1);
            if (!pathExist(path)) {
                throw new IllegalStateException("Folder o podanej ścieżce nie istnieje!!!");

            }
            if(slash=='/') {
                Integer wynik = create.delete(FOLDER_METADATA).where(FOLDER_METADATA.PATH_DISPLAY.equal(path))
                    .execute();
            return wynik;}
            else{
                Integer wynik = create.delete(FILE_METADATA).where(FILE_METADATA.PATH_DISPLAY.equal(path))
                        .execute();
                return wynik;
            }
        }
    }

    public void moveFolderWithContent(String oldPath, String newPath){
        List<Serializable> Folders =new ArrayList<>(loadFolderContent(oldPath,"true"));
        for (Serializable folder:Folders
             ) { //FolderMetadataRecord)folder.getVa
        }
    }
//move powinno zmieniac parent id

    public String moveFolder(String oldPath, String newPath) {
        try (DSLContext create = DSL.using(DB_URL)) {
            if(!pathExist(oldPath))
                throw new IllegalStateException("Folder o podanej ścieżce nie istnieje");
            char slash= oldPath.charAt(oldPath.length()-1);

            LocalDateTime timePoint = LocalDateTime.now();
            String changedTime = timePoint.toString();
            String part[]=newPath.split("/");
            String parentPath="";
            for(int i=0;i<part.length-1;i++)
                parentPath=parentPath+part[i]+"/";
            //System.out.println(">>"+parentPath);
            String newFolderPath ="";
           int  parentId = getId(parentPath);
           if(slash=='/'){
                    create.update(FOLDER_METADATA).set(FOLDER_METADATA.PATH_DISPLAY, newPath)
                    .set(FOLDER_METADATA.PATH_LOWER, newPath.toLowerCase()).set(FOLDER_METADATA.PARENT_FOLDER_ID,parentId)
                            .where(FOLDER_METADATA.PATH_DISPLAY.equal(oldPath)).execute();
                    //.returning(FOLDER_METADATA.PATH_DISPLAY)
                   // .fetchOne().getValue(FOLDER_METADATA.PATH_DISPLAY);
            return newFolderPath;}
            else{
                create.update(FILE_METADATA).set(FILE_METADATA.PATH_DISPLAY, newPath)
                        .set(FILE_METADATA.PATH_LOWER, newPath.toLowerCase()).set(FILE_METADATA.ENCLOSING_FOLDER_ID,parentId)
                        .set(FILE_METADATA.SERVER_CHANGED_AT,changedTime)
                        .where(FILE_METADATA.PATH_DISPLAY.equal(oldPath)).execute();

            return newFolderPath;
        }
    }}

    private boolean pathExist(String path) {
        try (DSLContext create = DSL.using(DB_URL)) {
            char slash=path.charAt(path.length()-1);
            if(slash=='/')
            {FolderMetadataRecord record = create.selectFrom(FOLDER_METADATA).where(FOLDER_METADATA.PATH_DISPLAY.equal(path)).fetchAny();
                if (record == null)
                    return false;
                else {
                    return true;
                }}
            else{
                FileMetadataRecord record = create.selectFrom(FILE_METADATA).where(FILE_METADATA.PATH_DISPLAY.equal(path)).fetchAny();
            if (record == null)
                return false;
            else {
                return true;
            }
        }
    }}

    public Directory storeDirectory(String ParentPath, String folderName) {
        try (DSLContext create = DSL.using(DB_URL)) {
            int parentId;

            if (pathExist(ParentPath + "/" + folderName + "/")) {
                System.out.println("Folder o podanej ścieżce już istnieje!!!");
                return null;
            }


            if (ParentPath.equals("")) {
                parentId = 0;
            } else {
                //FolderMetadataRecord record   = create.selectFrom(FOLDER_METADATA).where(FOLDER_METADATA.PATH_DISPLAY.equal(ParentPath)).fetchOne();
                //Directory parentDirectory =new Directory(record.into(FolderMetadata.class));
                parentId = getId(ParentPath);
                // if(parentId==-2)return null;

            }
            LocalDateTime timePoint = LocalDateTime.now();
            String newFolderName = folderName;
            String pathDisplay = ParentPath + "/" + folderName + "/";
            String pathLower = pathDisplay.toLowerCase();
            String serverTime = timePoint.toString();

            Directory newDirectory = new Directory(null,folderName, pathLower, pathDisplay, parentId, serverTime);
            FolderMetadataRecord record = create.newRecord(FOLDER_METADATA, newDirectory);
            record.store();
            // Directory directory =new Directory();

            //tu wytworzyc wszystkie dane dla bazy danych getParetnt(ParentPath) id potrzebne
            //crete fo.lder z tych parametrów


            Directory retrived = new Directory(record.into(FolderMetadata.class));
            //  System.out.print(retrived);
            return retrived;

        }

    }
    public Object renameFile(String path, String name) {
        try (DSLContext create = DSL.using(DB_URL)) {
            if(!pathExist(path))
                throw new IllegalStateException("Folder o podanej ścieżce nie istnieje");
            String newPath="";
            String part[]=path.split("/");
            String fileName=part[part.length-1];
            for(int i=0;i<part.length-1;i++){
                newPath=newPath+part[i]+"/";
            }
            String filePath=newPath+name;
            LocalDateTime timePoint = LocalDateTime.now();
            String changedTime = timePoint.toString();
            create.update(FILE_METADATA).set(FILE_METADATA.NAME,name).set(FILE_METADATA.PATH_DISPLAY, filePath)
                    .set(FILE_METADATA.PATH_LOWER, filePath.toLowerCase()).set(FILE_METADATA.SERVER_CHANGED_AT,changedTime)
                    .where(FILE_METADATA.PATH_DISPLAY.equal(path)).execute();

            return changedTime;
        }
    }

    public static void main(String args[]) {


        LocalDateTime timePoint = LocalDateTime.now();
        new DirectoryDAO().moveFolder("/kolejny/folder/", "/nowy/folder/");

    }


}