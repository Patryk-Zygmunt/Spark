package pl.edu.agh.kis.florist.model;

import pl.edu.agh.kis.florist.db.tables.pojos.FolderMetadata;

/**
 * Created by kantor on 19.01.17.
 */
public class Directory extends FolderMetadata {


    public Directory(
            Integer folderId,
            String  name,
            String  pathLower,
            String  pathDisplay,
            Integer parentFolderId,
            String  serverCreatedAt
            //Integer ownerId
    ) {
        super(folderId, name, pathLower, pathDisplay, parentFolderId, serverCreatedAt, null);

    }
    public Directory(Directory dir){
        super(dir);
    }
    public Directory( FolderMetadata dir){
        super(dir);
    }
    private static final long serialVersionUID=-5684917134468290041L;
}
