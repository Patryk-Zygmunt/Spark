CREATE TABLE file_metadata (
    file_id INTEGER(8) PRIMARY KEY,
    name TEXT,
    path_lower TEXT,
    path_display TEXT,
    enclosing_folder_id INTEGER(8),
    size INTEGER(8),
    server_created_at TEXT,
    server_changed_at TEXT
);
