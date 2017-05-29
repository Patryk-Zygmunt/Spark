package pl.edu.agh.kis.florist.dao;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.florist.db.Tables;
import pl.edu.agh.kis.florist.db.tables.pojos.FileMetadata;
import pl.edu.agh.kis.florist.db.tables.pojos.FolderMetadata;
import pl.edu.agh.kis.florist.db.tables.records.FileMetadataRecord;
import pl.edu.agh.kis.florist.db.tables.records.FolderMetadataRecord;
import pl.edu.agh.kis.florist.model.Directory;
import pl.edu.agh.kis.florist.model.File;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static pl.edu.agh.kis.florist.db.tables.FileMetadata.FILE_METADATA;
import static pl.edu.agh.kis.florist.db.tables.FolderMetadata.FOLDER_METADATA;

/**
 * Created by kantor on 20.01.17.
 */
public class FileDAOTest {
    private final String DB_URL = "jdbc:sqlite:test.db";
    private DSLContext create;
    @Before
    public void setUp() throws Exception {
        create = DSL.using(DB_URL);
        // clean up all tables
        create.deleteFrom(FILE_METADATA).execute();
        create.deleteFrom(FOLDER_METADATA).execute();
        //create.deleteFrom(AUTHORS).execute();
        //create.deleteFrom(BOOKS).execute();
    }
    @Test
    public void storeFile(){
        Directory dir = new Directory(1,"nowfq","/nowfq/","/nowfq/",0,"14:00");
       File file = new File(12,"nowy","/nowy","/nowy",1,"14:00","14:00");

        FolderMetadataRecord record1 = create.newRecord(Tables.FOLDER_METADATA, dir);      record1.store();

        // when:
        //int i=DirectoryDAO().deleteFolder("/nowf/kolejny/");
        File file1= new DirectoryDAO().storeFile(file);
        System.out.println(new DirectoryDAO().loadFolderContent("/nowfq/","d"));
        /*Directory folder2= new DirectoryDAO().storeDirectory(dir2);*/
        assertNotNull(file1);
      /*  assertThat(dir).extracting(Directory::getName, Directory::getPathDisplay,, Directory::getPathLower).containsOnly("nowy, "/nowy/");
        assertThat(book.getId()).isGreaterThan(0);
        List<Author> extractedAuthors = book.getAuthors();
        assertThat(extractedAuthors).hasSize(1);
        assertThat(extractedAuthors.get(0)).extracting(Author::getFirstName, Author::getLastName).containsOnly("Michał",
                "Bułchakow");
        assertThat(extractedAuthors.get(0).getId()).isGreaterThan(0);*/
    }
    @Test
    public void moveFile(){
        Directory dir = new Directory(1,"nowfq","/nowfq/","/nowfq/",0,"14:00");
        Directory dir2 = new Directory(2,"s1","/s1/","/s1/",0,"14:00");
        File file = new File(12,"nowy","/s1/nowy","/s1/nowy",2,"14:00","14:00");
        FolderMetadataRecord record1 = create.newRecord(Tables.FOLDER_METADATA, dir);      record1.store();
        FolderMetadataRecord record2 = create.newRecord(Tables.FOLDER_METADATA, dir2);      record2.store();
        FileMetadataRecord record = create.newRecord(Tables.FILE_METADATA,file);      record.store();

        System.out.println(new DirectoryDAO().loadFolderContent("/s1/","d:"));
        new DirectoryDAO().moveFolder("/s1/nowy","/nowfq/nowy");
        System.out.println(new DirectoryDAO().loadFolderMetaData("/nowfq/nowy"));
        FileMetadataRecord result = create.selectFrom(Tables.FILE_METADATA).where(Tables.FILE_METADATA.PATH_DISPLAY.equal("/nowfq/nowy"))
                .fetchOne();
        File movedFile= new File(result.into(FileMetadata.class));
        assertNotNull(movedFile);
        assertThat(movedFile).extracting(File::getPathDisplay).containsOnly( "/nowfq/nowy");

        assertThat(movedFile.getEnclosingFolderId()).isEqualTo(1);

    }
    @Test
    public void renameFile(){
        Directory dir = new Directory(1,"nowfq","/nowfq/","/nowfq/",0,"14:00");
        Directory dir2 = new Directory(2,"s1","/s1/","/s1/",0,"14:00");
        File file = new File(12,"nowy","/s1/nowy","/s1/nowy",2,"14:00","14:00");
        FolderMetadataRecord record1 = create.newRecord(Tables.FOLDER_METADATA, dir);      record1.store();
        FolderMetadataRecord record2 = create.newRecord(Tables.FOLDER_METADATA, dir2);      record2.store();
        FileMetadataRecord record = create.newRecord(Tables.FILE_METADATA,file);      record.store();

        System.out.println(new DirectoryDAO().loadFolderContent("/s1/","d:"));
        new DirectoryDAO().renameFile("/s1/nowy","nowaNazwa");
        System.out.println(new DirectoryDAO().loadFolderContent("/s1/","d"));
        FileMetadataRecord result = create.selectFrom(Tables.FILE_METADATA).where(Tables.FILE_METADATA.PATH_DISPLAY.equal("/s1/nowaNazwa"))
                .fetchOne();
        File movedFile= new File(result.into(FileMetadata.class));
        assertNotNull(movedFile);
        assertThat(movedFile).extracting(File::getPathDisplay).containsOnly( "/s1/nowaNazwa");
        assertThat(movedFile).extracting(File::getName).containsOnly( "nowaNazwa");



    }

