CREATE TABLE folder_folder_contents (
    parent_folder_id INTEGER(8),
    contained_folder_id INTEGER(8),

    PRIMARY KEY(parent_folder_id,contained_folder_id),
    FOREIGN KEY(parent_folder_id) REFERENCES folder_metadata(folder_id),
    FOREIGN KEY(contained_folder_id) REFERENCES folder_metadata(folder_id)
);
