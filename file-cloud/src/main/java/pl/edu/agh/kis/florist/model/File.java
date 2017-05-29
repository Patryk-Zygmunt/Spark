package pl.edu.agh.kis.florist.model;

import pl.edu.agh.kis.florist.db.tables.pojos.FileMetadata;
import pl.edu.agh.kis.florist.db.tables.pojos.FolderMetadata;

/**
 * Created by kantor on 20.01.17.
 */
public class File extends FileMetadata {

    public File(FileMetadata value) {
        super(value);
    }

    public File(Integer fileId, String name, String pathLower, String pathDisplay, Integer enclosingFolderId, String serverCreatedAt, String serverChangedAt) {

        super(fileId, name, pathLower, pathDisplay, enclosingFolderId, 20, serverCreatedAt, serverChangedAt,null);
    }


}