    @Test
    public void deleteFile(){
        Directory dir = new Directory(1,"nowfq","/nowfq/","/nowfq/",0,"14:00");
        File file = new File(12,"nowy","/nowfq/nowy","/nowfq/nowy",1,"14:00","14:00");

        FileMetadataRecord record = create.newRecord(Tables.FILE_METADATA,file);      record.store();
        FolderMetadataRecord record1 = create.newRecord(Tables.FOLDER_METADATA, dir);      record1.store();

        System.out.println(new DirectoryDAO().loadFolderContent("/nowfq/","d:"));
        new DirectoryDAO().deleteFolder("/nowfq/nowy");
        System.out.println(new DirectoryDAO().loadFolderContent("/nowfq/","s"));
        // FolderMetadataRecord result = create.selectFrom(Tables.FOLDER_METADATA).where(Tables.FOLDER_METADATA.PATH_DISPLAY.equal("/nowfq/kolejny/"))
        //  .fetchOne();
        // Directory deletedFolder= new Directory(result.into(FolderMetadata.class));
        // folder3=new DirectoryDAO().loadFolderMetaData("/nowfq/kolejny/");
        // assertNull(folder3);
        // assertThat(dir3).extracting( Directory::getPathDisplay).containsOnly( "/nowy/kolejny/");
        // assertThat(dir3.getFolderId()).isGreaterThan(0);

        // assertThat(dir3.getParentFolderId()).isEqualTo(1);

    }
    @Test
    public void getFileMetadata(){
        Directory dir = new Directory(1,"nowfq","/nowfq/","/nowfq/",0,"14:00");
        File file = new File(12,"nowy","/nowfq/nowy","/nowfq/nowy",1,"14:00","14:00");
        FileMetadataRecord record = create.newRecord(Tables.FILE_METADATA,file);      record.store();
        FolderMetadataRecord record1 = create.newRecord(Tables.FOLDER_METADATA, dir);      record1.store();



        System.out.println(new DirectoryDAO().loadFolderMetaData("/nowfq/nowy"));
        assertNotNull(new DirectoryDAO().loadFolderMetaData("/nowfq/nowy"));
    }
    @Test

    public void loadFolderContent(){
        Directory dir = new Directory(1,"nowfq","/nowfq/","/nowfq/",0,"14:00");
        Directory dir3 = new Directory(3,"kolejny","/nowfq/kolejny/","/nowfq/kolejny/",1,"14:00");
        Directory dir2 = new Directory(32,"k2","/nowfq/k2/","/nowfq/k2/",1,"14:00");
        File file = new File(12,"nowy","/nowfq/nowy","/nowfq/nowy",1,"14:00","14:00");
        FileMetadataRecord record = create.newRecord(Tables.FILE_METADATA,file);      record.store();
        FolderMetadataRecord record1 = create.newRecord(Tables.FOLDER_METADATA, dir);       record1.store();
        FolderMetadataRecord record3 = create.newRecord(Tables.FOLDER_METADATA, dir3);      record3.store();
        FolderMetadataRecord record2 = create.newRecord(Tables.FOLDER_METADATA, dir2);      record2.store();
        System.out.println(new DirectoryDAO().loadFolderContent("/nowfq/","s"));
        List<Serializable> result=new DirectoryDAO().loadFolderContent("/nowfq/","res");
        assertNotNull(result);

        assertThat(result).hasSize(3);
 /*       assertThat(new DirectoryDAO().loadFolderContent("/nowfq/kolejny/","res")).extracting( Directory::getPathDisplay).containsOnly( "/nowfq/kolejny/");
        assertThat(new DirectoryDAO().loadFolderMetaData("/nowfq/kolejny/")).extracting( Directory::getName).containsOnly( "kolejny");
        // assertThat(new DirectoryDAO().loadFolderMetaData("/nowfq/kolejny/")).extracting( Directory::get).containsOnly( "/nowfq/kolejny/");
        assertThat(new DirectoryDAO().loadFolderMetaData("/nowfq/kolejny/").getFolderId()).isGreaterThan(0);

        assertThat(new DirectoryDAO().loadFolderMetaData("/nowfq/kolejny/").getParentFolderId()).isEqualTo(1);*/
    }


    @After
    public void tearDown() throws Exception {

    }

}