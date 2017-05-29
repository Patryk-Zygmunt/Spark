CREATE TABLE file_contents (
    file_id INTEGER(8) PRIMARY KEY,
    contents BLOB,

    FOREIGN KEY(file_id) REFERENCES file_metadata(file_id)
);
